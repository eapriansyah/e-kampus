package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ExtraCourse;
import id.eara.repository.ExtraCourseRepository;
import id.eara.service.ExtraCourseService;
import id.eara.repository.search.ExtraCourseSearchRepository;
import id.eara.service.dto.ExtraCourseDTO;
import id.eara.service.mapper.ExtraCourseMapper;
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
 * Test class for the ExtraCourseResource REST controller.
 *
 * @see ExtraCourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ExtraCourseResourceIntTest {

    private static final String DEFAULT_COURSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    @Autowired
    private ExtraCourseRepository extraCourseRepository;

    @Autowired
    private ExtraCourseMapper extraCourseMapper;

    @Autowired
    private ExtraCourseService extraCourseService;

    @Autowired
    private ExtraCourseSearchRepository extraCourseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExtraCourseMockMvc;

    private ExtraCourse extraCourse;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtraCourseResource extraCourseResource = new ExtraCourseResource(extraCourseService);
        this.restExtraCourseMockMvc = MockMvcBuilders.standaloneSetup(extraCourseResource)
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
    public static ExtraCourse createEntity(EntityManager em) {
        ExtraCourse extraCourse = new ExtraCourse()
            .courseCode(DEFAULT_COURSE_CODE)
            .description(DEFAULT_DESCRIPTION)
            .credit(DEFAULT_CREDIT);
        return extraCourse;
    }

    @Before
    public void initTest() {
        extraCourseSearchRepository.deleteAll();
        extraCourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraCourse() throws Exception {
        int databaseSizeBeforeCreate = extraCourseRepository.findAll().size();

        // Create the ExtraCourse
        ExtraCourseDTO extraCourseDTO = extraCourseMapper.toDto(extraCourse);

        restExtraCourseMockMvc.perform(post("/api/extra-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraCourseDTO)))
            .andExpect(status().isCreated());

        // Validate the ExtraCourse in the database
        List<ExtraCourse> extraCourseList = extraCourseRepository.findAll();
        assertThat(extraCourseList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraCourse testExtraCourse = extraCourseList.get(extraCourseList.size() - 1);
        assertThat(testExtraCourse.getCourseCode()).isEqualTo(DEFAULT_COURSE_CODE);
        assertThat(testExtraCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExtraCourse.getCredit()).isEqualTo(DEFAULT_CREDIT);

        // Validate the ExtraCourse in Elasticsearch
        ExtraCourse extraCourseEs = extraCourseSearchRepository.findOne(testExtraCourse.getIdCourse());
        assertThat(extraCourseEs).isEqualToComparingFieldByField(testExtraCourse);
        keyEntity = testExtraCourse.getIdCourse();
    }

    @Test
    @Transactional
    public void createExtraCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraCourseRepository.findAll().size();

        // Create the ExtraCourse with an existing ID
        extraCourse.setIdCourse(keyEntity);
        ExtraCourseDTO extraCourseDTO = extraCourseMapper.toDto(extraCourse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraCourseMockMvc.perform(post("/api/extra-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraCourseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExtraCourse> extraCourseList = extraCourseRepository.findAll();
        assertThat(extraCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExtraCourses() throws Exception {
        // Initialize the database
        extraCourseRepository.saveAndFlush(extraCourse);

        // Get all the extraCourseList
        restExtraCourseMockMvc.perform(get("/api/extra-courses?sort=idCourse,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourse").value(hasItem(extraCourse.getIdCourse().toString())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    public void getExtraCourse() throws Exception {
        // Initialize the database
        extraCourseRepository.saveAndFlush(extraCourse);

        // Get the extraCourse
        restExtraCourseMockMvc.perform(get("/api/extra-courses/{id}", extraCourse.getIdCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idCourse").value(extraCourse.getIdCourse().toString()))
            .andExpect(jsonPath("$.courseCode").value(DEFAULT_COURSE_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT));
    }

    @Test
    @Transactional
    public void getNonExistingExtraCourse() throws Exception {
        // Get the extraCourse
        restExtraCourseMockMvc.perform(get("/api/extra-courses/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraCourse() throws Exception {
        // Initialize the database
        extraCourseRepository.saveAndFlush(extraCourse);
        extraCourseSearchRepository.save(extraCourse);
        int databaseSizeBeforeUpdate = extraCourseRepository.findAll().size();

        // Update the extraCourse
        ExtraCourse updatedExtraCourse = extraCourseRepository.findOne(extraCourse.getIdCourse());
        updatedExtraCourse
            .courseCode(UPDATED_COURSE_CODE)
            .description(UPDATED_DESCRIPTION)
            .credit(UPDATED_CREDIT);
        ExtraCourseDTO extraCourseDTO = extraCourseMapper.toDto(updatedExtraCourse);

        restExtraCourseMockMvc.perform(put("/api/extra-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraCourseDTO)))
            .andExpect(status().isOk());

        // Validate the ExtraCourse in the database
        List<ExtraCourse> extraCourseList = extraCourseRepository.findAll();
        assertThat(extraCourseList).hasSize(databaseSizeBeforeUpdate);
        ExtraCourse testExtraCourse = extraCourseList.get(extraCourseList.size() - 1);
        assertThat(testExtraCourse.getCourseCode()).isEqualTo(UPDATED_COURSE_CODE);
        assertThat(testExtraCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExtraCourse.getCredit()).isEqualTo(UPDATED_CREDIT);

        // Validate the ExtraCourse in Elasticsearch
        ExtraCourse extraCourseEs = extraCourseSearchRepository.findOne(testExtraCourse.getIdCourse());
        assertThat(extraCourseEs).isEqualToComparingFieldByField(testExtraCourse);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraCourse() throws Exception {
        int databaseSizeBeforeUpdate = extraCourseRepository.findAll().size();

        // Create the ExtraCourse
        ExtraCourseDTO extraCourseDTO = extraCourseMapper.toDto(extraCourse);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtraCourseMockMvc.perform(put("/api/extra-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraCourseDTO)))
            .andExpect(status().isCreated());

        // Validate the ExtraCourse in the database
        List<ExtraCourse> extraCourseList = extraCourseRepository.findAll();
        assertThat(extraCourseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtraCourse() throws Exception {
        // Initialize the database
        extraCourseRepository.saveAndFlush(extraCourse);
        extraCourseSearchRepository.save(extraCourse);
        int databaseSizeBeforeDelete = extraCourseRepository.findAll().size();

        // Get the extraCourse
        restExtraCourseMockMvc.perform(delete("/api/extra-courses/{id}", extraCourse.getIdCourse())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean extraCourseExistsInEs = extraCourseSearchRepository.exists(extraCourse.getIdCourse());
        assertThat(extraCourseExistsInEs).isFalse();

        // Validate the database is empty
        List<ExtraCourse> extraCourseList = extraCourseRepository.findAll();
        assertThat(extraCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExtraCourse() throws Exception {
        // Initialize the database
        extraCourseRepository.saveAndFlush(extraCourse);
        extraCourseSearchRepository.save(extraCourse);

        // Search the extraCourse
        restExtraCourseMockMvc.perform(get("/api/_search/extra-courses?query=idCourse:" + extraCourse.getIdCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourse").value(hasItem(extraCourse.getIdCourse().toString())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtraCourse.class);
        /*
        ExtraCourse extraCourse1 = new ExtraCourse();
<<<<<<< HEAD
        extraCourse1.setIdCourse(1L);
=======
        extraCourse1.setId(1L);
>>>>>>> Branch_v4.5.4
        ExtraCourse extraCourse2 = new ExtraCourse();
        extraCourse2.setIdCourse(extraCourse1.getId());
        assertThat(extraCourse1).isEqualTo(extraCourse2);
<<<<<<< HEAD
        extraCourse2.setIdCourse(2L);
=======
        extraCourse2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(extraCourse1).isNotEqualTo(extraCourse2);
        extraCourse1.setIdCourse(null);
        assertThat(extraCourse1).isNotEqualTo(extraCourse2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtraCourseDTO.class);
        /*
        ExtraCourseDTO extraCourseDTO1 = new ExtraCourseDTO();
<<<<<<< HEAD
        extraCourseDTO1.setIdCourse(1L);
=======
        extraCourseDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ExtraCourseDTO extraCourseDTO2 = new ExtraCourseDTO();
        assertThat(extraCourseDTO1).isNotEqualTo(extraCourseDTO2);
        extraCourseDTO2.setIdCourse(extraCourseDTO1.getIdCourse());
        assertThat(extraCourseDTO1).isEqualTo(extraCourseDTO2);
<<<<<<< HEAD
        extraCourseDTO2.setIdCourse(2L);
=======
        extraCourseDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(extraCourseDTO1).isNotEqualTo(extraCourseDTO2);
        extraCourseDTO1.setIdCourse(null);
        assertThat(extraCourseDTO1).isNotEqualTo(extraCourseDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(extraCourseMapper.fromidCourse(42L).getidCourse()).isEqualTo(42);
        //assertThat(extraCourseMapper.fromIdCourse(null)).isNull();
    }
}
