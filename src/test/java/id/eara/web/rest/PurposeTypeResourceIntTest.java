package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PurposeType;
import id.eara.repository.PurposeTypeRepository;
import id.eara.service.PurposeTypeService;
import id.eara.repository.search.PurposeTypeSearchRepository;
import id.eara.service.dto.PurposeTypeDTO;
import id.eara.service.mapper.PurposeTypeMapper;
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
 * Test class for the PurposeTypeResource REST controller.
 *
 * @see PurposeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PurposeTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PurposeTypeRepository purposeTypeRepository;

    @Autowired
    private PurposeTypeMapper purposeTypeMapper;

    @Autowired
    private PurposeTypeService purposeTypeService;

    @Autowired
    private PurposeTypeSearchRepository purposeTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurposeTypeMockMvc;

    private PurposeType purposeType;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurposeTypeResource purposeTypeResource = new PurposeTypeResource(purposeTypeService);
        this.restPurposeTypeMockMvc = MockMvcBuilders.standaloneSetup(purposeTypeResource)
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
    public static PurposeType createEntity(EntityManager em) {
        PurposeType purposeType = new PurposeType()
            .description(DEFAULT_DESCRIPTION);
        return purposeType;
    }

    @Before
    public void initTest() {
        purposeTypeSearchRepository.deleteAll();
        purposeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurposeType() throws Exception {
        int databaseSizeBeforeCreate = purposeTypeRepository.findAll().size();

        // Create the PurposeType
        PurposeTypeDTO purposeTypeDTO = purposeTypeMapper.toDto(purposeType);

        restPurposeTypeMockMvc.perform(post("/api/purpose-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purposeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PurposeType in the database
        List<PurposeType> purposeTypeList = purposeTypeRepository.findAll();
        assertThat(purposeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PurposeType testPurposeType = purposeTypeList.get(purposeTypeList.size() - 1);
        assertThat(testPurposeType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the PurposeType in Elasticsearch
        PurposeType purposeTypeEs = purposeTypeSearchRepository.findOne(testPurposeType.getIdPurposeType());
        assertThat(purposeTypeEs).isEqualToComparingFieldByField(testPurposeType);
    }

    @Test
    @Transactional
    public void createPurposeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purposeTypeRepository.findAll().size();

        // Create the PurposeType with an existing ID
        purposeType.setIdPurposeType(1L);
        PurposeTypeDTO purposeTypeDTO = purposeTypeMapper.toDto(purposeType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurposeTypeMockMvc.perform(post("/api/purpose-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purposeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PurposeType> purposeTypeList = purposeTypeRepository.findAll();
        assertThat(purposeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPurposeTypes() throws Exception {
        // Initialize the database
        purposeTypeRepository.saveAndFlush(purposeType);

        // Get all the purposeTypeList
        restPurposeTypeMockMvc.perform(get("/api/purpose-types?sort=idPurposeType,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPurposeType").value(hasItem(purposeType.getIdPurposeType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPurposeType() throws Exception {
        // Initialize the database
        purposeTypeRepository.saveAndFlush(purposeType);

        // Get the purposeType
        restPurposeTypeMockMvc.perform(get("/api/purpose-types/{id}", purposeType.getIdPurposeType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPurposeType").value(purposeType.getIdPurposeType().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPurposeType() throws Exception {
        // Get the purposeType
        restPurposeTypeMockMvc.perform(get("/api/purpose-types/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurposeType() throws Exception {
        // Initialize the database
        purposeTypeRepository.saveAndFlush(purposeType);
        purposeTypeSearchRepository.save(purposeType);
        int databaseSizeBeforeUpdate = purposeTypeRepository.findAll().size();

        // Update the purposeType
        PurposeType updatedPurposeType = purposeTypeRepository.findOne(purposeType.getIdPurposeType());
        updatedPurposeType
            .description(UPDATED_DESCRIPTION);
        PurposeTypeDTO purposeTypeDTO = purposeTypeMapper.toDto(updatedPurposeType);

        restPurposeTypeMockMvc.perform(put("/api/purpose-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purposeTypeDTO)))
            .andExpect(status().isOk());

        // Validate the PurposeType in the database
        List<PurposeType> purposeTypeList = purposeTypeRepository.findAll();
        assertThat(purposeTypeList).hasSize(databaseSizeBeforeUpdate);
        PurposeType testPurposeType = purposeTypeList.get(purposeTypeList.size() - 1);
        assertThat(testPurposeType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the PurposeType in Elasticsearch
        PurposeType purposeTypeEs = purposeTypeSearchRepository.findOne(testPurposeType.getIdPurposeType());
        assertThat(purposeTypeEs).isEqualToComparingFieldByField(testPurposeType);
    }

    @Test
    @Transactional
    public void updateNonExistingPurposeType() throws Exception {
        int databaseSizeBeforeUpdate = purposeTypeRepository.findAll().size();

        // Create the PurposeType
        PurposeTypeDTO purposeTypeDTO = purposeTypeMapper.toDto(purposeType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPurposeTypeMockMvc.perform(put("/api/purpose-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purposeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PurposeType in the database
        List<PurposeType> purposeTypeList = purposeTypeRepository.findAll();
        assertThat(purposeTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePurposeType() throws Exception {
        // Initialize the database
        purposeTypeRepository.saveAndFlush(purposeType);
        purposeTypeSearchRepository.save(purposeType);
        int databaseSizeBeforeDelete = purposeTypeRepository.findAll().size();

        // Get the purposeType
        restPurposeTypeMockMvc.perform(delete("/api/purpose-types/{id}", purposeType.getIdPurposeType())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean purposeTypeExistsInEs = purposeTypeSearchRepository.exists(purposeType.getIdPurposeType());
        assertThat(purposeTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<PurposeType> purposeTypeList = purposeTypeRepository.findAll();
        assertThat(purposeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPurposeType() throws Exception {
        // Initialize the database
        purposeTypeRepository.saveAndFlush(purposeType);
        purposeTypeSearchRepository.save(purposeType);

        // Search the purposeType
        restPurposeTypeMockMvc.perform(get("/api/_search/purpose-types?query=idPurposeType:" + purposeType.getIdPurposeType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPurposeType").value(hasItem(purposeType.getIdPurposeType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurposeType.class);
        /*
        PurposeType purposeType1 = new PurposeType();
<<<<<<< HEAD
        purposeType1.setIdPurposeType(1L);
=======
        purposeType1.setId(1L);
>>>>>>> Branch_v4.5.4
        PurposeType purposeType2 = new PurposeType();
        purposeType2.setIdPurposeType(purposeType1.getId());
        assertThat(purposeType1).isEqualTo(purposeType2);
<<<<<<< HEAD
        purposeType2.setIdPurposeType(2L);
=======
        purposeType2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(purposeType1).isNotEqualTo(purposeType2);
        purposeType1.setIdPurposeType(null);
        assertThat(purposeType1).isNotEqualTo(purposeType2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurposeTypeDTO.class);
        /*
        PurposeTypeDTO purposeTypeDTO1 = new PurposeTypeDTO();
<<<<<<< HEAD
        purposeTypeDTO1.setIdPurposeType(1L);
=======
        purposeTypeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PurposeTypeDTO purposeTypeDTO2 = new PurposeTypeDTO();
        assertThat(purposeTypeDTO1).isNotEqualTo(purposeTypeDTO2);
        purposeTypeDTO2.setIdPurposeType(purposeTypeDTO1.getIdPurposeType());
        assertThat(purposeTypeDTO1).isEqualTo(purposeTypeDTO2);
<<<<<<< HEAD
        purposeTypeDTO2.setIdPurposeType(2L);
=======
        purposeTypeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(purposeTypeDTO1).isNotEqualTo(purposeTypeDTO2);
        purposeTypeDTO1.setIdPurposeType(null);
        assertThat(purposeTypeDTO1).isNotEqualTo(purposeTypeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(purposeTypeMapper.fromidPurposeType(42L).getidPurposeType()).isEqualTo(42);
        //assertThat(purposeTypeMapper.fromIdPurposeType(null)).isNull();
    }
}
