package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Faculty;
import id.eara.domain.University;
import id.eara.repository.FacultyRepository;
import id.eara.service.FacultyService;
import id.eara.repository.search.FacultySearchRepository;
import id.eara.service.dto.FacultyDTO;
import id.eara.service.mapper.FacultyMapper;
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
 * Test class for the FacultyResource REST controller.
 *
 * @see FacultyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class FacultyResourceIntTest {

    private static final String DEFAULT_ID_INTERNAL = "AAAAAAAAAA";
    private static final String UPDATED_ID_INTERNAL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultySearchRepository facultySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFacultyMockMvc;

    private Faculty faculty;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FacultyResource facultyResource = new FacultyResource(facultyService);
        this.restFacultyMockMvc = MockMvcBuilders.standaloneSetup(facultyResource)
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
    public static Faculty createEntity(EntityManager em) {
        Faculty faculty = new Faculty()
            .idInternal(DEFAULT_ID_INTERNAL)
            .name(DEFAULT_NAME);
        // Add required entity
        University university = UniversityResourceIntTest.createEntity(em);
        em.persist(university);
        em.flush();
        faculty.setUniversity(university);
        return faculty;
    }

    @Before
    public void initTest() {
        facultySearchRepository.deleteAll();
        faculty = createEntity(em);
    }

    @Test
    @Transactional
    public void createFaculty() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();

        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isCreated());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate + 1);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getIdInternal()).isEqualTo(DEFAULT_ID_INTERNAL);
        assertThat(testFaculty.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Faculty in Elasticsearch
        Faculty facultyEs = facultySearchRepository.findOne(testFaculty.getIdPartyRole());
        assertThat(facultyEs).isEqualToComparingFieldByField(testFaculty);
        keyEntity = testFaculty.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createFacultyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facultyRepository.findAll().size();

        // Create the Faculty with an existing ID
        faculty.setIdPartyRole(keyEntity);
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacultyMockMvc.perform(post("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFaculties() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get all the facultyList
        restFacultyMockMvc.perform(get("/api/faculties?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(faculty.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);

        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", faculty.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(faculty.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idInternal").value(DEFAULT_ID_INTERNAL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFaculty() throws Exception {
        // Get the faculty
        restFacultyMockMvc.perform(get("/api/faculties/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        facultySearchRepository.save(faculty);
        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Update the faculty
        Faculty updatedFaculty = facultyRepository.findOne(faculty.getIdPartyRole());
        updatedFaculty
            .idInternal(UPDATED_ID_INTERNAL)
            .name(UPDATED_NAME);
        FacultyDTO facultyDTO = facultyMapper.toDto(updatedFaculty);

        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isOk());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate);
        Faculty testFaculty = facultyList.get(facultyList.size() - 1);
        assertThat(testFaculty.getIdInternal()).isEqualTo(UPDATED_ID_INTERNAL);
        assertThat(testFaculty.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Faculty in Elasticsearch
        Faculty facultyEs = facultySearchRepository.findOne(testFaculty.getIdPartyRole());
        assertThat(facultyEs).isEqualToComparingFieldByField(testFaculty);
    }

    @Test
    @Transactional
    public void updateNonExistingFaculty() throws Exception {
        int databaseSizeBeforeUpdate = facultyRepository.findAll().size();

        // Create the Faculty
        FacultyDTO facultyDTO = facultyMapper.toDto(faculty);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFacultyMockMvc.perform(put("/api/faculties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facultyDTO)))
            .andExpect(status().isCreated());

        // Validate the Faculty in the database
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        facultySearchRepository.save(faculty);
        int databaseSizeBeforeDelete = facultyRepository.findAll().size();

        // Get the faculty
        restFacultyMockMvc.perform(delete("/api/faculties/{id}", faculty.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean facultyExistsInEs = facultySearchRepository.exists(faculty.getIdPartyRole());
        assertThat(facultyExistsInEs).isFalse();

        // Validate the database is empty
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFaculty() throws Exception {
        // Initialize the database
        facultyRepository.saveAndFlush(faculty);
        facultySearchRepository.save(faculty);

        // Search the faculty
        restFacultyMockMvc.perform(get("/api/_search/faculties?query=idPartyRole:" + faculty.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(faculty.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Faculty.class);
        /*
        Faculty faculty1 = new Faculty();
<<<<<<< HEAD
        faculty1.setIdPartyRole(1L);
=======
        faculty1.setId(1L);
>>>>>>> Branch_v4.5.4
        Faculty faculty2 = new Faculty();
        faculty2.setIdPartyRole(faculty1.getId());
        assertThat(faculty1).isEqualTo(faculty2);
<<<<<<< HEAD
        faculty2.setIdPartyRole(2L);
=======
        faculty2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(faculty1).isNotEqualTo(faculty2);
        faculty1.setIdPartyRole(null);
        assertThat(faculty1).isNotEqualTo(faculty2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacultyDTO.class);
        /*
        FacultyDTO facultyDTO1 = new FacultyDTO();
<<<<<<< HEAD
        facultyDTO1.setIdPartyRole(1L);
=======
        facultyDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        FacultyDTO facultyDTO2 = new FacultyDTO();
        assertThat(facultyDTO1).isNotEqualTo(facultyDTO2);
        facultyDTO2.setIdPartyRole(facultyDTO1.getIdPartyRole());
        assertThat(facultyDTO1).isEqualTo(facultyDTO2);
<<<<<<< HEAD
        facultyDTO2.setIdPartyRole(2L);
=======
        facultyDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(facultyDTO1).isNotEqualTo(facultyDTO2);
        facultyDTO1.setIdPartyRole(null);
        assertThat(facultyDTO1).isNotEqualTo(facultyDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(facultyMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(facultyMapper.fromIdPartyRole(null)).isNull();
    }
}
