package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.RegularCourse;
import id.eara.repository.RegularCourseRepository;
import id.eara.service.RegularCourseService;
import id.eara.repository.search.RegularCourseSearchRepository;
import id.eara.service.dto.RegularCourseDTO;
import id.eara.service.mapper.RegularCourseMapper;
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
 * Test class for the RegularCourseResource REST controller.
 *
 * @see RegularCourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class RegularCourseResourceIntTest {

    private static final String DEFAULT_COURSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    @Autowired
    private RegularCourseRepository regularCourseRepository;

    @Autowired
    private RegularCourseMapper regularCourseMapper;

    @Autowired
    private RegularCourseService regularCourseService;

    @Autowired
    private RegularCourseSearchRepository regularCourseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegularCourseMockMvc;

    private RegularCourse regularCourse;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegularCourseResource regularCourseResource = new RegularCourseResource(regularCourseService);
        this.restRegularCourseMockMvc = MockMvcBuilders.standaloneSetup(regularCourseResource)
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
    public static RegularCourse createEntity(EntityManager em) {
        RegularCourse regularCourse = new RegularCourse()
            .courseCode(DEFAULT_COURSE_CODE)
            .description(DEFAULT_DESCRIPTION)
            .credit(DEFAULT_CREDIT);
        return regularCourse;
    }

    @Before
    public void initTest() {
        regularCourseSearchRepository.deleteAll();
        regularCourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegularCourse() throws Exception {
        int databaseSizeBeforeCreate = regularCourseRepository.findAll().size();

        // Create the RegularCourse
        RegularCourseDTO regularCourseDTO = regularCourseMapper.toDto(regularCourse);

        restRegularCourseMockMvc.perform(post("/api/regular-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regularCourseDTO)))
            .andExpect(status().isCreated());

        // Validate the RegularCourse in the database
        List<RegularCourse> regularCourseList = regularCourseRepository.findAll();
        assertThat(regularCourseList).hasSize(databaseSizeBeforeCreate + 1);
        RegularCourse testRegularCourse = regularCourseList.get(regularCourseList.size() - 1);
        assertThat(testRegularCourse.getCourseCode()).isEqualTo(DEFAULT_COURSE_CODE);
        assertThat(testRegularCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRegularCourse.getCredit()).isEqualTo(DEFAULT_CREDIT);

        // Validate the RegularCourse in Elasticsearch
        RegularCourse regularCourseEs = regularCourseSearchRepository.findOne(testRegularCourse.getIdCourse());
        assertThat(regularCourseEs).isEqualToComparingFieldByField(testRegularCourse);
        keyEntity = testRegularCourse.getIdCourse();
    }

    @Test
    @Transactional
    public void createRegularCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regularCourseRepository.findAll().size();

        // Create the RegularCourse with an existing ID
        regularCourse.setIdCourse(keyEntity);
        RegularCourseDTO regularCourseDTO = regularCourseMapper.toDto(regularCourse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegularCourseMockMvc.perform(post("/api/regular-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regularCourseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RegularCourse> regularCourseList = regularCourseRepository.findAll();
        assertThat(regularCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegularCourses() throws Exception {
        // Initialize the database
        regularCourseRepository.saveAndFlush(regularCourse);

        // Get all the regularCourseList
        restRegularCourseMockMvc.perform(get("/api/regular-courses?sort=idCourse,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourse").value(hasItem(regularCourse.getIdCourse().toString())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    public void getRegularCourse() throws Exception {
        // Initialize the database
        regularCourseRepository.saveAndFlush(regularCourse);

        // Get the regularCourse
        restRegularCourseMockMvc.perform(get("/api/regular-courses/{id}", regularCourse.getIdCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idCourse").value(regularCourse.getIdCourse().toString()))
            .andExpect(jsonPath("$.courseCode").value(DEFAULT_COURSE_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT));
    }

    @Test
    @Transactional
    public void getNonExistingRegularCourse() throws Exception {
        // Get the regularCourse
        restRegularCourseMockMvc.perform(get("/api/regular-courses/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegularCourse() throws Exception {
        // Initialize the database
        regularCourseRepository.saveAndFlush(regularCourse);
        regularCourseSearchRepository.save(regularCourse);
        int databaseSizeBeforeUpdate = regularCourseRepository.findAll().size();

        // Update the regularCourse
        RegularCourse updatedRegularCourse = regularCourseRepository.findOne(regularCourse.getIdCourse());
        updatedRegularCourse
            .courseCode(UPDATED_COURSE_CODE)
            .description(UPDATED_DESCRIPTION)
            .credit(UPDATED_CREDIT);
        RegularCourseDTO regularCourseDTO = regularCourseMapper.toDto(updatedRegularCourse);

        restRegularCourseMockMvc.perform(put("/api/regular-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regularCourseDTO)))
            .andExpect(status().isOk());

        // Validate the RegularCourse in the database
        List<RegularCourse> regularCourseList = regularCourseRepository.findAll();
        assertThat(regularCourseList).hasSize(databaseSizeBeforeUpdate);
        RegularCourse testRegularCourse = regularCourseList.get(regularCourseList.size() - 1);
        assertThat(testRegularCourse.getCourseCode()).isEqualTo(UPDATED_COURSE_CODE);
        assertThat(testRegularCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRegularCourse.getCredit()).isEqualTo(UPDATED_CREDIT);

        // Validate the RegularCourse in Elasticsearch
        RegularCourse regularCourseEs = regularCourseSearchRepository.findOne(testRegularCourse.getIdCourse());
        assertThat(regularCourseEs).isEqualToComparingFieldByField(testRegularCourse);
    }

    @Test
    @Transactional
    public void updateNonExistingRegularCourse() throws Exception {
        int databaseSizeBeforeUpdate = regularCourseRepository.findAll().size();

        // Create the RegularCourse
        RegularCourseDTO regularCourseDTO = regularCourseMapper.toDto(regularCourse);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegularCourseMockMvc.perform(put("/api/regular-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regularCourseDTO)))
            .andExpect(status().isCreated());

        // Validate the RegularCourse in the database
        List<RegularCourse> regularCourseList = regularCourseRepository.findAll();
        assertThat(regularCourseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegularCourse() throws Exception {
        // Initialize the database
        regularCourseRepository.saveAndFlush(regularCourse);
        regularCourseSearchRepository.save(regularCourse);
        int databaseSizeBeforeDelete = regularCourseRepository.findAll().size();

        // Get the regularCourse
        restRegularCourseMockMvc.perform(delete("/api/regular-courses/{id}", regularCourse.getIdCourse())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean regularCourseExistsInEs = regularCourseSearchRepository.exists(regularCourse.getIdCourse());
        assertThat(regularCourseExistsInEs).isFalse();

        // Validate the database is empty
        List<RegularCourse> regularCourseList = regularCourseRepository.findAll();
        assertThat(regularCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegularCourse() throws Exception {
        // Initialize the database
        regularCourseRepository.saveAndFlush(regularCourse);
        regularCourseSearchRepository.save(regularCourse);

        // Search the regularCourse
        restRegularCourseMockMvc.perform(get("/api/_search/regular-courses?query=idCourse:" + regularCourse.getIdCourse()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idCourse").value(hasItem(regularCourse.getIdCourse().toString())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegularCourse.class);
        /*
        RegularCourse regularCourse1 = new RegularCourse();
<<<<<<< HEAD
        regularCourse1.setIdCourse(1L);
=======
        regularCourse1.setId(1L);
>>>>>>> Branch_v4.5.4
        RegularCourse regularCourse2 = new RegularCourse();
        regularCourse2.setIdCourse(regularCourse1.getId());
        assertThat(regularCourse1).isEqualTo(regularCourse2);
<<<<<<< HEAD
        regularCourse2.setIdCourse(2L);
=======
        regularCourse2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(regularCourse1).isNotEqualTo(regularCourse2);
        regularCourse1.setIdCourse(null);
        assertThat(regularCourse1).isNotEqualTo(regularCourse2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegularCourseDTO.class);
        /*
        RegularCourseDTO regularCourseDTO1 = new RegularCourseDTO();
<<<<<<< HEAD
        regularCourseDTO1.setIdCourse(1L);
=======
        regularCourseDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        RegularCourseDTO regularCourseDTO2 = new RegularCourseDTO();
        assertThat(regularCourseDTO1).isNotEqualTo(regularCourseDTO2);
        regularCourseDTO2.setIdCourse(regularCourseDTO1.getIdCourse());
        assertThat(regularCourseDTO1).isEqualTo(regularCourseDTO2);
<<<<<<< HEAD
        regularCourseDTO2.setIdCourse(2L);
=======
        regularCourseDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(regularCourseDTO1).isNotEqualTo(regularCourseDTO2);
        regularCourseDTO1.setIdCourse(null);
        assertThat(regularCourseDTO1).isNotEqualTo(regularCourseDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(regularCourseMapper.fromidCourse(42L).getidCourse()).isEqualTo(42);
        //assertThat(regularCourseMapper.fromIdCourse(null)).isNull();
    }
}
