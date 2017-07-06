package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.WorkType;
import id.eara.repository.WorkTypeRepository;
import id.eara.service.WorkTypeService;
import id.eara.repository.search.WorkTypeSearchRepository;
import id.eara.service.dto.WorkTypeDTO;
import id.eara.service.mapper.WorkTypeMapper;
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
 * Test class for the WorkTypeResource REST controller.
 *
 * @see WorkTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class WorkTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private WorkTypeRepository workTypeRepository;

    @Autowired
    private WorkTypeMapper workTypeMapper;

    @Autowired
    private WorkTypeService workTypeService;

    @Autowired
    private WorkTypeSearchRepository workTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkTypeMockMvc;

    private WorkType workType;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkTypeResource workTypeResource = new WorkTypeResource(workTypeService);
        this.restWorkTypeMockMvc = MockMvcBuilders.standaloneSetup(workTypeResource)
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
    public static WorkType createEntity(EntityManager em) {
        WorkType workType = new WorkType()
            .description(DEFAULT_DESCRIPTION);
        return workType;
    }

    @Before
    public void initTest() {
        workTypeSearchRepository.deleteAll();
        workType = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkType() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType
        WorkTypeDTO workTypeDTO = workTypeMapper.toDto(workType);

        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate + 1);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the WorkType in Elasticsearch
        WorkType workTypeEs = workTypeSearchRepository.findOne(testWorkType.getIdWorkType());
        assertThat(workTypeEs).isEqualToComparingFieldByField(testWorkType);
    }

    @Test
    @Transactional
    public void createWorkTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workTypeRepository.findAll().size();

        // Create the WorkType with an existing ID
        workType.setIdWorkType(1L);
        WorkTypeDTO workTypeDTO = workTypeMapper.toDto(workType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTypeMockMvc.perform(post("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWorkTypes() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get all the workTypeList
        restWorkTypeMockMvc.perform(get("/api/work-types?sort=idWorkType,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idWorkType").value(hasItem(workType.getIdWorkType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);

        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}", workType.getIdWorkType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idWorkType").value(workType.getIdWorkType().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkType() throws Exception {
        // Get the workType
        restWorkTypeMockMvc.perform(get("/api/work-types/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);
        workTypeSearchRepository.save(workType);
        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Update the workType
        WorkType updatedWorkType = workTypeRepository.findOne(workType.getIdWorkType());
        updatedWorkType
            .description(UPDATED_DESCRIPTION);
        WorkTypeDTO workTypeDTO = workTypeMapper.toDto(updatedWorkType);

        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeDTO)))
            .andExpect(status().isOk());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate);
        WorkType testWorkType = workTypeList.get(workTypeList.size() - 1);
        assertThat(testWorkType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the WorkType in Elasticsearch
        WorkType workTypeEs = workTypeSearchRepository.findOne(testWorkType.getIdWorkType());
        assertThat(workTypeEs).isEqualToComparingFieldByField(testWorkType);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkType() throws Exception {
        int databaseSizeBeforeUpdate = workTypeRepository.findAll().size();

        // Create the WorkType
        WorkTypeDTO workTypeDTO = workTypeMapper.toDto(workType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkTypeMockMvc.perform(put("/api/work-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkType in the database
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);
        workTypeSearchRepository.save(workType);
        int databaseSizeBeforeDelete = workTypeRepository.findAll().size();

        // Get the workType
        restWorkTypeMockMvc.perform(delete("/api/work-types/{id}", workType.getIdWorkType())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean workTypeExistsInEs = workTypeSearchRepository.exists(workType.getIdWorkType());
        assertThat(workTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkType> workTypeList = workTypeRepository.findAll();
        assertThat(workTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkType() throws Exception {
        // Initialize the database
        workTypeRepository.saveAndFlush(workType);
        workTypeSearchRepository.save(workType);

        // Search the workType
        restWorkTypeMockMvc.perform(get("/api/_search/work-types?query=idWorkType:" + workType.getIdWorkType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idWorkType").value(hasItem(workType.getIdWorkType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkType.class);
        /*
        WorkType workType1 = new WorkType();
<<<<<<< HEAD
        workType1.setIdWorkType(1L);
=======
        workType1.setId(1L);
>>>>>>> Branch_v4.5.4
        WorkType workType2 = new WorkType();
        workType2.setIdWorkType(workType1.getId());
        assertThat(workType1).isEqualTo(workType2);
<<<<<<< HEAD
        workType2.setIdWorkType(2L);
=======
        workType2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(workType1).isNotEqualTo(workType2);
        workType1.setIdWorkType(null);
        assertThat(workType1).isNotEqualTo(workType2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkTypeDTO.class);
        /*
        WorkTypeDTO workTypeDTO1 = new WorkTypeDTO();
<<<<<<< HEAD
        workTypeDTO1.setIdWorkType(1L);
=======
        workTypeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        WorkTypeDTO workTypeDTO2 = new WorkTypeDTO();
        assertThat(workTypeDTO1).isNotEqualTo(workTypeDTO2);
        workTypeDTO2.setIdWorkType(workTypeDTO1.getIdWorkType());
        assertThat(workTypeDTO1).isEqualTo(workTypeDTO2);
<<<<<<< HEAD
        workTypeDTO2.setIdWorkType(2L);
=======
        workTypeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(workTypeDTO1).isNotEqualTo(workTypeDTO2);
        workTypeDTO1.setIdWorkType(null);
        assertThat(workTypeDTO1).isNotEqualTo(workTypeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(workTypeMapper.fromidWorkType(42L).getidWorkType()).isEqualTo(42);
        //assertThat(workTypeMapper.fromIdWorkType(null)).isNull();
    }
}
