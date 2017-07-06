package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.University;
import id.eara.repository.UniversityRepository;
import id.eara.service.UniversityService;
import id.eara.repository.search.UniversitySearchRepository;
import id.eara.service.dto.UniversityDTO;
import id.eara.service.mapper.UniversityMapper;
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
 * Test class for the UniversityResource REST controller.
 *
 * @see UniversityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class UniversityResourceIntTest {

    private static final String DEFAULT_ID_INTERNAL = "AAAAAAAAAA";
    private static final String UPDATED_ID_INTERNAL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UniversityMapper universityMapper;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UniversitySearchRepository universitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUniversityMockMvc;

    private University university;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UniversityResource universityResource = new UniversityResource(universityService);
        this.restUniversityMockMvc = MockMvcBuilders.standaloneSetup(universityResource)
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
    public static University createEntity(EntityManager em) {
        University university = new University()
            .idInternal(DEFAULT_ID_INTERNAL)
            .name(DEFAULT_NAME);
        return university;
    }

    @Before
    public void initTest() {
        universitySearchRepository.deleteAll();
        university = createEntity(em);
    }

    @Test
    @Transactional
    public void createUniversity() throws Exception {
        int databaseSizeBeforeCreate = universityRepository.findAll().size();

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isCreated());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate + 1);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getIdInternal()).isEqualTo(DEFAULT_ID_INTERNAL);
        assertThat(testUniversity.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the University in Elasticsearch
        University universityEs = universitySearchRepository.findOne(testUniversity.getIdPartyRole());
        assertThat(universityEs).isEqualToComparingFieldByField(testUniversity);
        keyEntity = testUniversity.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createUniversityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = universityRepository.findAll().size();

        // Create the University with an existing ID
        university.setIdPartyRole(keyEntity);
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversityMockMvc.perform(post("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUniversities() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get all the universityList
        restUniversityMockMvc.perform(get("/api/universities?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(university.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);

        // Get the university
        restUniversityMockMvc.perform(get("/api/universities/{id}", university.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(university.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idInternal").value(DEFAULT_ID_INTERNAL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUniversity() throws Exception {
        // Get the university
        restUniversityMockMvc.perform(get("/api/universities/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);
        universitySearchRepository.save(university);
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Update the university
        University updatedUniversity = universityRepository.findOne(university.getIdPartyRole());
        updatedUniversity
            .idInternal(UPDATED_ID_INTERNAL)
            .name(UPDATED_NAME);
        UniversityDTO universityDTO = universityMapper.toDto(updatedUniversity);

        restUniversityMockMvc.perform(put("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isOk());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate);
        University testUniversity = universityList.get(universityList.size() - 1);
        assertThat(testUniversity.getIdInternal()).isEqualTo(UPDATED_ID_INTERNAL);
        assertThat(testUniversity.getName()).isEqualTo(UPDATED_NAME);

        // Validate the University in Elasticsearch
        University universityEs = universitySearchRepository.findOne(testUniversity.getIdPartyRole());
        assertThat(universityEs).isEqualToComparingFieldByField(testUniversity);
    }

    @Test
    @Transactional
    public void updateNonExistingUniversity() throws Exception {
        int databaseSizeBeforeUpdate = universityRepository.findAll().size();

        // Create the University
        UniversityDTO universityDTO = universityMapper.toDto(university);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUniversityMockMvc.perform(put("/api/universities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(universityDTO)))
            .andExpect(status().isCreated());

        // Validate the University in the database
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);
        universitySearchRepository.save(university);
        int databaseSizeBeforeDelete = universityRepository.findAll().size();

        // Get the university
        restUniversityMockMvc.perform(delete("/api/universities/{id}", university.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean universityExistsInEs = universitySearchRepository.exists(university.getIdPartyRole());
        assertThat(universityExistsInEs).isFalse();

        // Validate the database is empty
        List<University> universityList = universityRepository.findAll();
        assertThat(universityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUniversity() throws Exception {
        // Initialize the database
        universityRepository.saveAndFlush(university);
        universitySearchRepository.save(university);

        // Search the university
        restUniversityMockMvc.perform(get("/api/_search/universities?query=idPartyRole:" + university.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(university.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(University.class);
        /*
        University university1 = new University();
<<<<<<< HEAD
        university1.setIdPartyRole(1L);
=======
        university1.setId(1L);
>>>>>>> Branch_v4.5.4
        University university2 = new University();
        university2.setIdPartyRole(university1.getId());
        assertThat(university1).isEqualTo(university2);
<<<<<<< HEAD
        university2.setIdPartyRole(2L);
=======
        university2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(university1).isNotEqualTo(university2);
        university1.setIdPartyRole(null);
        assertThat(university1).isNotEqualTo(university2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversityDTO.class);
        /*
        UniversityDTO universityDTO1 = new UniversityDTO();
<<<<<<< HEAD
        universityDTO1.setIdPartyRole(1L);
=======
        universityDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        UniversityDTO universityDTO2 = new UniversityDTO();
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO2.setIdPartyRole(universityDTO1.getIdPartyRole());
        assertThat(universityDTO1).isEqualTo(universityDTO2);
<<<<<<< HEAD
        universityDTO2.setIdPartyRole(2L);
=======
        universityDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO1.setIdPartyRole(null);
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(universityMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(universityMapper.fromIdPartyRole(null)).isNull();
    }
}
