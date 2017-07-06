package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ClassStudy;
import id.eara.repository.ClassStudyRepository;
import id.eara.service.ClassStudyService;
import id.eara.repository.search.ClassStudySearchRepository;
import id.eara.service.dto.ClassStudyDTO;
import id.eara.service.mapper.ClassStudyMapper;
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
 * Test class for the ClassStudyResource REST controller.
 *
 * @see ClassStudyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ClassStudyResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REFKEY = "AAAAAAAAAA";
    private static final String UPDATED_REFKEY = "BBBBBBBBBB";

    @Autowired
    private ClassStudyRepository classStudyRepository;

    @Autowired
    private ClassStudyMapper classStudyMapper;

    @Autowired
    private ClassStudyService classStudyService;

    @Autowired
    private ClassStudySearchRepository classStudySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassStudyMockMvc;

    private ClassStudy classStudy;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassStudyResource classStudyResource = new ClassStudyResource(classStudyService);
        this.restClassStudyMockMvc = MockMvcBuilders.standaloneSetup(classStudyResource)
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
    public static ClassStudy createEntity(EntityManager em) {
        ClassStudy classStudy = new ClassStudy()
            .description(DEFAULT_DESCRIPTION)
            .refkey(DEFAULT_REFKEY);
        return classStudy;
    }

    @Before
    public void initTest() {
        classStudySearchRepository.deleteAll();
        classStudy = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassStudy() throws Exception {
        int databaseSizeBeforeCreate = classStudyRepository.findAll().size();

        // Create the ClassStudy
        ClassStudyDTO classStudyDTO = classStudyMapper.toDto(classStudy);

        restClassStudyMockMvc.perform(post("/api/class-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStudyDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassStudy in the database
        List<ClassStudy> classStudyList = classStudyRepository.findAll();
        assertThat(classStudyList).hasSize(databaseSizeBeforeCreate + 1);
        ClassStudy testClassStudy = classStudyList.get(classStudyList.size() - 1);
        assertThat(testClassStudy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClassStudy.getRefkey()).isEqualTo(DEFAULT_REFKEY);

        // Validate the ClassStudy in Elasticsearch
        ClassStudy classStudyEs = classStudySearchRepository.findOne(testClassStudy.getIdClassStudy());
        assertThat(classStudyEs).isEqualToComparingFieldByField(testClassStudy);
        keyEntity = testClassStudy.getIdClassStudy();
    }

    @Test
    @Transactional
    public void createClassStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classStudyRepository.findAll().size();

        // Create the ClassStudy with an existing ID
        classStudy.setIdClassStudy(keyEntity);
        ClassStudyDTO classStudyDTO = classStudyMapper.toDto(classStudy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassStudyMockMvc.perform(post("/api/class-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStudyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ClassStudy> classStudyList = classStudyRepository.findAll();
        assertThat(classStudyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClassStudies() throws Exception {
        // Initialize the database
        classStudyRepository.saveAndFlush(classStudy);

        // Get all the classStudyList
        restClassStudyMockMvc.perform(get("/api/class-studies?sort=idClassStudy,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idClassStudy").value(hasItem(classStudy.getIdClassStudy().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].refkey").value(hasItem(DEFAULT_REFKEY.toString())));
    }

    @Test
    @Transactional
    public void getClassStudy() throws Exception {
        // Initialize the database
        classStudyRepository.saveAndFlush(classStudy);

        // Get the classStudy
        restClassStudyMockMvc.perform(get("/api/class-studies/{id}", classStudy.getIdClassStudy()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idClassStudy").value(classStudy.getIdClassStudy().toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.refkey").value(DEFAULT_REFKEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassStudy() throws Exception {
        // Get the classStudy
        restClassStudyMockMvc.perform(get("/api/class-studies/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassStudy() throws Exception {
        // Initialize the database
        classStudyRepository.saveAndFlush(classStudy);
        classStudySearchRepository.save(classStudy);
        int databaseSizeBeforeUpdate = classStudyRepository.findAll().size();

        // Update the classStudy
        ClassStudy updatedClassStudy = classStudyRepository.findOne(classStudy.getIdClassStudy());
        updatedClassStudy
            .description(UPDATED_DESCRIPTION)
            .refkey(UPDATED_REFKEY);
        ClassStudyDTO classStudyDTO = classStudyMapper.toDto(updatedClassStudy);

        restClassStudyMockMvc.perform(put("/api/class-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStudyDTO)))
            .andExpect(status().isOk());

        // Validate the ClassStudy in the database
        List<ClassStudy> classStudyList = classStudyRepository.findAll();
        assertThat(classStudyList).hasSize(databaseSizeBeforeUpdate);
        ClassStudy testClassStudy = classStudyList.get(classStudyList.size() - 1);
        assertThat(testClassStudy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClassStudy.getRefkey()).isEqualTo(UPDATED_REFKEY);

        // Validate the ClassStudy in Elasticsearch
        ClassStudy classStudyEs = classStudySearchRepository.findOne(testClassStudy.getIdClassStudy());
        assertThat(classStudyEs).isEqualToComparingFieldByField(testClassStudy);
    }

    @Test
    @Transactional
    public void updateNonExistingClassStudy() throws Exception {
        int databaseSizeBeforeUpdate = classStudyRepository.findAll().size();

        // Create the ClassStudy
        ClassStudyDTO classStudyDTO = classStudyMapper.toDto(classStudy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassStudyMockMvc.perform(put("/api/class-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classStudyDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassStudy in the database
        List<ClassStudy> classStudyList = classStudyRepository.findAll();
        assertThat(classStudyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassStudy() throws Exception {
        // Initialize the database
        classStudyRepository.saveAndFlush(classStudy);
        classStudySearchRepository.save(classStudy);
        int databaseSizeBeforeDelete = classStudyRepository.findAll().size();

        // Get the classStudy
        restClassStudyMockMvc.perform(delete("/api/class-studies/{id}", classStudy.getIdClassStudy())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean classStudyExistsInEs = classStudySearchRepository.exists(classStudy.getIdClassStudy());
        assertThat(classStudyExistsInEs).isFalse();

        // Validate the database is empty
        List<ClassStudy> classStudyList = classStudyRepository.findAll();
        assertThat(classStudyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClassStudy() throws Exception {
        // Initialize the database
        classStudyRepository.saveAndFlush(classStudy);
        classStudySearchRepository.save(classStudy);

        // Search the classStudy
        restClassStudyMockMvc.perform(get("/api/_search/class-studies?query=idClassStudy:" + classStudy.getIdClassStudy()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idClassStudy").value(hasItem(classStudy.getIdClassStudy().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].refkey").value(hasItem(DEFAULT_REFKEY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassStudy.class);
        /*
        ClassStudy classStudy1 = new ClassStudy();
<<<<<<< HEAD
        classStudy1.setIdClassStudy(1L);
=======
        classStudy1.setId(1L);
>>>>>>> Branch_v4.5.4
        ClassStudy classStudy2 = new ClassStudy();
        classStudy2.setIdClassStudy(classStudy1.getId());
        assertThat(classStudy1).isEqualTo(classStudy2);
<<<<<<< HEAD
        classStudy2.setIdClassStudy(2L);
=======
        classStudy2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(classStudy1).isNotEqualTo(classStudy2);
        classStudy1.setIdClassStudy(null);
        assertThat(classStudy1).isNotEqualTo(classStudy2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassStudyDTO.class);
        /*
        ClassStudyDTO classStudyDTO1 = new ClassStudyDTO();
<<<<<<< HEAD
        classStudyDTO1.setIdClassStudy(1L);
=======
        classStudyDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ClassStudyDTO classStudyDTO2 = new ClassStudyDTO();
        assertThat(classStudyDTO1).isNotEqualTo(classStudyDTO2);
        classStudyDTO2.setIdClassStudy(classStudyDTO1.getIdClassStudy());
        assertThat(classStudyDTO1).isEqualTo(classStudyDTO2);
<<<<<<< HEAD
        classStudyDTO2.setIdClassStudy(2L);
=======
        classStudyDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(classStudyDTO1).isNotEqualTo(classStudyDTO2);
        classStudyDTO1.setIdClassStudy(null);
        assertThat(classStudyDTO1).isNotEqualTo(classStudyDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(classStudyMapper.fromidClassStudy(42L).getidClassStudy()).isEqualTo(42);
        //assertThat(classStudyMapper.fromIdClassStudy(null)).isNull();
    }
}
