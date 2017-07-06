package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PreStudent;
import id.eara.repository.PreStudentRepository;
import id.eara.service.PreStudentService;
import id.eara.repository.search.PreStudentSearchRepository;
import id.eara.service.dto.PreStudentDTO;
import id.eara.service.mapper.PreStudentMapper;
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
 * Test class for the PreStudentResource REST controller.
 *
 * @see PreStudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PreStudentResourceIntTest {

    private static final String DEFAULT_ID_PRE_STUDENT = "AAAAAAAAAA";
    private static final String UPDATED_ID_PRE_STUDENT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR_OF = 1;
    private static final Integer UPDATED_YEAR_OF = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private PreStudentRepository preStudentRepository;

    @Autowired
    private PreStudentMapper preStudentMapper;

    @Autowired
    private PreStudentService preStudentService;

    @Autowired
    private PreStudentSearchRepository preStudentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPreStudentMockMvc;

    private PreStudent preStudent;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PreStudentResource preStudentResource = new PreStudentResource(preStudentService);
        this.restPreStudentMockMvc = MockMvcBuilders.standaloneSetup(preStudentResource)
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
    public static PreStudent createEntity(EntityManager em) {
        PreStudent preStudent = new PreStudent()
            .idPreStudent(DEFAULT_ID_PRE_STUDENT)
            .name(DEFAULT_NAME)
            .yearOf(DEFAULT_YEAR_OF)
            .status(DEFAULT_STATUS);
        return preStudent;
    }

    @Before
    public void initTest() {
        preStudentSearchRepository.deleteAll();
        preStudent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreStudent() throws Exception {
        int databaseSizeBeforeCreate = preStudentRepository.findAll().size();

        // Create the PreStudent
        PreStudentDTO preStudentDTO = preStudentMapper.toDto(preStudent);

        restPreStudentMockMvc.perform(post("/api/pre-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preStudentDTO)))
            .andExpect(status().isCreated());

        // Validate the PreStudent in the database
        List<PreStudent> preStudentList = preStudentRepository.findAll();
        assertThat(preStudentList).hasSize(databaseSizeBeforeCreate + 1);
        PreStudent testPreStudent = preStudentList.get(preStudentList.size() - 1);
        assertThat(testPreStudent.getIdPreStudent()).isEqualTo(DEFAULT_ID_PRE_STUDENT);
        assertThat(testPreStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPreStudent.getYearOf()).isEqualTo(DEFAULT_YEAR_OF);
        assertThat(testPreStudent.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the PreStudent in Elasticsearch
        PreStudent preStudentEs = preStudentSearchRepository.findOne(testPreStudent.getIdPartyRole());
        assertThat(preStudentEs).isEqualToComparingFieldByField(testPreStudent);
        keyEntity = testPreStudent.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createPreStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preStudentRepository.findAll().size();

        // Create the PreStudent with an existing ID
        preStudent.setIdPartyRole(keyEntity);
        PreStudentDTO preStudentDTO = preStudentMapper.toDto(preStudent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreStudentMockMvc.perform(post("/api/pre-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preStudentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PreStudent> preStudentList = preStudentRepository.findAll();
        assertThat(preStudentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPreStudents() throws Exception {
        // Initialize the database
        preStudentRepository.saveAndFlush(preStudent);

        // Get all the preStudentList
        restPreStudentMockMvc.perform(get("/api/pre-students?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(preStudent.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idPreStudent").value(hasItem(DEFAULT_ID_PRE_STUDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].yearOf").value(hasItem(DEFAULT_YEAR_OF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getPreStudent() throws Exception {
        // Initialize the database
        preStudentRepository.saveAndFlush(preStudent);

        // Get the preStudent
        restPreStudentMockMvc.perform(get("/api/pre-students/{id}", preStudent.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(preStudent.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idPreStudent").value(DEFAULT_ID_PRE_STUDENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.yearOf").value(DEFAULT_YEAR_OF))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingPreStudent() throws Exception {
        // Get the preStudent
        restPreStudentMockMvc.perform(get("/api/pre-students/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreStudent() throws Exception {
        // Initialize the database
        preStudentRepository.saveAndFlush(preStudent);
        preStudentSearchRepository.save(preStudent);
        int databaseSizeBeforeUpdate = preStudentRepository.findAll().size();

        // Update the preStudent
        PreStudent updatedPreStudent = preStudentRepository.findOne(preStudent.getIdPartyRole());
        updatedPreStudent
            .idPreStudent(UPDATED_ID_PRE_STUDENT)
            .name(UPDATED_NAME)
            .yearOf(UPDATED_YEAR_OF)
            .status(UPDATED_STATUS);
        PreStudentDTO preStudentDTO = preStudentMapper.toDto(updatedPreStudent);

        restPreStudentMockMvc.perform(put("/api/pre-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preStudentDTO)))
            .andExpect(status().isOk());

        // Validate the PreStudent in the database
        List<PreStudent> preStudentList = preStudentRepository.findAll();
        assertThat(preStudentList).hasSize(databaseSizeBeforeUpdate);
        PreStudent testPreStudent = preStudentList.get(preStudentList.size() - 1);
        assertThat(testPreStudent.getIdPreStudent()).isEqualTo(UPDATED_ID_PRE_STUDENT);
        assertThat(testPreStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPreStudent.getYearOf()).isEqualTo(UPDATED_YEAR_OF);
        assertThat(testPreStudent.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the PreStudent in Elasticsearch
        PreStudent preStudentEs = preStudentSearchRepository.findOne(testPreStudent.getIdPartyRole());
        assertThat(preStudentEs).isEqualToComparingFieldByField(testPreStudent);
    }

    @Test
    @Transactional
    public void updateNonExistingPreStudent() throws Exception {
        int databaseSizeBeforeUpdate = preStudentRepository.findAll().size();

        // Create the PreStudent
        PreStudentDTO preStudentDTO = preStudentMapper.toDto(preStudent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPreStudentMockMvc.perform(put("/api/pre-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preStudentDTO)))
            .andExpect(status().isCreated());

        // Validate the PreStudent in the database
        List<PreStudent> preStudentList = preStudentRepository.findAll();
        assertThat(preStudentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePreStudent() throws Exception {
        // Initialize the database
        preStudentRepository.saveAndFlush(preStudent);
        preStudentSearchRepository.save(preStudent);
        int databaseSizeBeforeDelete = preStudentRepository.findAll().size();

        // Get the preStudent
        restPreStudentMockMvc.perform(delete("/api/pre-students/{id}", preStudent.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean preStudentExistsInEs = preStudentSearchRepository.exists(preStudent.getIdPartyRole());
        assertThat(preStudentExistsInEs).isFalse();

        // Validate the database is empty
        List<PreStudent> preStudentList = preStudentRepository.findAll();
        assertThat(preStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPreStudent() throws Exception {
        // Initialize the database
        preStudentRepository.saveAndFlush(preStudent);
        preStudentSearchRepository.save(preStudent);

        // Search the preStudent
        restPreStudentMockMvc.perform(get("/api/_search/pre-students?query=idPartyRole:" + preStudent.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(preStudent.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idPreStudent").value(hasItem(DEFAULT_ID_PRE_STUDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].yearOf").value(hasItem(DEFAULT_YEAR_OF)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreStudent.class);
        /*
        PreStudent preStudent1 = new PreStudent();
<<<<<<< HEAD
        preStudent1.setIdPartyRole(1L);
=======
        preStudent1.setId(1L);
>>>>>>> Branch_v4.5.4
        PreStudent preStudent2 = new PreStudent();
        preStudent2.setIdPartyRole(preStudent1.getId());
        assertThat(preStudent1).isEqualTo(preStudent2);
<<<<<<< HEAD
        preStudent2.setIdPartyRole(2L);
=======
        preStudent2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(preStudent1).isNotEqualTo(preStudent2);
        preStudent1.setIdPartyRole(null);
        assertThat(preStudent1).isNotEqualTo(preStudent2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreStudentDTO.class);
        /*
        PreStudentDTO preStudentDTO1 = new PreStudentDTO();
<<<<<<< HEAD
        preStudentDTO1.setIdPartyRole(1L);
=======
        preStudentDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PreStudentDTO preStudentDTO2 = new PreStudentDTO();
        assertThat(preStudentDTO1).isNotEqualTo(preStudentDTO2);
        preStudentDTO2.setIdPartyRole(preStudentDTO1.getIdPartyRole());
        assertThat(preStudentDTO1).isEqualTo(preStudentDTO2);
<<<<<<< HEAD
        preStudentDTO2.setIdPartyRole(2L);
=======
        preStudentDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(preStudentDTO1).isNotEqualTo(preStudentDTO2);
        preStudentDTO1.setIdPartyRole(null);
        assertThat(preStudentDTO1).isNotEqualTo(preStudentDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(preStudentMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(preStudentMapper.fromIdPartyRole(null)).isNull();
    }
}
