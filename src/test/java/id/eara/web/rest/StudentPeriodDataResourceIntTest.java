package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudentPeriodData;
import id.eara.repository.StudentPeriodDataRepository;
import id.eara.service.StudentPeriodDataService;
import id.eara.repository.search.StudentPeriodDataSearchRepository;
import id.eara.service.dto.StudentPeriodDataDTO;
import id.eara.service.mapper.StudentPeriodDataMapper;
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
 * Test class for the StudentPeriodDataResource REST controller.
 *
 * @see StudentPeriodDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudentPeriodDataResourceIntTest {

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

    @Autowired
    private StudentPeriodDataRepository studentPeriodDataRepository;

    @Autowired
    private StudentPeriodDataMapper studentPeriodDataMapper;

    @Autowired
    private StudentPeriodDataService studentPeriodDataService;

    @Autowired
    private StudentPeriodDataSearchRepository studentPeriodDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentPeriodDataMockMvc;

    private StudentPeriodData studentPeriodData;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentPeriodDataResource studentPeriodDataResource = new StudentPeriodDataResource(studentPeriodDataService);
        this.restStudentPeriodDataMockMvc = MockMvcBuilders.standaloneSetup(studentPeriodDataResource)
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
    public static StudentPeriodData createEntity(EntityManager em) {
        StudentPeriodData studentPeriodData = new StudentPeriodData()
            .seqnum(DEFAULT_SEQNUM)
            .credit(DEFAULT_CREDIT)
            .totalCredit(DEFAULT_TOTAL_CREDIT)
            .totalPoint(DEFAULT_TOTAL_POINT)
            .currentPoint(DEFAULT_CURRENT_POINT);
        return studentPeriodData;
    }

    @Before
    public void initTest() {
        studentPeriodDataSearchRepository.deleteAll();
        studentPeriodData = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentPeriodData() throws Exception {
        int databaseSizeBeforeCreate = studentPeriodDataRepository.findAll().size();

        // Create the StudentPeriodData
        StudentPeriodDataDTO studentPeriodDataDTO = studentPeriodDataMapper.toDto(studentPeriodData);

        restStudentPeriodDataMockMvc.perform(post("/api/student-period-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodDataDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentPeriodData in the database
        List<StudentPeriodData> studentPeriodDataList = studentPeriodDataRepository.findAll();
        assertThat(studentPeriodDataList).hasSize(databaseSizeBeforeCreate + 1);
        StudentPeriodData testStudentPeriodData = studentPeriodDataList.get(studentPeriodDataList.size() - 1);
        assertThat(testStudentPeriodData.getSeqnum()).isEqualTo(DEFAULT_SEQNUM);
        assertThat(testStudentPeriodData.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testStudentPeriodData.getTotalCredit()).isEqualTo(DEFAULT_TOTAL_CREDIT);
        assertThat(testStudentPeriodData.getTotalPoint()).isEqualTo(DEFAULT_TOTAL_POINT);
        assertThat(testStudentPeriodData.getCurrentPoint()).isEqualTo(DEFAULT_CURRENT_POINT);

        // Validate the StudentPeriodData in Elasticsearch
        StudentPeriodData studentPeriodDataEs = studentPeriodDataSearchRepository.findOne(testStudentPeriodData.getIdStudentPeriod());
        assertThat(studentPeriodDataEs).isEqualToComparingFieldByField(testStudentPeriodData);
        keyEntity = testStudentPeriodData.getIdStudentPeriod();
    }

    @Test
    @Transactional
    public void createStudentPeriodDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentPeriodDataRepository.findAll().size();

        // Create the StudentPeriodData with an existing ID
        studentPeriodData.setIdStudentPeriod(keyEntity);
        StudentPeriodDataDTO studentPeriodDataDTO = studentPeriodDataMapper.toDto(studentPeriodData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentPeriodDataMockMvc.perform(post("/api/student-period-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentPeriodData> studentPeriodDataList = studentPeriodDataRepository.findAll();
        assertThat(studentPeriodDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentPeriodData() throws Exception {
        // Initialize the database
        studentPeriodDataRepository.saveAndFlush(studentPeriodData);

        // Get all the studentPeriodDataList
        restStudentPeriodDataMockMvc.perform(get("/api/student-period-data?sort=idStudentPeriod,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentPeriod").value(hasItem(studentPeriodData.getIdStudentPeriod().toString())))
            .andExpect(jsonPath("$.[*].seqnum").value(hasItem(DEFAULT_SEQNUM)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].totalCredit").value(hasItem(DEFAULT_TOTAL_CREDIT)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT)))
            .andExpect(jsonPath("$.[*].currentPoint").value(hasItem(DEFAULT_CURRENT_POINT)));
    }

    @Test
    @Transactional
    public void getStudentPeriodData() throws Exception {
        // Initialize the database
        studentPeriodDataRepository.saveAndFlush(studentPeriodData);

        // Get the studentPeriodData
        restStudentPeriodDataMockMvc.perform(get("/api/student-period-data/{id}", studentPeriodData.getIdStudentPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudentPeriod").value(studentPeriodData.getIdStudentPeriod().toString()))
            .andExpect(jsonPath("$.seqnum").value(DEFAULT_SEQNUM))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT))
            .andExpect(jsonPath("$.totalCredit").value(DEFAULT_TOTAL_CREDIT))
            .andExpect(jsonPath("$.totalPoint").value(DEFAULT_TOTAL_POINT))
            .andExpect(jsonPath("$.currentPoint").value(DEFAULT_CURRENT_POINT));
    }

    @Test
    @Transactional
    public void getNonExistingStudentPeriodData() throws Exception {
        // Get the studentPeriodData
        restStudentPeriodDataMockMvc.perform(get("/api/student-period-data/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentPeriodData() throws Exception {
        // Initialize the database
        studentPeriodDataRepository.saveAndFlush(studentPeriodData);
        studentPeriodDataSearchRepository.save(studentPeriodData);
        int databaseSizeBeforeUpdate = studentPeriodDataRepository.findAll().size();

        // Update the studentPeriodData
        StudentPeriodData updatedStudentPeriodData = studentPeriodDataRepository.findOne(studentPeriodData.getIdStudentPeriod());
        updatedStudentPeriodData
            .seqnum(UPDATED_SEQNUM)
            .credit(UPDATED_CREDIT)
            .totalCredit(UPDATED_TOTAL_CREDIT)
            .totalPoint(UPDATED_TOTAL_POINT)
            .currentPoint(UPDATED_CURRENT_POINT);
        StudentPeriodDataDTO studentPeriodDataDTO = studentPeriodDataMapper.toDto(updatedStudentPeriodData);

        restStudentPeriodDataMockMvc.perform(put("/api/student-period-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodDataDTO)))
            .andExpect(status().isOk());

        // Validate the StudentPeriodData in the database
        List<StudentPeriodData> studentPeriodDataList = studentPeriodDataRepository.findAll();
        assertThat(studentPeriodDataList).hasSize(databaseSizeBeforeUpdate);
        StudentPeriodData testStudentPeriodData = studentPeriodDataList.get(studentPeriodDataList.size() - 1);
        assertThat(testStudentPeriodData.getSeqnum()).isEqualTo(UPDATED_SEQNUM);
        assertThat(testStudentPeriodData.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testStudentPeriodData.getTotalCredit()).isEqualTo(UPDATED_TOTAL_CREDIT);
        assertThat(testStudentPeriodData.getTotalPoint()).isEqualTo(UPDATED_TOTAL_POINT);
        assertThat(testStudentPeriodData.getCurrentPoint()).isEqualTo(UPDATED_CURRENT_POINT);

        // Validate the StudentPeriodData in Elasticsearch
        StudentPeriodData studentPeriodDataEs = studentPeriodDataSearchRepository.findOne(testStudentPeriodData.getIdStudentPeriod());
        assertThat(studentPeriodDataEs).isEqualToComparingFieldByField(testStudentPeriodData);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentPeriodData() throws Exception {
        int databaseSizeBeforeUpdate = studentPeriodDataRepository.findAll().size();

        // Create the StudentPeriodData
        StudentPeriodDataDTO studentPeriodDataDTO = studentPeriodDataMapper.toDto(studentPeriodData);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentPeriodDataMockMvc.perform(put("/api/student-period-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentPeriodDataDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentPeriodData in the database
        List<StudentPeriodData> studentPeriodDataList = studentPeriodDataRepository.findAll();
        assertThat(studentPeriodDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentPeriodData() throws Exception {
        // Initialize the database
        studentPeriodDataRepository.saveAndFlush(studentPeriodData);
        studentPeriodDataSearchRepository.save(studentPeriodData);
        int databaseSizeBeforeDelete = studentPeriodDataRepository.findAll().size();

        // Get the studentPeriodData
        restStudentPeriodDataMockMvc.perform(delete("/api/student-period-data/{id}", studentPeriodData.getIdStudentPeriod())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentPeriodDataExistsInEs = studentPeriodDataSearchRepository.exists(studentPeriodData.getIdStudentPeriod());
        assertThat(studentPeriodDataExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentPeriodData> studentPeriodDataList = studentPeriodDataRepository.findAll();
        assertThat(studentPeriodDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentPeriodData() throws Exception {
        // Initialize the database
        studentPeriodDataRepository.saveAndFlush(studentPeriodData);
        studentPeriodDataSearchRepository.save(studentPeriodData);

        // Search the studentPeriodData
        restStudentPeriodDataMockMvc.perform(get("/api/_search/student-period-data?query=idStudentPeriod:" + studentPeriodData.getIdStudentPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentPeriod").value(hasItem(studentPeriodData.getIdStudentPeriod().toString())))
            .andExpect(jsonPath("$.[*].seqnum").value(hasItem(DEFAULT_SEQNUM)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].totalCredit").value(hasItem(DEFAULT_TOTAL_CREDIT)))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT)))
            .andExpect(jsonPath("$.[*].currentPoint").value(hasItem(DEFAULT_CURRENT_POINT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPeriodData.class);
        /*
        StudentPeriodData studentPeriodData1 = new StudentPeriodData();
<<<<<<< HEAD
        studentPeriodData1.setIdStudentPeriod(1L);
=======
        studentPeriodData1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentPeriodData studentPeriodData2 = new StudentPeriodData();
        studentPeriodData2.setIdStudentPeriod(studentPeriodData1.getId());
        assertThat(studentPeriodData1).isEqualTo(studentPeriodData2);
<<<<<<< HEAD
        studentPeriodData2.setIdStudentPeriod(2L);
=======
        studentPeriodData2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentPeriodData1).isNotEqualTo(studentPeriodData2);
        studentPeriodData1.setIdStudentPeriod(null);
        assertThat(studentPeriodData1).isNotEqualTo(studentPeriodData2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentPeriodDataDTO.class);
        /*
        StudentPeriodDataDTO studentPeriodDataDTO1 = new StudentPeriodDataDTO();
<<<<<<< HEAD
        studentPeriodDataDTO1.setIdStudentPeriod(1L);
=======
        studentPeriodDataDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentPeriodDataDTO studentPeriodDataDTO2 = new StudentPeriodDataDTO();
        assertThat(studentPeriodDataDTO1).isNotEqualTo(studentPeriodDataDTO2);
        studentPeriodDataDTO2.setIdStudentPeriod(studentPeriodDataDTO1.getIdStudentPeriod());
        assertThat(studentPeriodDataDTO1).isEqualTo(studentPeriodDataDTO2);
<<<<<<< HEAD
        studentPeriodDataDTO2.setIdStudentPeriod(2L);
=======
        studentPeriodDataDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentPeriodDataDTO1).isNotEqualTo(studentPeriodDataDTO2);
        studentPeriodDataDTO1.setIdStudentPeriod(null);
        assertThat(studentPeriodDataDTO1).isNotEqualTo(studentPeriodDataDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studentPeriodDataMapper.fromidStudentPeriod(42L).getidStudentPeriod()).isEqualTo(42);
        //assertThat(studentPeriodDataMapper.fromIdStudentPeriod(null)).isNull();
    }
}
