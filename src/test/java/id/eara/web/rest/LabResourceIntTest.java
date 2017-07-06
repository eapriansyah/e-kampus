package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Lab;
import id.eara.repository.LabRepository;
import id.eara.service.LabService;
import id.eara.repository.search.LabSearchRepository;
import id.eara.service.dto.LabDTO;
import id.eara.service.mapper.LabMapper;
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
 * Test class for the LabResource REST controller.
 *
 * @see LabResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class LabResourceIntTest {

    private static final String DEFAULT_FACILITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FACILITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private LabService labService;

    @Autowired
    private LabSearchRepository labSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLabMockMvc;

    private Lab lab;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LabResource labResource = new LabResource(labService);
        this.restLabMockMvc = MockMvcBuilders.standaloneSetup(labResource)
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
    public static Lab createEntity(EntityManager em) {
        Lab lab = new Lab()
            .facilityCode(DEFAULT_FACILITY_CODE)
            .description(DEFAULT_DESCRIPTION);
        return lab;
    }

    @Before
    public void initTest() {
        labSearchRepository.deleteAll();
        lab = createEntity(em);
    }

    @Test
    @Transactional
    public void createLab() throws Exception {
        int databaseSizeBeforeCreate = labRepository.findAll().size();

        // Create the Lab
        LabDTO labDTO = labMapper.toDto(lab);

        restLabMockMvc.perform(post("/api/labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(labDTO)))
            .andExpect(status().isCreated());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeCreate + 1);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getFacilityCode()).isEqualTo(DEFAULT_FACILITY_CODE);
        assertThat(testLab.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Lab in Elasticsearch
        Lab labEs = labSearchRepository.findOne(testLab.getIdFacility());
        assertThat(labEs).isEqualToComparingFieldByField(testLab);
        keyEntity = testLab.getIdFacility();
    }

    @Test
    @Transactional
    public void createLabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = labRepository.findAll().size();

        // Create the Lab with an existing ID
        lab.setIdFacility(keyEntity);
        LabDTO labDTO = labMapper.toDto(lab);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabMockMvc.perform(post("/api/labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(labDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLabs() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        // Get all the labList
        restLabMockMvc.perform(get("/api/labs?sort=idFacility,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(lab.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);

        // Get the lab
        restLabMockMvc.perform(get("/api/labs/{id}", lab.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idFacility").value(lab.getIdFacility().toString()))
            .andExpect(jsonPath("$.facilityCode").value(DEFAULT_FACILITY_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLab() throws Exception {
        // Get the lab
        restLabMockMvc.perform(get("/api/labs/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);
        labSearchRepository.save(lab);
        int databaseSizeBeforeUpdate = labRepository.findAll().size();

        // Update the lab
        Lab updatedLab = labRepository.findOne(lab.getIdFacility());
        updatedLab
            .facilityCode(UPDATED_FACILITY_CODE)
            .description(UPDATED_DESCRIPTION);
        LabDTO labDTO = labMapper.toDto(updatedLab);

        restLabMockMvc.perform(put("/api/labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(labDTO)))
            .andExpect(status().isOk());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate);
        Lab testLab = labList.get(labList.size() - 1);
        assertThat(testLab.getFacilityCode()).isEqualTo(UPDATED_FACILITY_CODE);
        assertThat(testLab.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Lab in Elasticsearch
        Lab labEs = labSearchRepository.findOne(testLab.getIdFacility());
        assertThat(labEs).isEqualToComparingFieldByField(testLab);
    }

    @Test
    @Transactional
    public void updateNonExistingLab() throws Exception {
        int databaseSizeBeforeUpdate = labRepository.findAll().size();

        // Create the Lab
        LabDTO labDTO = labMapper.toDto(lab);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLabMockMvc.perform(put("/api/labs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(labDTO)))
            .andExpect(status().isCreated());

        // Validate the Lab in the database
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);
        labSearchRepository.save(lab);
        int databaseSizeBeforeDelete = labRepository.findAll().size();

        // Get the lab
        restLabMockMvc.perform(delete("/api/labs/{id}", lab.getIdFacility())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean labExistsInEs = labSearchRepository.exists(lab.getIdFacility());
        assertThat(labExistsInEs).isFalse();

        // Validate the database is empty
        List<Lab> labList = labRepository.findAll();
        assertThat(labList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLab() throws Exception {
        // Initialize the database
        labRepository.saveAndFlush(lab);
        labSearchRepository.save(lab);

        // Search the lab
        restLabMockMvc.perform(get("/api/_search/labs?query=idFacility:" + lab.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(lab.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lab.class);
        /*
        Lab lab1 = new Lab();
<<<<<<< HEAD
        lab1.setIdFacility(1L);
=======
        lab1.setId(1L);
>>>>>>> Branch_v4.5.4
        Lab lab2 = new Lab();
        lab2.setIdFacility(lab1.getId());
        assertThat(lab1).isEqualTo(lab2);
<<<<<<< HEAD
        lab2.setIdFacility(2L);
=======
        lab2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(lab1).isNotEqualTo(lab2);
        lab1.setIdFacility(null);
        assertThat(lab1).isNotEqualTo(lab2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabDTO.class);
        /*
        LabDTO labDTO1 = new LabDTO();
<<<<<<< HEAD
        labDTO1.setIdFacility(1L);
=======
        labDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        LabDTO labDTO2 = new LabDTO();
        assertThat(labDTO1).isNotEqualTo(labDTO2);
        labDTO2.setIdFacility(labDTO1.getIdFacility());
        assertThat(labDTO1).isEqualTo(labDTO2);
<<<<<<< HEAD
        labDTO2.setIdFacility(2L);
=======
        labDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(labDTO1).isNotEqualTo(labDTO2);
        labDTO1.setIdFacility(null);
        assertThat(labDTO1).isNotEqualTo(labDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(labMapper.fromidFacility(42L).getidFacility()).isEqualTo(42);
        //assertThat(labMapper.fromIdFacility(null)).isNull();
    }
}
