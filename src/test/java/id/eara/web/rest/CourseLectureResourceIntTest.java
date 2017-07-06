package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.CourseLecture;
import id.eara.domain.Lecture;
import id.eara.domain.Course;
import id.eara.repository.CourseLectureRepository;
import id.eara.service.CourseLectureService;
import id.eara.repository.search.CourseLectureSearchRepository;
import id.eara.service.dto.CourseLectureDTO;
import id.eara.service.mapper.CourseLectureMapper;
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
 * Test class for the CourseLectureResource REST controller.
 *
 * @see CourseLectureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class CourseLectureResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_THRU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_THRU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CourseLectureRepository courseLectureRepository;

    @Autowired
    private CourseLectureMapper courseLectureMapper;

    @Autowired
    private CourseLectureService courseLectureService;

    @Autowired
    private CourseLectureSearchRepository courseLectureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseLectureMockMvc;

    private CourseLecture courseLecture;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseLectureResource courseLectureResource = new CourseLectureResource(courseLectureService);
        this.restCourseLectureMockMvc = MockMvcBuilders.standaloneSetup(courseLectureResource)
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
    public static CourseLecture createEntity(EntityManager em) {
        CourseLecture courseLecture = new CourseLecture()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateThru(DEFAULT_DATE_THRU);
        // Add required entity
        Lecture lecture = LectureResourceIntTest.createEntity(em);
        em.persist(lecture);
        em.flush();
        courseLecture.setLecture(lecture);
        // Add required entity
        Course course = CourseResourceIntTest.createEntity(em);
        em.persist(course);
        em.flush();
        courseLecture.setCourse(course);
        return courseLecture;
    }

    @Before
    public void initTest() {
        courseLectureSearchRepository.deleteAll();
        courseLecture = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseLecture() throws Exception {
        int databaseSizeBeforeCreate = courseLectureRepository.findAll().size();

        // Create the CourseLecture
        CourseLectureDTO courseLectureDTO = courseLectureMapper.toDto(courseLecture);

        restCourseLectureMockMvc.perform(post("/api/course-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseLectureDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseLecture in the database
        List<CourseLecture> courseLectureList = courseLectureRepository.findAll();
        assertThat(courseLectureList).hasSize(databaseSizeBeforeCreate + 1);
        CourseLecture testCourseLecture = courseLectureList.get(courseLectureList.size() - 1);
        assertThat(testCourseLecture.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testCourseLecture.getDateThru()).isEqualTo(DEFAULT_DATE_THRU);

        // Validate the CourseLecture in Elasticsearch
        CourseLecture courseLectureEs = courseLectureSearchRepository.findOne(testCourseLecture.getIdCourseLecture());
        assertThat(courseLectureEs).isEqualToComparingFieldByField(testCourseLecture);
        keyEntity = testCourseLecture.getIdCourseLecture();
    }

    @Test
    @Transactional
    public void createCourseLectureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseLectureRepository.findAll().size();

        // Create the CourseLecture with an existing ID
        courseLecture.setIdCourseLecture(keyEntity);
        CourseLectureDTO courseLectureDTO = courseLectureMapper.toDto(courseLecture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseLectureMockMvc.perform(post("/api/course-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseLectureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CourseLecture> courseLectureList = courseLectureRepository.findAll();
        assertThat(courseLectureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourseLectures() throws Exception {
        // Initialize the database
        courseLectureRepository.saveAndFlush(courseLecture);

        // Get all the courseLectureList
        restCourseLectureMockMvc.perform(get("/api/course-lectures?sort=idCourseLecture,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourseLecture").value(hasItem(courseLecture.getIdCourseLecture().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void getCourseLecture() throws Exception {
        // Initialize the database
        courseLectureRepository.saveAndFlush(courseLecture);

        // Get the courseLecture
        restCourseLectureMockMvc.perform(get("/api/course-lectures/{id}", courseLecture.getIdCourseLecture()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idCourseLecture").value(courseLecture.getIdCourseLecture().toString()))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateThru").value(sameInstant(DEFAULT_DATE_THRU)));
    }

    @Test
    @Transactional
    public void getNonExistingCourseLecture() throws Exception {
        // Get the courseLecture
        restCourseLectureMockMvc.perform(get("/api/course-lectures/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseLecture() throws Exception {
        // Initialize the database
        courseLectureRepository.saveAndFlush(courseLecture);
        courseLectureSearchRepository.save(courseLecture);
        int databaseSizeBeforeUpdate = courseLectureRepository.findAll().size();

        // Update the courseLecture
        CourseLecture updatedCourseLecture = courseLectureRepository.findOne(courseLecture.getIdCourseLecture());
        updatedCourseLecture
            .dateFrom(UPDATED_DATE_FROM)
            .dateThru(UPDATED_DATE_THRU);
        CourseLectureDTO courseLectureDTO = courseLectureMapper.toDto(updatedCourseLecture);

        restCourseLectureMockMvc.perform(put("/api/course-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseLectureDTO)))
            .andExpect(status().isOk());

        // Validate the CourseLecture in the database
        List<CourseLecture> courseLectureList = courseLectureRepository.findAll();
        assertThat(courseLectureList).hasSize(databaseSizeBeforeUpdate);
        CourseLecture testCourseLecture = courseLectureList.get(courseLectureList.size() - 1);
        assertThat(testCourseLecture.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testCourseLecture.getDateThru()).isEqualTo(UPDATED_DATE_THRU);

        // Validate the CourseLecture in Elasticsearch
        CourseLecture courseLectureEs = courseLectureSearchRepository.findOne(testCourseLecture.getIdCourseLecture());
        assertThat(courseLectureEs).isEqualToComparingFieldByField(testCourseLecture);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseLecture() throws Exception {
        int databaseSizeBeforeUpdate = courseLectureRepository.findAll().size();

        // Create the CourseLecture
        CourseLectureDTO courseLectureDTO = courseLectureMapper.toDto(courseLecture);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourseLectureMockMvc.perform(put("/api/course-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseLectureDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseLecture in the database
        List<CourseLecture> courseLectureList = courseLectureRepository.findAll();
        assertThat(courseLectureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourseLecture() throws Exception {
        // Initialize the database
        courseLectureRepository.saveAndFlush(courseLecture);
        courseLectureSearchRepository.save(courseLecture);
        int databaseSizeBeforeDelete = courseLectureRepository.findAll().size();

        // Get the courseLecture
        restCourseLectureMockMvc.perform(delete("/api/course-lectures/{id}", courseLecture.getIdCourseLecture())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean courseLectureExistsInEs = courseLectureSearchRepository.exists(courseLecture.getIdCourseLecture());
        assertThat(courseLectureExistsInEs).isFalse();

        // Validate the database is empty
        List<CourseLecture> courseLectureList = courseLectureRepository.findAll();
        assertThat(courseLectureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCourseLecture() throws Exception {
        // Initialize the database
        courseLectureRepository.saveAndFlush(courseLecture);
        courseLectureSearchRepository.save(courseLecture);

        // Search the courseLecture
        restCourseLectureMockMvc.perform(get("/api/_search/course-lectures?query=idCourseLecture:" + courseLecture.getIdCourseLecture()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourseLecture").value(hasItem(courseLecture.getIdCourseLecture().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLecture.class);
        /*
        CourseLecture courseLecture1 = new CourseLecture();
<<<<<<< HEAD
        courseLecture1.setIdCourseLecture(1L);
=======
        courseLecture1.setId(1L);
>>>>>>> Branch_v4.5.4
        CourseLecture courseLecture2 = new CourseLecture();
        courseLecture2.setIdCourseLecture(courseLecture1.getId());
        assertThat(courseLecture1).isEqualTo(courseLecture2);
<<<<<<< HEAD
        courseLecture2.setIdCourseLecture(2L);
=======
        courseLecture2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(courseLecture1).isNotEqualTo(courseLecture2);
        courseLecture1.setIdCourseLecture(null);
        assertThat(courseLecture1).isNotEqualTo(courseLecture2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLectureDTO.class);
        /*
        CourseLectureDTO courseLectureDTO1 = new CourseLectureDTO();
<<<<<<< HEAD
        courseLectureDTO1.setIdCourseLecture(1L);
=======
        courseLectureDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        CourseLectureDTO courseLectureDTO2 = new CourseLectureDTO();
        assertThat(courseLectureDTO1).isNotEqualTo(courseLectureDTO2);
        courseLectureDTO2.setIdCourseLecture(courseLectureDTO1.getIdCourseLecture());
        assertThat(courseLectureDTO1).isEqualTo(courseLectureDTO2);
<<<<<<< HEAD
        courseLectureDTO2.setIdCourseLecture(2L);
=======
        courseLectureDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(courseLectureDTO1).isNotEqualTo(courseLectureDTO2);
        courseLectureDTO1.setIdCourseLecture(null);
        assertThat(courseLectureDTO1).isNotEqualTo(courseLectureDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(courseLectureMapper.fromidCourseLecture(42L).getidCourseLecture()).isEqualTo(42);
        //assertThat(courseLectureMapper.fromIdCourseLecture(null)).isNull();
    }
}
