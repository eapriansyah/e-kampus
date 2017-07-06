package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.CourseApplicable;
import id.eara.domain.ProgramStudy;
import id.eara.domain.Course;
import id.eara.domain.PeriodType;
import id.eara.repository.CourseApplicableRepository;
import id.eara.service.CourseApplicableService;
import id.eara.repository.search.CourseApplicableSearchRepository;
import id.eara.service.dto.CourseApplicableDTO;
import id.eara.service.mapper.CourseApplicableMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static id.eara.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseApplicableResource REST controller.
 *
 * @see CourseApplicableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class CourseApplicableResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_THRU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_THRU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CourseApplicableRepository courseApplicableRepository;

    @Autowired
    private CourseApplicableMapper courseApplicableMapper;

    @Autowired
    private CourseApplicableService courseApplicableService;

    @Autowired
    private CourseApplicableSearchRepository courseApplicableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseApplicableMockMvc;

    private CourseApplicable courseApplicable;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseApplicableResource courseApplicableResource = new CourseApplicableResource(courseApplicableService);
        this.restCourseApplicableMockMvc = MockMvcBuilders.standaloneSetup(courseApplicableResource)
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
    public static CourseApplicable createEntity(EntityManager em) {
        CourseApplicable courseApplicable = new CourseApplicable()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateThru(DEFAULT_DATE_THRU);
        // Add required entity
        ProgramStudy prody = ProgramStudyResourceIntTest.createEntity(em);
        em.persist(prody);
        em.flush();
        courseApplicable.setPrody(prody);
        // Add required entity
        Course course = CourseResourceIntTest.createEntity(em);
        em.persist(course);
        em.flush();
        courseApplicable.setCourse(course);
        // Add required entity
        PeriodType periodType = PeriodTypeResourceIntTest.createEntity(em);
        em.persist(periodType);
        em.flush();
        courseApplicable.setPeriodType(periodType);
        return courseApplicable;
    }

    @Before
    public void initTest() {
        courseApplicableSearchRepository.deleteAll();
        courseApplicable = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseApplicable() throws Exception {
        int databaseSizeBeforeCreate = courseApplicableRepository.findAll().size();

        // Create the CourseApplicable
        CourseApplicableDTO courseApplicableDTO = courseApplicableMapper.toDto(courseApplicable);

        restCourseApplicableMockMvc.perform(post("/api/course-applicables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseApplicableDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseApplicable in the database
        List<CourseApplicable> courseApplicableList = courseApplicableRepository.findAll();
        assertThat(courseApplicableList).hasSize(databaseSizeBeforeCreate + 1);
        CourseApplicable testCourseApplicable = courseApplicableList.get(courseApplicableList.size() - 1);
        assertThat(testCourseApplicable.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testCourseApplicable.getDateThru()).isEqualTo(DEFAULT_DATE_THRU);

        // Validate the CourseApplicable in Elasticsearch
        CourseApplicable courseApplicableEs = courseApplicableSearchRepository.findOne(testCourseApplicable.getIdApplCourse());
        assertThat(courseApplicableEs).isEqualToComparingFieldByField(testCourseApplicable);
        keyEntity = testCourseApplicable.getIdApplCourse();
    }

    @Test
    @Transactional
    public void createCourseApplicableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseApplicableRepository.findAll().size();

        // Create the CourseApplicable with an existing ID
        courseApplicable.setIdApplCourse(keyEntity);
        CourseApplicableDTO courseApplicableDTO = courseApplicableMapper.toDto(courseApplicable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseApplicableMockMvc.perform(post("/api/course-applicables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseApplicableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseApplicable> courseApplicableList = courseApplicableRepository.findAll();
        assertThat(courseApplicableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseApplicables() throws Exception {
        // Initialize the database
        courseApplicableRepository.saveAndFlush(courseApplicable);

        // Get all the courseApplicableList
        restCourseApplicableMockMvc.perform(get("/api/course-applicables?sort=idApplCourse,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idApplCourse").value(hasItem(courseApplicable.getIdApplCourse().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void getCourseApplicable() throws Exception {
        // Initialize the database
        courseApplicableRepository.saveAndFlush(courseApplicable);

        // Get the courseApplicable
        restCourseApplicableMockMvc.perform(get("/api/course-applicables/{id}", courseApplicable.getIdApplCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idApplCourse").value(courseApplicable.getIdApplCourse().toString()))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateThru").value(sameInstant(DEFAULT_DATE_THRU)));
    }

    @Test
    @Transactional
    public void getNonExistingCourseApplicable() throws Exception {
        // Get the courseApplicable
        restCourseApplicableMockMvc.perform(get("/api/course-applicables/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseApplicable() throws Exception {
        // Initialize the database
        courseApplicableRepository.saveAndFlush(courseApplicable);
        courseApplicableSearchRepository.save(courseApplicable);
        int databaseSizeBeforeUpdate = courseApplicableRepository.findAll().size();

        // Update the courseApplicable
        CourseApplicable updatedCourseApplicable = courseApplicableRepository.findOne(courseApplicable.getIdApplCourse());
        updatedCourseApplicable
            .dateFrom(UPDATED_DATE_FROM)
            .dateThru(UPDATED_DATE_THRU);
        CourseApplicableDTO courseApplicableDTO = courseApplicableMapper.toDto(updatedCourseApplicable);

        restCourseApplicableMockMvc.perform(put("/api/course-applicables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseApplicableDTO)))
            .andExpect(status().isOk());

        // Validate the CourseApplicable in the database
        List<CourseApplicable> courseApplicableList = courseApplicableRepository.findAll();
        assertThat(courseApplicableList).hasSize(databaseSizeBeforeUpdate);
        CourseApplicable testCourseApplicable = courseApplicableList.get(courseApplicableList.size() - 1);
        assertThat(testCourseApplicable.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testCourseApplicable.getDateThru()).isEqualTo(UPDATED_DATE_THRU);

        // Validate the CourseApplicable in Elasticsearch
        CourseApplicable courseApplicableEs = courseApplicableSearchRepository.findOne(testCourseApplicable.getIdApplCourse());
        assertThat(courseApplicableEs).isEqualToComparingFieldByField(testCourseApplicable);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseApplicable() throws Exception {
        int databaseSizeBeforeUpdate = courseApplicableRepository.findAll().size();

        // Create the CourseApplicable
        CourseApplicableDTO courseApplicableDTO = courseApplicableMapper.toDto(courseApplicable);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseApplicableMockMvc.perform(put("/api/course-applicables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseApplicableDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseApplicable in the database
        List<CourseApplicable> courseApplicableList = courseApplicableRepository.findAll();
        assertThat(courseApplicableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseApplicable() throws Exception {
        // Initialize the database
        courseApplicableRepository.saveAndFlush(courseApplicable);
        courseApplicableSearchRepository.save(courseApplicable);
        int databaseSizeBeforeDelete = courseApplicableRepository.findAll().size();

        // Get the courseApplicable
        restCourseApplicableMockMvc.perform(delete("/api/course-applicables/{id}", courseApplicable.getIdApplCourse())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean courseApplicableExistsInEs = courseApplicableSearchRepository.exists(courseApplicable.getIdApplCourse());
        assertThat(courseApplicableExistsInEs).isFalse();

        // Validate the database is empty
        List<CourseApplicable> courseApplicableList = courseApplicableRepository.findAll();
        assertThat(courseApplicableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCourseApplicable() throws Exception {
        // Initialize the database
        courseApplicableRepository.saveAndFlush(courseApplicable);
        courseApplicableSearchRepository.save(courseApplicable);

        // Search the courseApplicable
        restCourseApplicableMockMvc.perform(get("/api/_search/course-applicables?query=idApplCourse:" + courseApplicable.getIdApplCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idApplCourse").value(hasItem(courseApplicable.getIdApplCourse().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseApplicable.class);
        /*
        CourseApplicable courseApplicable1 = new CourseApplicable();
<<<<<<< HEAD
        courseApplicable1.setIdApplCourse(1L);
=======
        courseApplicable1.setId(1L);
>>>>>>> Branch_v4.5.4
        CourseApplicable courseApplicable2 = new CourseApplicable();
        courseApplicable2.setIdApplCourse(courseApplicable1.getId());
        assertThat(courseApplicable1).isEqualTo(courseApplicable2);
<<<<<<< HEAD
        courseApplicable2.setIdApplCourse(2L);
=======
        courseApplicable2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(courseApplicable1).isNotEqualTo(courseApplicable2);
        courseApplicable1.setIdApplCourse(null);
        assertThat(courseApplicable1).isNotEqualTo(courseApplicable2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseApplicableDTO.class);
        /*
        CourseApplicableDTO courseApplicableDTO1 = new CourseApplicableDTO();
<<<<<<< HEAD
        courseApplicableDTO1.setIdApplCourse(1L);
=======
        courseApplicableDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        CourseApplicableDTO courseApplicableDTO2 = new CourseApplicableDTO();
        assertThat(courseApplicableDTO1).isNotEqualTo(courseApplicableDTO2);
        courseApplicableDTO2.setIdApplCourse(courseApplicableDTO1.getIdApplCourse());
        assertThat(courseApplicableDTO1).isEqualTo(courseApplicableDTO2);
<<<<<<< HEAD
        courseApplicableDTO2.setIdApplCourse(2L);
=======
        courseApplicableDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(courseApplicableDTO1).isNotEqualTo(courseApplicableDTO2);
        courseApplicableDTO1.setIdApplCourse(null);
        assertThat(courseApplicableDTO1).isNotEqualTo(courseApplicableDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(courseApplicableMapper.fromidApplCourse(42L).getidApplCourse()).isEqualTo(42);
        //assertThat(courseApplicableMapper.fromIdApplCourse(null)).isNull();
    }
}
