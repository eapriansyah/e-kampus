package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudentCourseScore;
import id.eara.repository.StudentCourseScoreRepository;
import id.eara.service.StudentCourseScoreService;
import id.eara.repository.search.StudentCourseScoreSearchRepository;
import id.eara.service.dto.StudentCourseScoreDTO;
import id.eara.service.mapper.StudentCourseScoreMapper;
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
 * Test class for the StudentCourseScoreResource REST controller.
 *
 * @see StudentCourseScoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudentCourseScoreResourceIntTest {

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final String DEFAULT_VALUE_STRING = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_STRING = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private StudentCourseScoreRepository studentCourseScoreRepository;

    @Autowired
    private StudentCourseScoreMapper studentCourseScoreMapper;

    @Autowired
    private StudentCourseScoreService studentCourseScoreService;

    @Autowired
    private StudentCourseScoreSearchRepository studentCourseScoreSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentCourseScoreMockMvc;

    private StudentCourseScore studentCourseScore;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentCourseScoreResource studentCourseScoreResource = new StudentCourseScoreResource(studentCourseScoreService);
        this.restStudentCourseScoreMockMvc = MockMvcBuilders.standaloneSetup(studentCourseScoreResource)
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
    public static StudentCourseScore createEntity(EntityManager em) {
        StudentCourseScore studentCourseScore = new StudentCourseScore()
            .credit(DEFAULT_CREDIT)
            .value(DEFAULT_VALUE)
            .valueString(DEFAULT_VALUE_STRING)
            .status(DEFAULT_STATUS);
        return studentCourseScore;
    }

    @Before
    public void initTest() {
        studentCourseScoreSearchRepository.deleteAll();
        studentCourseScore = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentCourseScore() throws Exception {
        int databaseSizeBeforeCreate = studentCourseScoreRepository.findAll().size();

        // Create the StudentCourseScore
        StudentCourseScoreDTO studentCourseScoreDTO = studentCourseScoreMapper.toDto(studentCourseScore);

        restStudentCourseScoreMockMvc.perform(post("/api/student-course-scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseScoreDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentCourseScore in the database
        List<StudentCourseScore> studentCourseScoreList = studentCourseScoreRepository.findAll();
        assertThat(studentCourseScoreList).hasSize(databaseSizeBeforeCreate + 1);
        StudentCourseScore testStudentCourseScore = studentCourseScoreList.get(studentCourseScoreList.size() - 1);
        assertThat(testStudentCourseScore.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testStudentCourseScore.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testStudentCourseScore.getValueString()).isEqualTo(DEFAULT_VALUE_STRING);
        assertThat(testStudentCourseScore.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the StudentCourseScore in Elasticsearch
        StudentCourseScore studentCourseScoreEs = studentCourseScoreSearchRepository.findOne(testStudentCourseScore.getIdStudentCourseScore());
        assertThat(studentCourseScoreEs).isEqualToComparingFieldByField(testStudentCourseScore);
        keyEntity = testStudentCourseScore.getIdStudentCourseScore();
    }

    @Test
    @Transactional
    public void createStudentCourseScoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentCourseScoreRepository.findAll().size();

        // Create the StudentCourseScore with an existing ID
        studentCourseScore.setIdStudentCourseScore(keyEntity);
        StudentCourseScoreDTO studentCourseScoreDTO = studentCourseScoreMapper.toDto(studentCourseScore);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentCourseScoreMockMvc.perform(post("/api/student-course-scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseScoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentCourseScore> studentCourseScoreList = studentCourseScoreRepository.findAll();
        assertThat(studentCourseScoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentCourseScores() throws Exception {
        // Initialize the database
        studentCourseScoreRepository.saveAndFlush(studentCourseScore);

        // Get all the studentCourseScoreList
        restStudentCourseScoreMockMvc.perform(get("/api/student-course-scores?sort=idStudentCourseScore,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentCourseScore").value(hasItem(studentCourseScore.getIdStudentCourseScore().toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getStudentCourseScore() throws Exception {
        // Initialize the database
        studentCourseScoreRepository.saveAndFlush(studentCourseScore);

        // Get the studentCourseScore
        restStudentCourseScoreMockMvc.perform(get("/api/student-course-scores/{id}", studentCourseScore.getIdStudentCourseScore()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudentCourseScore").value(studentCourseScore.getIdStudentCourseScore().toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.valueString").value(DEFAULT_VALUE_STRING.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingStudentCourseScore() throws Exception {
        // Get the studentCourseScore
        restStudentCourseScoreMockMvc.perform(get("/api/student-course-scores/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentCourseScore() throws Exception {
        // Initialize the database
        studentCourseScoreRepository.saveAndFlush(studentCourseScore);
        studentCourseScoreSearchRepository.save(studentCourseScore);
        int databaseSizeBeforeUpdate = studentCourseScoreRepository.findAll().size();

        // Update the studentCourseScore
        StudentCourseScore updatedStudentCourseScore = studentCourseScoreRepository.findOne(studentCourseScore.getIdStudentCourseScore());
        updatedStudentCourseScore
            .credit(UPDATED_CREDIT)
            .value(UPDATED_VALUE)
            .valueString(UPDATED_VALUE_STRING)
            .status(UPDATED_STATUS);
        StudentCourseScoreDTO studentCourseScoreDTO = studentCourseScoreMapper.toDto(updatedStudentCourseScore);

        restStudentCourseScoreMockMvc.perform(put("/api/student-course-scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseScoreDTO)))
            .andExpect(status().isOk());

        // Validate the StudentCourseScore in the database
        List<StudentCourseScore> studentCourseScoreList = studentCourseScoreRepository.findAll();
        assertThat(studentCourseScoreList).hasSize(databaseSizeBeforeUpdate);
        StudentCourseScore testStudentCourseScore = studentCourseScoreList.get(studentCourseScoreList.size() - 1);
        assertThat(testStudentCourseScore.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testStudentCourseScore.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testStudentCourseScore.getValueString()).isEqualTo(UPDATED_VALUE_STRING);
        assertThat(testStudentCourseScore.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the StudentCourseScore in Elasticsearch
        StudentCourseScore studentCourseScoreEs = studentCourseScoreSearchRepository.findOne(testStudentCourseScore.getIdStudentCourseScore());
        assertThat(studentCourseScoreEs).isEqualToComparingFieldByField(testStudentCourseScore);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentCourseScore() throws Exception {
        int databaseSizeBeforeUpdate = studentCourseScoreRepository.findAll().size();

        // Create the StudentCourseScore
        StudentCourseScoreDTO studentCourseScoreDTO = studentCourseScoreMapper.toDto(studentCourseScore);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentCourseScoreMockMvc.perform(put("/api/student-course-scores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentCourseScoreDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentCourseScore in the database
        List<StudentCourseScore> studentCourseScoreList = studentCourseScoreRepository.findAll();
        assertThat(studentCourseScoreList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentCourseScore() throws Exception {
        // Initialize the database
        studentCourseScoreRepository.saveAndFlush(studentCourseScore);
        studentCourseScoreSearchRepository.save(studentCourseScore);
        int databaseSizeBeforeDelete = studentCourseScoreRepository.findAll().size();

        // Get the studentCourseScore
        restStudentCourseScoreMockMvc.perform(delete("/api/student-course-scores/{id}", studentCourseScore.getIdStudentCourseScore())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentCourseScoreExistsInEs = studentCourseScoreSearchRepository.exists(studentCourseScore.getIdStudentCourseScore());
        assertThat(studentCourseScoreExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentCourseScore> studentCourseScoreList = studentCourseScoreRepository.findAll();
        assertThat(studentCourseScoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentCourseScore() throws Exception {
        // Initialize the database
        studentCourseScoreRepository.saveAndFlush(studentCourseScore);
        studentCourseScoreSearchRepository.save(studentCourseScore);

        // Search the studentCourseScore
        restStudentCourseScoreMockMvc.perform(get("/api/_search/student-course-scores?query=idStudentCourseScore:" + studentCourseScore.getIdStudentCourseScore()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentCourseScore").value(hasItem(studentCourseScore.getIdStudentCourseScore().toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].valueString").value(hasItem(DEFAULT_VALUE_STRING.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentCourseScore.class);
        /*
        StudentCourseScore studentCourseScore1 = new StudentCourseScore();
<<<<<<< HEAD
        studentCourseScore1.setIdStudentCourseScore(1L);
=======
        studentCourseScore1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentCourseScore studentCourseScore2 = new StudentCourseScore();
        studentCourseScore2.setIdStudentCourseScore(studentCourseScore1.getId());
        assertThat(studentCourseScore1).isEqualTo(studentCourseScore2);
<<<<<<< HEAD
        studentCourseScore2.setIdStudentCourseScore(2L);
=======
        studentCourseScore2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentCourseScore1).isNotEqualTo(studentCourseScore2);
        studentCourseScore1.setIdStudentCourseScore(null);
        assertThat(studentCourseScore1).isNotEqualTo(studentCourseScore2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentCourseScoreDTO.class);
        /*
        StudentCourseScoreDTO studentCourseScoreDTO1 = new StudentCourseScoreDTO();
<<<<<<< HEAD
        studentCourseScoreDTO1.setIdStudentCourseScore(1L);
=======
        studentCourseScoreDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentCourseScoreDTO studentCourseScoreDTO2 = new StudentCourseScoreDTO();
        assertThat(studentCourseScoreDTO1).isNotEqualTo(studentCourseScoreDTO2);
        studentCourseScoreDTO2.setIdStudentCourseScore(studentCourseScoreDTO1.getIdStudentCourseScore());
        assertThat(studentCourseScoreDTO1).isEqualTo(studentCourseScoreDTO2);
<<<<<<< HEAD
        studentCourseScoreDTO2.setIdStudentCourseScore(2L);
=======
        studentCourseScoreDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentCourseScoreDTO1).isNotEqualTo(studentCourseScoreDTO2);
        studentCourseScoreDTO1.setIdStudentCourseScore(null);
        assertThat(studentCourseScoreDTO1).isNotEqualTo(studentCourseScoreDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studentCourseScoreMapper.fromidStudentCourseScore(42L).getidStudentCourseScore()).isEqualTo(42);
        //assertThat(studentCourseScoreMapper.fromIdStudentCourseScore(null)).isNull();
    }
}
