package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Lecture;
import id.eara.repository.LectureRepository;
import id.eara.service.LectureService;
import id.eara.repository.search.LectureSearchRepository;
import id.eara.service.dto.LectureDTO;
import id.eara.service.mapper.LectureMapper;
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
 * Test class for the LectureResource REST controller.
 *
 * @see LectureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class LectureResourceIntTest {

    private static final String DEFAULT_ID_LECTURE = "AAAAAAAAAA";
    private static final String UPDATED_ID_LECTURE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureMapper lectureMapper;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureSearchRepository lectureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLectureMockMvc;

    private Lecture lecture;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LectureResource lectureResource = new LectureResource(lectureService);
        this.restLectureMockMvc = MockMvcBuilders.standaloneSetup(lectureResource)
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
    public static Lecture createEntity(EntityManager em) {
        Lecture lecture = new Lecture()
            .idLecture(DEFAULT_ID_LECTURE)
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return lecture;
    }

    @Before
    public void initTest() {
        lectureSearchRepository.deleteAll();
        lecture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLecture() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();

        // Create the Lecture
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isCreated());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate + 1);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getIdLecture()).isEqualTo(DEFAULT_ID_LECTURE);
        assertThat(testLecture.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLecture.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Lecture in Elasticsearch
        Lecture lectureEs = lectureSearchRepository.findOne(testLecture.getIdPartyRole());
        assertThat(lectureEs).isEqualToComparingFieldByField(testLecture);
        keyEntity = testLecture.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createLectureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();

        // Create the Lecture with an existing ID
        lecture.setIdPartyRole(keyEntity);
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLectures() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList
        restLectureMockMvc.perform(get("/api/lectures?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(lecture.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idLecture").value(hasItem(DEFAULT_ID_LECTURE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", lecture.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(lecture.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idLecture").value(DEFAULT_ID_LECTURE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingLecture() throws Exception {
        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);
        lectureSearchRepository.save(lecture);
        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Update the lecture
        Lecture updatedLecture = lectureRepository.findOne(lecture.getIdPartyRole());
        updatedLecture
            .idLecture(UPDATED_ID_LECTURE)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        LectureDTO lectureDTO = lectureMapper.toDto(updatedLecture);

        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isOk());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getIdLecture()).isEqualTo(UPDATED_ID_LECTURE);
        assertThat(testLecture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLecture.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Lecture in Elasticsearch
        Lecture lectureEs = lectureSearchRepository.findOne(testLecture.getIdPartyRole());
        assertThat(lectureEs).isEqualToComparingFieldByField(testLecture);
    }

    @Test
    @Transactional
    public void updateNonExistingLecture() throws Exception {
        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Create the Lecture
        LectureDTO lectureDTO = lectureMapper.toDto(lecture);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureDTO)))
            .andExpect(status().isCreated());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);
        lectureSearchRepository.save(lecture);
        int databaseSizeBeforeDelete = lectureRepository.findAll().size();

        // Get the lecture
        restLectureMockMvc.perform(delete("/api/lectures/{id}", lecture.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lectureExistsInEs = lectureSearchRepository.exists(lecture.getIdPartyRole());
        assertThat(lectureExistsInEs).isFalse();

        // Validate the database is empty
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);
        lectureSearchRepository.save(lecture);

        // Search the lecture
        restLectureMockMvc.perform(get("/api/_search/lectures?query=idPartyRole:" + lecture.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(lecture.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idLecture").value(hasItem(DEFAULT_ID_LECTURE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lecture.class);
        /*
        Lecture lecture1 = new Lecture();
<<<<<<< HEAD
        lecture1.setIdPartyRole(1L);
=======
        lecture1.setId(1L);
>>>>>>> Branch_v4.5.4
        Lecture lecture2 = new Lecture();
        lecture2.setIdPartyRole(lecture1.getId());
        assertThat(lecture1).isEqualTo(lecture2);
<<<<<<< HEAD
        lecture2.setIdPartyRole(2L);
=======
        lecture2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(lecture1).isNotEqualTo(lecture2);
        lecture1.setIdPartyRole(null);
        assertThat(lecture1).isNotEqualTo(lecture2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LectureDTO.class);
        /*
        LectureDTO lectureDTO1 = new LectureDTO();
<<<<<<< HEAD
        lectureDTO1.setIdPartyRole(1L);
=======
        lectureDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        LectureDTO lectureDTO2 = new LectureDTO();
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
        lectureDTO2.setIdPartyRole(lectureDTO1.getIdPartyRole());
        assertThat(lectureDTO1).isEqualTo(lectureDTO2);
<<<<<<< HEAD
        lectureDTO2.setIdPartyRole(2L);
=======
        lectureDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
        lectureDTO1.setIdPartyRole(null);
        assertThat(lectureDTO1).isNotEqualTo(lectureDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(lectureMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(lectureMapper.fromIdPartyRole(null)).isNull();
    }
}
