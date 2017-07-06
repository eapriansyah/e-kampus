package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudentPeriods;
import id.eara.repository.StudentPeriodsRepository;
import id.eara.service.StudentPeriodsService;
import id.eara.repository.search.StudentPeriodsSearchRepository;
import id.eara.service.dto.StudentPeriodsDTO;
import id.eara.service.mapper.StudentPeriodsMapper;
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
 * Test class for the StudentPeriodsResource REST controller.
 *
 * @see StudentPeriodsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudentPeriodsResourceIntTest {

    private static final Integer DEFAULT_SEQNUM = 1;
    private static final Integer UPDATED_SEQNUM = 2;

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    private static final Integer DEFAULT_TOTAL_CREDIT = 1;
    private static final Integer UPDATED_TOTAL_CREDIT = 2;

    private static final Integer DEFAULT_TOTAL_POINT = 1;
    private static final Integer UPDATED_TOTAL_POINT = 2;

    private static final Integer DEFAULT_CURRENT_POINT = 1;
    private static final Integer UPDATED_CURRENT_POINT = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private StudentPeriodsRepository studentPeriodsRepository;

    @Autowired
    private StudentPeriodsMapper studentPeriodsMapper;

    @Autowired
    private StudentPeriodsService studentPeriodsService;

    @Autowired
    private StudentPeriodsSearchRepository studentPeriodsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentPeriodsMockMvc;

    private StudentPeriods studentPeriods;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentPeriodsResource studentPeriodsResource = new StudentPeriodsResource(studentPeriodsService);
        this.restStudentPeriodsMockMvc = MockMvcBuilders.standaloneSetup(studentPeriodsResource)
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
    public static StudentPeriods createEntity(EntityManager em) {
        StudentPeriods studentPeriods = new StudentPeriods()
            .seqnum(DEFAULT_SEQNUM)
            .credit(DEFAULT_CREDIT)
            .totalCredit(DEFAULT_TOTAL_CREDIT)
            .totalPoint(DEFAULT_TOTAL_POINT)
            .currentPoint(DEFAULT_CURRENT_POINT)
            .status(DEFAULT_STATUS);
        return studentPeriods;
    }

    @Before
    public void initTest() {
        studentPeriodsSearchRepository.deleteAll();
        studentPeriods = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentPeriods() throws Exception {
        int databaseSizeBeforeCreate = studentPeriodsRepository.findAll().size();

        // Create the StudentPeriods
        StudentPeriodsDTO studentPeriodsDTO = studentPeriodsMapper.toDto(studentPeriods);

        restStudentPeriodsMockMvc.perform(post("/api/student-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodsDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentPeriods in the database
        List<StudentPeriods> studentPeriodsList = studentPeriodsRepository.findAll();
        assertThat(studentPeriodsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentPeriods testStudentPeriods = studentPeriodsList.get(studentPeriodsList.size() - 1);
        assertThat(testStudentPeriods.getSeqnum()).isEqualTo(DEFAULT_SEQNUM);
        assertThat(testStudentPeriods.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testStudentPeriods.getTotalCredit()).isEqualTo(DEFAULT_TOTAL_CREDIT);
        assertThat(testStudentPeriods.getTotalPoint()).isEqualTo(DEFAULT_TOTAL_POINT);
        assertThat(testStudentPeriods.getCurrentPoint()).isEqualTo(DEFAULT_CURRENT_POINT);
        assertThat(testStudentPeriods.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the StudentPeriods in Elasticsearch
        StudentPeriods studentPeriodsEs = studentPeriodsSearchRepository.findOne(testStudentPeriods.getIdStudentPeriod());
        assertThat(studentPeriodsEs).isEqualToComparingFieldByField(testStudentPeriods);
        keyEntity = testStudentPeriods.getIdStudentPeriod();
    }

    @Test
    @Transactional
    public void createStudentPeriodsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentPeriodsRepository.findAll().size();

        // Create the StudentPeriods with an existing ID
        studentPeriods.setIdStudentPeriod(keyEntity);
        StudentPeriodsDTO studentPeriodsDTO = studentPeriodsMapper.toDto(studentPeriods);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentPeriodsMockMvc.perform(post("/api/student-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentPeriods> studentPeriodsList = studentPeriodsRepository.findAll();
        assertThat(studentPeriodsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentPeriods() throws Exception {
        // Initialize the database
        studentPeriodsRepository.saveAndFlush(studentPeriods);

        // Get all the studentPeriodsList
        restStudentPeriodsMockMvc.perform(get("/api/student-periods?sort=idStudentPeriod,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentPeriod").value(hasItem(studentPeriods.getIdStudentPeriod().toString())))
            .andExpect(jsonPath("$.[*].seqnum").value(hasItem(DEFAULT_SEQNUM)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].totalCredit").value(hasItem(DEFAULT_TOTAL_CREDIT)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT)))
            .andExpect(jsonPath("$.[*].currentPoint").value(hasItem(DEFAULT_CURRENT_POINT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getStudentPeriods() throws Exception {
        // Initialize the database
        studentPeriodsRepository.saveAndFlush(studentPeriods);

        // Get the studentPeriods
        restStudentPeriodsMockMvc.perform(get("/api/student-periods/{id}", studentPeriods.getIdStudentPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudentPeriod").value(studentPeriods.getIdStudentPeriod().toString()))
            .andExpect(jsonPath("$.seqnum").value(DEFAULT_SEQNUM))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT))
            .andExpect(jsonPath("$.totalCredit").value(DEFAULT_TOTAL_CREDIT))
            .andExpect(jsonPath("$.totalPoint").value(DEFAULT_TOTAL_POINT))
            .andExpect(jsonPath("$.currentPoint").value(DEFAULT_CURRENT_POINT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingStudentPeriods() throws Exception {
        // Get the studentPeriods
        restStudentPeriodsMockMvc.perform(get("/api/student-periods/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentPeriods() throws Exception {
        // Initialize the database
        studentPeriodsRepository.saveAndFlush(studentPeriods);
        studentPeriodsSearchRepository.save(studentPeriods);
        int databaseSizeBeforeUpdate = studentPeriodsRepository.findAll().size();

        // Update the studentPeriods
        StudentPeriods updatedStudentPeriods = studentPeriodsRepository.findOne(studentPeriods.getIdStudentPeriod());
        updatedStudentPeriods
            .seqnum(UPDATED_SEQNUM)
            .credit(UPDATED_CREDIT)
            .totalCredit(UPDATED_TOTAL_CREDIT)
            .totalPoint(UPDATED_TOTAL_POINT)
            .currentPoint(UPDATED_CURRENT_POINT)
            .status(UPDATED_STATUS);
        StudentPeriodsDTO studentPeriodsDTO = studentPeriodsMapper.toDto(updatedStudentPeriods);

        restStudentPeriodsMockMvc.perform(put("/api/student-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodsDTO)))
            .andExpect(status().isOk());

        // Validate the StudentPeriods in the database
        List<StudentPeriods> studentPeriodsList = studentPeriodsRepository.findAll();
        assertThat(studentPeriodsList).hasSize(databaseSizeBeforeUpdate);
        StudentPeriods testStudentPeriods = studentPeriodsList.get(studentPeriodsList.size() - 1);
        assertThat(testStudentPeriods.getSeqnum()).isEqualTo(UPDATED_SEQNUM);
        assertThat(testStudentPeriods.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testStudentPeriods.getTotalCredit()).isEqualTo(UPDATED_TOTAL_CREDIT);
        assertThat(testStudentPeriods.getTotalPoint()).isEqualTo(UPDATED_TOTAL_POINT);
        assertThat(testStudentPeriods.getCurrentPoint()).isEqualTo(UPDATED_CURRENT_POINT);
        assertThat(testStudentPeriods.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the StudentPeriods in Elasticsearch
        StudentPeriods studentPeriodsEs = studentPeriodsSearchRepository.findOne(testStudentPeriods.getIdStudentPeriod());
        assertThat(studentPeriodsEs).isEqualToComparingFieldByField(testStudentPeriods);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentPeriods() throws Exception {
        int databaseSizeBeforeUpdate = studentPeriodsRepository.findAll().size();

        // Create the StudentPeriods
        StudentPeriodsDTO studentPeriodsDTO = studentPeriodsMapper.toDto(studentPeriods);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentPeriodsMockMvc.perform(put("/api/student-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodsDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentPeriods in the database
        List<StudentPeriods> studentPeriodsList = studentPeriodsRepository.findAll();
        assertThat(studentPeriodsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentPeriods() throws Exception {
        // Initialize the database
        studentPeriodsRepository.saveAndFlush(studentPeriods);
        studentPeriodsSearchRepository.save(studentPeriods);
        int databaseSizeBeforeDelete = studentPeriodsRepository.findAll().size();

        // Get the studentPeriods
        restStudentPeriodsMockMvc.perform(delete("/api/student-periods/{id}", studentPeriods.getIdStudentPeriod())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentPeriodsExistsInEs = studentPeriodsSearchRepository.exists(studentPeriods.getIdStudentPeriod());
        assertThat(studentPeriodsExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentPeriods> studentPeriodsList = studentPeriodsRepository.findAll();
        assertThat(studentPeriodsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentPeriods() throws Exception {
        // Initialize the database
        studentPeriodsRepository.saveAndFlush(studentPeriods);
        studentPeriodsSearchRepository.save(studentPeriods);

        // Search the studentPeriods
        restStudentPeriodsMockMvc.perform(get("/api/_search/student-periods?query=idStudentPeriod:" + studentPeriods.getIdStudentPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentPeriod").value(hasItem(studentPeriods.getIdStudentPeriod().toString())))
            .andExpect(jsonPath("$.[*].seqnum").value(hasItem(DEFAULT_SEQNUM)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].totalCredit").value(hasItem(DEFAULT_TOTAL_CREDIT)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT)))
            .andExpect(jsonPath("$.[*].currentPoint").value(hasItem(DEFAULT_CURRENT_POINT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPeriods.class);
        /*
        StudentPeriods studentPeriods1 = new StudentPeriods();
<<<<<<< HEAD
        studentPeriods1.setIdStudentPeriod(1L);
=======
        studentPeriods1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentPeriods studentPeriods2 = new StudentPeriods();
        studentPeriods2.setIdStudentPeriod(studentPeriods1.getId());
        assertThat(studentPeriods1).isEqualTo(studentPeriods2);
<<<<<<< HEAD
        studentPeriods2.setIdStudentPeriod(2L);
=======
        studentPeriods2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentPeriods1).isNotEqualTo(studentPeriods2);
        studentPeriods1.setIdStudentPeriod(null);
        assertThat(studentPeriods1).isNotEqualTo(studentPeriods2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPeriodsDTO.class);
        /*
        StudentPeriodsDTO studentPeriodsDTO1 = new StudentPeriodsDTO();
<<<<<<< HEAD
        studentPeriodsDTO1.setIdStudentPeriod(1L);
=======
        studentPeriodsDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentPeriodsDTO studentPeriodsDTO2 = new StudentPeriodsDTO();
        assertThat(studentPeriodsDTO1).isNotEqualTo(studentPeriodsDTO2);
        studentPeriodsDTO2.setIdStudentPeriod(studentPeriodsDTO1.getIdStudentPeriod());
        assertThat(studentPeriodsDTO1).isEqualTo(studentPeriodsDTO2);
<<<<<<< HEAD
        studentPeriodsDTO2.setIdStudentPeriod(2L);
=======
        studentPeriodsDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentPeriodsDTO1).isNotEqualTo(studentPeriodsDTO2);
        studentPeriodsDTO1.setIdStudentPeriod(null);
        assertThat(studentPeriodsDTO1).isNotEqualTo(studentPeriodsDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studentPeriodsMapper.fromidStudentPeriod(42L).getidStudentPeriod()).isEqualTo(42);
        //assertThat(studentPeriodsMapper.fromIdStudentPeriod(null)).isNull();
    }
}
