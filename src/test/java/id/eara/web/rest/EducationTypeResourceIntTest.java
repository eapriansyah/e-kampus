package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.EducationType;
import id.eara.repository.EducationTypeRepository;
import id.eara.service.EducationTypeService;
import id.eara.repository.search.EducationTypeSearchRepository;
import id.eara.service.dto.EducationTypeDTO;
import id.eara.service.mapper.EducationTypeMapper;
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
 * Test class for the EducationTypeResource REST controller.
 *
 * @see EducationTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class EducationTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private EducationTypeRepository educationTypeRepository;

    @Autowired
    private EducationTypeMapper educationTypeMapper;

    @Autowired
    private EducationTypeService educationTypeService;

    @Autowired
    private EducationTypeSearchRepository educationTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEducationTypeMockMvc;

    private EducationType educationType;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EducationTypeResource educationTypeResource = new EducationTypeResource(educationTypeService);
        this.restEducationTypeMockMvc = MockMvcBuilders.standaloneSetup(educationTypeResource)
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
    public static EducationType createEntity(EntityManager em) {
        EducationType educationType = new EducationType()
            .description(DEFAULT_DESCRIPTION);
        return educationType;
    }

    @Before
    public void initTest() {
        educationTypeSearchRepository.deleteAll();
        educationType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEducationType() throws Exception {
        int databaseSizeBeforeCreate = educationTypeRepository.findAll().size();

        // Create the EducationType
        EducationTypeDTO educationTypeDTO = educationTypeMapper.toDto(educationType);

        restEducationTypeMockMvc.perform(post("/api/education-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EducationType in the database
        List<EducationType> educationTypeList = educationTypeRepository.findAll();
        assertThat(educationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EducationType testEducationType = educationTypeList.get(educationTypeList.size() - 1);
        assertThat(testEducationType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the EducationType in Elasticsearch
        EducationType educationTypeEs = educationTypeSearchRepository.findOne(testEducationType.getIdEducationType());
        assertThat(educationTypeEs).isEqualToComparingFieldByField(testEducationType);
    }

    @Test
    @Transactional
    public void createEducationTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = educationTypeRepository.findAll().size();

        // Create the EducationType with an existing ID
        educationType.setIdEducationType(1L);
        EducationTypeDTO educationTypeDTO = educationTypeMapper.toDto(educationType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationTypeMockMvc.perform(post("/api/education-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EducationType> educationTypeList = educationTypeRepository.findAll();
        assertThat(educationTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEducationTypes() throws Exception {
        // Initialize the database
        educationTypeRepository.saveAndFlush(educationType);

        // Get all the educationTypeList
        restEducationTypeMockMvc.perform(get("/api/education-types?sort=idEducationType,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEducationType").value(hasItem(educationType.getIdEducationType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEducationType() throws Exception {
        // Initialize the database
        educationTypeRepository.saveAndFlush(educationType);

        // Get the educationType
        restEducationTypeMockMvc.perform(get("/api/education-types/{id}", educationType.getIdEducationType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idEducationType").value(educationType.getIdEducationType().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEducationType() throws Exception {
        // Get the educationType
        restEducationTypeMockMvc.perform(get("/api/education-types/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEducationType() throws Exception {
        // Initialize the database
        educationTypeRepository.saveAndFlush(educationType);
        educationTypeSearchRepository.save(educationType);
        int databaseSizeBeforeUpdate = educationTypeRepository.findAll().size();

        // Update the educationType
        EducationType updatedEducationType = educationTypeRepository.findOne(educationType.getIdEducationType());
        updatedEducationType
            .description(UPDATED_DESCRIPTION);
        EducationTypeDTO educationTypeDTO = educationTypeMapper.toDto(updatedEducationType);

        restEducationTypeMockMvc.perform(put("/api/education-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationTypeDTO)))
            .andExpect(status().isOk());

        // Validate the EducationType in the database
        List<EducationType> educationTypeList = educationTypeRepository.findAll();
        assertThat(educationTypeList).hasSize(databaseSizeBeforeUpdate);
        EducationType testEducationType = educationTypeList.get(educationTypeList.size() - 1);
        assertThat(testEducationType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the EducationType in Elasticsearch
        EducationType educationTypeEs = educationTypeSearchRepository.findOne(testEducationType.getIdEducationType());
        assertThat(educationTypeEs).isEqualToComparingFieldByField(testEducationType);
    }

    @Test
    @Transactional
    public void updateNonExistingEducationType() throws Exception {
        int databaseSizeBeforeUpdate = educationTypeRepository.findAll().size();

        // Create the EducationType
        EducationTypeDTO educationTypeDTO = educationTypeMapper.toDto(educationType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEducationTypeMockMvc.perform(put("/api/education-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EducationType in the database
        List<EducationType> educationTypeList = educationTypeRepository.findAll();
        assertThat(educationTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEducationType() throws Exception {
        // Initialize the database
        educationTypeRepository.saveAndFlush(educationType);
        educationTypeSearchRepository.save(educationType);
        int databaseSizeBeforeDelete = educationTypeRepository.findAll().size();

        // Get the educationType
        restEducationTypeMockMvc.perform(delete("/api/education-types/{id}", educationType.getIdEducationType())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean educationTypeExistsInEs = educationTypeSearchRepository.exists(educationType.getIdEducationType());
        assertThat(educationTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<EducationType> educationTypeList = educationTypeRepository.findAll();
        assertThat(educationTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEducationType() throws Exception {
        // Initialize the database
        educationTypeRepository.saveAndFlush(educationType);
        educationTypeSearchRepository.save(educationType);

        // Search the educationType
        restEducationTypeMockMvc.perform(get("/api/_search/education-types?query=idEducationType:" + educationType.getIdEducationType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEducationType").value(hasItem(educationType.getIdEducationType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationType.class);
        /*
        EducationType educationType1 = new EducationType();
<<<<<<< HEAD
        educationType1.setIdEducationType(1L);
=======
        educationType1.setId(1L);
>>>>>>> Branch_v4.5.4
        EducationType educationType2 = new EducationType();
        educationType2.setIdEducationType(educationType1.getId());
        assertThat(educationType1).isEqualTo(educationType2);
<<<<<<< HEAD
        educationType2.setIdEducationType(2L);
=======
        educationType2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(educationType1).isNotEqualTo(educationType2);
        educationType1.setIdEducationType(null);
        assertThat(educationType1).isNotEqualTo(educationType2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationTypeDTO.class);
        /*
        EducationTypeDTO educationTypeDTO1 = new EducationTypeDTO();
<<<<<<< HEAD
        educationTypeDTO1.setIdEducationType(1L);
=======
        educationTypeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        EducationTypeDTO educationTypeDTO2 = new EducationTypeDTO();
        assertThat(educationTypeDTO1).isNotEqualTo(educationTypeDTO2);
        educationTypeDTO2.setIdEducationType(educationTypeDTO1.getIdEducationType());
        assertThat(educationTypeDTO1).isEqualTo(educationTypeDTO2);
<<<<<<< HEAD
        educationTypeDTO2.setIdEducationType(2L);
=======
        educationTypeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(educationTypeDTO1).isNotEqualTo(educationTypeDTO2);
        educationTypeDTO1.setIdEducationType(null);
        assertThat(educationTypeDTO1).isNotEqualTo(educationTypeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(educationTypeMapper.fromidEducationType(42L).getidEducationType()).isEqualTo(42);
        //assertThat(educationTypeMapper.fromIdEducationType(null)).isNull();
    }
}
