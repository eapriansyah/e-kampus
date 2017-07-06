package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudyPath;
import id.eara.repository.StudyPathRepository;
import id.eara.service.StudyPathService;
import id.eara.repository.search.StudyPathSearchRepository;
import id.eara.service.dto.StudyPathDTO;
import id.eara.service.mapper.StudyPathMapper;
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


import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StudyPathResource REST controller.
 *
 * @see StudyPathResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudyPathResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private StudyPathRepository studyPathRepository;

    @Autowired
    private StudyPathMapper studyPathMapper;

    @Autowired
    private StudyPathService studyPathService;

    @Autowired
    private StudyPathSearchRepository studyPathSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudyPathMockMvc;

    private StudyPath studyPath;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudyPathResource studyPathResource = new StudyPathResource(studyPathService);
        this.restStudyPathMockMvc = MockMvcBuilders.standaloneSetup(studyPathResource)
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
    public static StudyPath createEntity(EntityManager em) {
        StudyPath studyPath = new StudyPath()
            .description(DEFAULT_DESCRIPTION);
        return studyPath;
    }

    @Before
    public void initTest() {
        studyPathSearchRepository.deleteAll();
        studyPath = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudyPath() throws Exception {
        int databaseSizeBeforeCreate = studyPathRepository.findAll().size();

        // Create the StudyPath
        StudyPathDTO studyPathDTO = studyPathMapper.toDto(studyPath);

        restStudyPathMockMvc.perform(post("/api/study-paths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyPathDTO)))
            .andExpect(status().isCreated());

        // Validate the StudyPath in the database
        List<StudyPath> studyPathList = studyPathRepository.findAll();
        assertThat(studyPathList).hasSize(databaseSizeBeforeCreate + 1);
        StudyPath testStudyPath = studyPathList.get(studyPathList.size() - 1);
        assertThat(testStudyPath.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the StudyPath in Elasticsearch
        StudyPath studyPathEs = studyPathSearchRepository.findOne(testStudyPath.getIdStudyPath());
        assertThat(studyPathEs).isEqualToComparingFieldByField(testStudyPath);
    }

    @Test
    @Transactional
    public void createStudyPathWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyPathRepository.findAll().size();

        // Create the StudyPath with an existing ID
        studyPath.setIdStudyPath(1L);
        StudyPathDTO studyPathDTO = studyPathMapper.toDto(studyPath);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyPathMockMvc.perform(post("/api/study-paths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyPathDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudyPath> studyPathList = studyPathRepository.findAll();
        assertThat(studyPathList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudyPaths() throws Exception {
        // Initialize the database
        studyPathRepository.saveAndFlush(studyPath);

        // Get all the studyPathList
        restStudyPathMockMvc.perform(get("/api/study-paths?sort=idStudyPath,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudyPath").value(hasItem(studyPath.getIdStudyPath().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getStudyPath() throws Exception {
        // Initialize the database
        studyPathRepository.saveAndFlush(studyPath);

        // Get the studyPath
        restStudyPathMockMvc.perform(get("/api/study-paths/{id}", studyPath.getIdStudyPath()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudyPath").value(studyPath.getIdStudyPath().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStudyPath() throws Exception {
        // Get the studyPath
        restStudyPathMockMvc.perform(get("/api/study-paths/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudyPath() throws Exception {
        // Initialize the database
        studyPathRepository.saveAndFlush(studyPath);
        studyPathSearchRepository.save(studyPath);
        int databaseSizeBeforeUpdate = studyPathRepository.findAll().size();

        // Update the studyPath
        StudyPath updatedStudyPath = studyPathRepository.findOne(studyPath.getIdStudyPath());
        updatedStudyPath
            .description(UPDATED_DESCRIPTION);
        StudyPathDTO studyPathDTO = studyPathMapper.toDto(updatedStudyPath);

        restStudyPathMockMvc.perform(put("/api/study-paths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyPathDTO)))
            .andExpect(status().isOk());

        // Validate the StudyPath in the database
        List<StudyPath> studyPathList = studyPathRepository.findAll();
        assertThat(studyPathList).hasSize(databaseSizeBeforeUpdate);
        StudyPath testStudyPath = studyPathList.get(studyPathList.size() - 1);
        assertThat(testStudyPath.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the StudyPath in Elasticsearch
        StudyPath studyPathEs = studyPathSearchRepository.findOne(testStudyPath.getIdStudyPath());
        assertThat(studyPathEs).isEqualToComparingFieldByField(testStudyPath);
    }

    @Test
    @Transactional
    public void updateNonExistingStudyPath() throws Exception {
        int databaseSizeBeforeUpdate = studyPathRepository.findAll().size();

        // Create the StudyPath
        StudyPathDTO studyPathDTO = studyPathMapper.toDto(studyPath);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudyPathMockMvc.perform(put("/api/study-paths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studyPathDTO)))
            .andExpect(status().isCreated());

        // Validate the StudyPath in the database
        List<StudyPath> studyPathList = studyPathRepository.findAll();
        assertThat(studyPathList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudyPath() throws Exception {
        // Initialize the database
        studyPathRepository.saveAndFlush(studyPath);
        studyPathSearchRepository.save(studyPath);
        int databaseSizeBeforeDelete = studyPathRepository.findAll().size();

        // Get the studyPath
        restStudyPathMockMvc.perform(delete("/api/study-paths/{id}", studyPath.getIdStudyPath())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studyPathExistsInEs = studyPathSearchRepository.exists(studyPath.getIdStudyPath());
        assertThat(studyPathExistsInEs).isFalse();

        // Validate the database is empty
        List<StudyPath> studyPathList = studyPathRepository.findAll();
        assertThat(studyPathList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudyPath() throws Exception {
        // Initialize the database
        studyPathRepository.saveAndFlush(studyPath);
        studyPathSearchRepository.save(studyPath);

        // Search the studyPath
        restStudyPathMockMvc.perform(get("/api/_search/study-paths?query=idStudyPath:" + studyPath.getIdStudyPath()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudyPath").value(hasItem(studyPath.getIdStudyPath().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyPath.class);
        /*
        StudyPath studyPath1 = new StudyPath();
<<<<<<< HEAD
        studyPath1.setIdStudyPath(1L);
=======
        studyPath1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudyPath studyPath2 = new StudyPath();
        studyPath2.setIdStudyPath(studyPath1.getId());
        assertThat(studyPath1).isEqualTo(studyPath2);
<<<<<<< HEAD
        studyPath2.setIdStudyPath(2L);
=======
        studyPath2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studyPath1).isNotEqualTo(studyPath2);
        studyPath1.setIdStudyPath(null);
        assertThat(studyPath1).isNotEqualTo(studyPath2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyPathDTO.class);
        /*
        StudyPathDTO studyPathDTO1 = new StudyPathDTO();
<<<<<<< HEAD
        studyPathDTO1.setIdStudyPath(1L);
=======
        studyPathDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudyPathDTO studyPathDTO2 = new StudyPathDTO();
        assertThat(studyPathDTO1).isNotEqualTo(studyPathDTO2);
        studyPathDTO2.setIdStudyPath(studyPathDTO1.getIdStudyPath());
        assertThat(studyPathDTO1).isEqualTo(studyPathDTO2);
<<<<<<< HEAD
        studyPathDTO2.setIdStudyPath(2L);
=======
        studyPathDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studyPathDTO1).isNotEqualTo(studyPathDTO2);
        studyPathDTO1.setIdStudyPath(null);
        assertThat(studyPathDTO1).isNotEqualTo(studyPathDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studyPathMapper.fromidStudyPath(42L).getidStudyPath()).isEqualTo(42);
        //assertThat(studyPathMapper.fromIdStudyPath(null)).isNull();
    }
}
