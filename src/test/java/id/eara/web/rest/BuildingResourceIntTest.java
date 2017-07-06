package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Building;
import id.eara.repository.BuildingRepository;
import id.eara.service.BuildingService;
import id.eara.repository.search.BuildingSearchRepository;
import id.eara.service.dto.BuildingDTO;
import id.eara.service.mapper.BuildingMapper;
import id.eara.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BuildingResource REST controller.
 *
 * @see BuildingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class BuildingResourceIntTest {

    private static final String DEFAULT_FACILITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FACILITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingSearchRepository buildingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBuildingMockMvc;

    private Building building;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuildingResource buildingResource = new BuildingResource(buildingService);
        this.restBuildingMockMvc = MockMvcBuilders.standaloneSetup(buildingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
            .facilityCode(DEFAULT_FACILITY_CODE)
            .description(DEFAULT_DESCRIPTION);
        return building;
    }

    @Before
    public void initTest() {
        buildingSearchRepository.deleteAll();
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getFacilityCode()).isEqualTo(DEFAULT_FACILITY_CODE);
        assertThat(testBuilding.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Building in Elasticsearch
        Building buildingEs = buildingSearchRepository.findOne(testBuilding.getIdFacility());
        assertThat(buildingEs).isEqualToComparingFieldByField(testBuilding);
        keyEntity = testBuilding.getIdFacility();
    }

    @Test
    @Transactional
    public void createBuildingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building with an existing ID
        building.setIdFacility(keyEntity);
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingMockMvc.perform(post("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList
        restBuildingMockMvc.perform(get("/api/buildings?sort=idFacility,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(building.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idFacility").value(building.getIdFacility().toString()))
            .andExpect(jsonPath("$.facilityCode").value(DEFAULT_FACILITY_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        buildingSearchRepository.save(building);
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findOne(building.getIdFacility());
        updatedBuilding
            .facilityCode(UPDATED_FACILITY_CODE)
            .description(UPDATED_DESCRIPTION);
        BuildingDTO buildingDTO = buildingMapper.toDto(updatedBuilding);

        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getFacilityCode()).isEqualTo(UPDATED_FACILITY_CODE);
        assertThat(testBuilding.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Building in Elasticsearch
        Building buildingEs = buildingSearchRepository.findOne(testBuilding.getIdFacility());
        assertThat(buildingEs).isEqualToComparingFieldByField(testBuilding);
    }

    @Test
    @Transactional
    public void updateNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBuildingMockMvc.perform(put("/api/buildings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        buildingSearchRepository.save(building);
        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Get the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getIdFacility())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean buildingExistsInEs = buildingSearchRepository.exists(building.getIdFacility());
        assertThat(buildingExistsInEs).isFalse();

        // Validate the database is empty
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);
        buildingSearchRepository.save(building);

        // Search the building
        restBuildingMockMvc.perform(get("/api/_search/buildings?query=idFacility:" + building.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(building.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Building.class);
        /*
        Building building1 = new Building();
<<<<<<< HEAD
        building1.setIdFacility(1L);
=======
        building1.setId(1L);
>>>>>>> Branch_v4.5.4
        Building building2 = new Building();
        building2.setIdFacility(building1.getId());
        assertThat(building1).isEqualTo(building2);
<<<<<<< HEAD
        building2.setIdFacility(2L);
=======
        building2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(building1).isNotEqualTo(building2);
        building1.setIdFacility(null);
        assertThat(building1).isNotEqualTo(building2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BuildingDTO.class);
        /*
        BuildingDTO buildingDTO1 = new BuildingDTO();
<<<<<<< HEAD
        buildingDTO1.setIdFacility(1L);
=======
        buildingDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        BuildingDTO buildingDTO2 = new BuildingDTO();
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
        buildingDTO2.setIdFacility(buildingDTO1.getIdFacility());
        assertThat(buildingDTO1).isEqualTo(buildingDTO2);
<<<<<<< HEAD
        buildingDTO2.setIdFacility(2L);
=======
        buildingDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
        buildingDTO1.setIdFacility(null);
        assertThat(buildingDTO1).isNotEqualTo(buildingDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(buildingMapper.fromidFacility(42L).getidFacility()).isEqualTo(42);
        //assertThat(buildingMapper.fromIdFacility(null)).isNull();
    }
}
