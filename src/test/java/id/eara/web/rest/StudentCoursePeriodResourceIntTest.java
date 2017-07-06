package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudentCoursePeriod;
import id.eara.repository.StudentCoursePeriodRepository;
import id.eara.service.StudentCoursePeriodService;
import id.eara.repository.search.StudentCoursePeriodSearchRepository;
import id.eara.service.dto.StudentCoursePeriodDTO;
import id.eara.service.mapper.StudentCoursePeriodMapper;
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
 * Test class for the StudentCoursePeriodResource REST controller.
 *
 * @see StudentCoursePeriodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudentCoursePeriodResourceIntTest {

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final String DEFAULT_VALUE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_STRING = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private StudentCoursePeriodRepository studentCoursePeriodRepository;

    @Autowired
    private StudentCoursePeriodMapper studentCoursePeriodMapper;

    @Autowired
    private StudentCoursePeriodService studentCoursePeriodService;

    @Autowired
    private StudentCoursePeriodSearchRepository studentCoursePeriodSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentCoursePeriodMockMvc;

    private StudentCoursePeriod studentCoursePeriod;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentCoursePeriodResource studentCoursePeriodResource = new StudentCoursePeriodResource(studentCoursePeriodService);
        this.restStudentCoursePeriodMockMvc = MockMvcBuilders.standaloneSetup(studentCoursePeriodResource)
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
    public static StudentCoursePeriod createEntity(EntityManager em) {
        StudentCoursePeriod studentCoursePeriod = new StudentCoursePeriod()
            .credit(DEFAULT_CREDIT)
            .value(DEFAULT_VALUE)
            .valueString(DEFAULT_VALUE_STRING)
            .status(DEFAULT_STATUS);
        return studentCoursePeriod;
    }

    @Before
    public void initTest() {
        studentCoursePeriodSearchRepository.deleteAll();
        studentCoursePeriod = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentCoursePeriod() throws Exception {
        int databaseSizeBeforeCreate = studentCoursePeriodRepository.findAll().size();

        // Create the StudentCoursePeriod
        StudentCoursePeriodDTO studentCoursePeriodDTO = studentCoursePeriodMapper.toDto(studentCoursePeriod);

        restStudentCoursePeriodMockMvc.perform(post("/api/student-course-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCoursePeriodDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentCoursePeriod in the database
        List<StudentCoursePeriod> studentCoursePeriodList = studentCoursePeriodRepository.findAll();
        assertThat(studentCoursePeriodList).hasSize(databaseSizeBeforeCreate + 1);
        StudentCoursePeriod testStudentCoursePeriod = studentCoursePeriodList.get(studentCoursePeriodList.size() - 1);
        assertThat(testStudentCoursePeriod.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testStudentCoursePeriod.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testStudentCoursePeriod.getValueString()).isEqualTo(DEFAULT_VALUE_STRING);
        assertThat(testStudentCoursePeriod.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the StudentCoursePeriod in Elasticsearch
        StudentCoursePeriod studentCoursePeriodEs = studentCoursePeriodSearchRepository.findOne(testStudentCoursePeriod.getIdStudentCoursePeriod());
        assertThat(studentCoursePeriodEs).isEqualToComparingFieldByField(testStudentCoursePeriod);
        keyEntity = testStudentCoursePeriod.getIdStudentCoursePeriod();
    }

    @Test
    @Transactional
    public void createStudentCoursePeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentCoursePeriodRepository.findAll().size();

        // Create the StudentCoursePeriod with an existing ID
        studentCoursePeriod.setIdStudentCoursePeriod(keyEntity);
        StudentCoursePeriodDTO studentCoursePeriodDTO = studentCoursePeriodMapper.toDto(studentCoursePeriod);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentCoursePeriodMockMvc.perform(post("/api/student-course-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCoursePeriodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentCoursePeriod> studentCoursePeriodList = studentCoursePeriodRepository.findAll();
        assertThat(studentCoursePeriodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentCoursePeriods() throws Exception {
        // Initialize the database
        studentCoursePeriodRepository.saveAndFlush(studentCoursePeriod);

        // Get all the studentCoursePeriodList
        restStudentCoursePeriodMockMvc.perform(get("/api/student-course-periods?sort=idStudentCoursePeriod,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentCoursePeriod").value(hasItem(studentCoursePeriod.getIdStudentCoursePeriod().toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getStudentCoursePeriod() throws Exception {
        // Initialize the database
        studentCoursePeriodRepository.saveAndFlush(studentCoursePeriod);

        // Get the studentCoursePeriod
        restStudentCoursePeriodMockMvc.perform(get("/api/student-course-periods/{id}", studentCoursePeriod.getIdStudentCoursePeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudentCoursePeriod").value(studentCoursePeriod.getIdStudentCoursePeriod().toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.valueString").value(DEFAULT_VALUE_STRING.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingStudentCoursePeriod() throws Exception {
        // Get the studentCoursePeriod
        restStudentCoursePeriodMockMvc.perform(get("/api/student-course-periods/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentCoursePeriod() throws Exception {
        // Initialize the database
        studentCoursePeriodRepository.saveAndFlush(studentCoursePeriod);
        studentCoursePeriodSearchRepository.save(studentCoursePeriod);
        int databaseSizeBeforeUpdate = studentCoursePeriodRepository.findAll().size();

        // Update the studentCoursePeriod
        StudentCoursePeriod updatedStudentCoursePeriod = studentCoursePeriodRepository.findOne(studentCoursePeriod.getIdStudentCoursePeriod());
        updatedStudentCoursePeriod
            .credit(UPDATED_CREDIT)
            .value(UPDATED_VALUE)
            .valueString(UPDATED_VALUE_STRING)
            .status(UPDATED_STATUS);
        StudentCoursePeriodDTO studentCoursePeriodDTO = studentCoursePeriodMapper.toDto(updatedStudentCoursePeriod);

        restStudentCoursePeriodMockMvc.perform(put("/api/student-course-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCoursePeriodDTO)))
            .andExpect(status().isOk());

        // Validate the StudentCoursePeriod in the database
        List<StudentCoursePeriod> studentCoursePeriodList = studentCoursePeriodRepository.findAll();
        assertThat(studentCoursePeriodList).hasSize(databaseSizeBeforeUpdate);
        StudentCoursePeriod testStudentCoursePeriod = studentCoursePeriodList.get(studentCoursePeriodList.size() - 1);
        assertThat(testStudentCoursePeriod.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testStudentCoursePeriod.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testStudentCoursePeriod.getValueString()).isEqualTo(UPDATED_VALUE_STRING);
        assertThat(testStudentCoursePeriod.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the StudentCoursePeriod in Elasticsearch
        StudentCoursePeriod studentCoursePeriodEs = studentCoursePeriodSearchRepository.findOne(testStudentCoursePeriod.getIdStudentCoursePeriod());
        assertThat(studentCoursePeriodEs).isEqualToComparingFieldByField(testStudentCoursePeriod);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentCoursePeriod() throws Exception {
        int databaseSizeBeforeUpdate = studentCoursePeriodRepository.findAll().size();

        // Create the StudentCoursePeriod
        StudentCoursePeriodDTO studentCoursePeriodDTO = studentCoursePeriodMapper.toDto(studentCoursePeriod);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentCoursePeriodMockMvc.perform(put("/api/student-course-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCoursePeriodDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentCoursePeriod in the database
        List<StudentCoursePeriod> studentCoursePeriodList = studentCoursePeriodRepository.findAll();
        assertThat(studentCoursePeriodList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentCoursePeriod() throws Exception {
        // Initialize the database
        studentCoursePeriodRepository.saveAndFlush(studentCoursePeriod);
        studentCoursePeriodSearchRepository.save(studentCoursePeriod);
        int databaseSizeBeforeDelete = studentCoursePeriodRepository.findAll().size();

        // Get the studentCoursePeriod
        restStudentCoursePeriodMockMvc.perform(delete("/api/student-course-periods/{id}", studentCoursePeriod.getIdStudentCoursePeriod())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentCoursePeriodExistsInEs = studentCoursePeriodSearchRepository.exists(studentCoursePeriod.getIdStudentCoursePeriod());
        assertThat(studentCoursePeriodExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentCoursePeriod> studentCoursePeriodList = studentCoursePeriodRepository.findAll();
        assertThat(studentCoursePeriodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentCoursePeriod() throws Exception {
        // Initialize the database
        studentCoursePeriodRepository.saveAndFlush(studentCoursePeriod);
        studentCoursePeriodSearchRepository.save(studentCoursePeriod);

        // Search the studentCoursePeriod
        restStudentCoursePeriodMockMvc.perform(get("/api/_search/student-course-periods?query=idStudentCoursePeriod:" + studentCoursePeriod.getIdStudentCoursePeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentCoursePeriod").value(hasItem(studentCoursePeriod.getIdStudentCoursePeriod().toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentCoursePeriod.class);
        /*
        StudentCoursePeriod studentCoursePeriod1 = new StudentCoursePeriod();
<<<<<<< HEAD
        studentCoursePeriod1.setIdStudentCoursePeriod(1L);
=======
        studentCoursePeriod1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentCoursePeriod studentCoursePeriod2 = new StudentCoursePeriod();
        studentCoursePeriod2.setIdStudentCoursePeriod(studentCoursePeriod1.getId());
        assertThat(studentCoursePeriod1).isEqualTo(studentCoursePeriod2);
<<<<<<< HEAD
        studentCoursePeriod2.setIdStudentCoursePeriod(2L);
=======
        studentCoursePeriod2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentCoursePeriod1).isNotEqualTo(studentCoursePeriod2);
        studentCoursePeriod1.setIdStudentCoursePeriod(null);
        assertThat(studentCoursePeriod1).isNotEqualTo(studentCoursePeriod2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentCoursePeriodDTO.class);
        /*
        StudentCoursePeriodDTO studentCoursePeriodDTO1 = new StudentCoursePeriodDTO();
<<<<<<< HEAD
        studentCoursePeriodDTO1.setIdStudentCoursePeriod(1L);
=======
        studentCoursePeriodDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentCoursePeriodDTO studentCoursePeriodDTO2 = new StudentCoursePeriodDTO();
        assertThat(studentCoursePeriodDTO1).isNotEqualTo(studentCoursePeriodDTO2);
        studentCoursePeriodDTO2.setIdStudentCoursePeriod(studentCoursePeriodDTO1.getIdStudentCoursePeriod());
        assertThat(studentCoursePeriodDTO1).isEqualTo(studentCoursePeriodDTO2);
<<<<<<< HEAD
        studentCoursePeriodDTO2.setIdStudentCoursePeriod(2L);
=======
        studentCoursePeriodDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentCoursePeriodDTO1).isNotEqualTo(studentCoursePeriodDTO2);
        studentCoursePeriodDTO1.setIdStudentCoursePeriod(null);
        assertThat(studentCoursePeriodDTO1).isNotEqualTo(studentCoursePeriodDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studentCoursePeriodMapper.fromidStudentCoursePeriod(42L).getidStudentCoursePeriod()).isEqualTo(42);
        //assertThat(studentCoursePeriodMapper.fromIdStudentCoursePeriod(null)).isNull();
    }
}
