package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Zone;
import id.eara.repository.ZoneRepository;
import id.eara.service.ZoneService;
import id.eara.repository.search.ZoneSearchRepository;
import id.eara.service.dto.ZoneDTO;
import id.eara.service.mapper.ZoneMapper;
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
 * Test class for the ZoneResource REST controller.
 *
 * @see ZoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ZoneResourceIntTest {

    private static final String DEFAULT_GEO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GEO_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneMapper zoneMapper;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ZoneSearchRepository zoneSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restZoneMockMvc;

    private Zone zone;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ZoneResource zoneResource = new ZoneResource(zoneService);
        this.restZoneMockMvc = MockMvcBuilders.standaloneSetup(zoneResource)
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
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone()
            .geoCode(DEFAULT_GEO_CODE)
            .description(DEFAULT_DESCRIPTION);
        return zone;
    }

    @Before
    public void initTest() {
        zoneSearchRepository.deleteAll();
        zone = createEntity(em);
    }

    @Test
    @Transactional
    public void createZone() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate + 1);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getGeoCode()).isEqualTo(DEFAULT_GEO_CODE);
        assertThat(testZone.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Zone in Elasticsearch
        Zone zoneEs = zoneSearchRepository.findOne(testZone.getIdGeoBoundary());
        assertThat(zoneEs).isEqualToComparingFieldByField(testZone);
        keyEntity = testZone.getIdGeoBoundary();
    }

    @Test
    @Transactional
    public void createZoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // Create the Zone with an existing ID
        zone.setIdGeoBoundary(keyEntity);
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllZones() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc.perform(get("/api/zones?sort=idGeoBoundary,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idGeoBoundary").value(hasItem(zone.getIdGeoBoundary().toString())))
            .andExpect(jsonPath("$.[*].geoCode").value(hasItem(DEFAULT_GEO_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", zone.getIdGeoBoundary()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idGeoBoundary").value(zone.getIdGeoBoundary().toString()))
            .andExpect(jsonPath("$.geoCode").value(DEFAULT_GEO_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);
        zoneSearchRepository.save(zone);
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone
        Zone updatedZone = zoneRepository.findOne(zone.getIdGeoBoundary());
        updatedZone
            .geoCode(UPDATED_GEO_CODE)
            .description(UPDATED_DESCRIPTION);
        ZoneDTO zoneDTO = zoneMapper.toDto(updatedZone);

        restZoneMockMvc.perform(put("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getGeoCode()).isEqualTo(UPDATED_GEO_CODE);
        assertThat(testZone.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Zone in Elasticsearch
        Zone zoneEs = zoneSearchRepository.findOne(testZone.getIdGeoBoundary());
        assertThat(zoneEs).isEqualToComparingFieldByField(testZone);
    }

    @Test
    @Transactional
    public void updateNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restZoneMockMvc.perform(put("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);
        zoneSearchRepository.save(zone);
        int databaseSizeBeforeDelete = zoneRepository.findAll().size();

        // Get the zone
        restZoneMockMvc.perform(delete("/api/zones/{id}", zone.getIdGeoBoundary())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean zoneExistsInEs = zoneSearchRepository.exists(zone.getIdGeoBoundary());
        assertThat(zoneExistsInEs).isFalse();

        // Validate the database is empty
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);
        zoneSearchRepository.save(zone);

        // Search the zone
        restZoneMockMvc.perform(get("/api/_search/zones?query=idGeoBoundary:" + zone.getIdGeoBoundary()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idGeoBoundary").value(hasItem(zone.getIdGeoBoundary().toString())))
            .andExpect(jsonPath("$.[*].geoCode").value(hasItem(DEFAULT_GEO_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zone.class);
        /*
        Zone zone1 = new Zone();
<<<<<<< HEAD
        zone1.setIdGeoBoundary(1L);
=======
        zone1.setId(1L);
>>>>>>> Branch_v4.5.4
        Zone zone2 = new Zone();
        zone2.setIdGeoBoundary(zone1.getId());
        assertThat(zone1).isEqualTo(zone2);
<<<<<<< HEAD
        zone2.setIdGeoBoundary(2L);
=======
        zone2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(zone1).isNotEqualTo(zone2);
        zone1.setIdGeoBoundary(null);
        assertThat(zone1).isNotEqualTo(zone2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZoneDTO.class);
        /*
        ZoneDTO zoneDTO1 = new ZoneDTO();
<<<<<<< HEAD
        zoneDTO1.setIdGeoBoundary(1L);
=======
        zoneDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ZoneDTO zoneDTO2 = new ZoneDTO();
        assertThat(zoneDTO1).isNotEqualTo(zoneDTO2);
        zoneDTO2.setIdGeoBoundary(zoneDTO1.getIdGeoBoundary());
        assertThat(zoneDTO1).isEqualTo(zoneDTO2);
<<<<<<< HEAD
        zoneDTO2.setIdGeoBoundary(2L);
=======
        zoneDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(zoneDTO1).isNotEqualTo(zoneDTO2);
        zoneDTO1.setIdGeoBoundary(null);
        assertThat(zoneDTO1).isNotEqualTo(zoneDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(zoneMapper.fromidGeoBoundary(42L).getidGeoBoundary()).isEqualTo(42);
        //assertThat(zoneMapper.fromIdGeoBoundary(null)).isNull();
    }
}
