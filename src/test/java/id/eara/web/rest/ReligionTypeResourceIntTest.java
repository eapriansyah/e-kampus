package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ReligionType;
import id.eara.repository.ReligionTypeRepository;
import id.eara.service.ReligionTypeService;
import id.eara.repository.search.ReligionTypeSearchRepository;
import id.eara.service.dto.ReligionTypeDTO;
import id.eara.service.mapper.ReligionTypeMapper;
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
 * Test class for the ReligionTypeResource REST controller.
 *
 * @see ReligionTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ReligionTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ReligionTypeRepository religionTypeRepository;

    @Autowired
    private ReligionTypeMapper religionTypeMapper;

    @Autowired
    private ReligionTypeService religionTypeService;

    @Autowired
    private ReligionTypeSearchRepository religionTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReligionTypeMockMvc;

    private ReligionType religionType;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReligionTypeResource religionTypeResource = new ReligionTypeResource(religionTypeService);
        this.restReligionTypeMockMvc = MockMvcBuilders.standaloneSetup(religionTypeResource)
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
    public static ReligionType createEntity(EntityManager em) {
        ReligionType religionType = new ReligionType()
            .description(DEFAULT_DESCRIPTION);
        return religionType;
    }

    @Before
    public void initTest() {
        religionTypeSearchRepository.deleteAll();
        religionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createReligionType() throws Exception {
        int databaseSizeBeforeCreate = religionTypeRepository.findAll().size();

        // Create the ReligionType
        ReligionTypeDTO religionTypeDTO = religionTypeMapper.toDto(religionType);

        restReligionTypeMockMvc.perform(post("/api/religion-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ReligionType in the database
        List<ReligionType> religionTypeList = religionTypeRepository.findAll();
        assertThat(religionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ReligionType testReligionType = religionTypeList.get(religionTypeList.size() - 1);
        assertThat(testReligionType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ReligionType in Elasticsearch
        ReligionType religionTypeEs = religionTypeSearchRepository.findOne(testReligionType.getIdReligionType());
        assertThat(religionTypeEs).isEqualToComparingFieldByField(testReligionType);
    }

    @Test
    @Transactional
    public void createReligionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = religionTypeRepository.findAll().size();

        // Create the ReligionType with an existing ID
        religionType.setIdReligionType(1L);
        ReligionTypeDTO religionTypeDTO = religionTypeMapper.toDto(religionType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReligionTypeMockMvc.perform(post("/api/religion-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religionTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ReligionType> religionTypeList = religionTypeRepository.findAll();
        assertThat(religionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReligionTypes() throws Exception {
        // Initialize the database
        religionTypeRepository.saveAndFlush(religionType);

        // Get all the religionTypeList
        restReligionTypeMockMvc.perform(get("/api/religion-types?sort=idReligionType,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idReligionType").value(hasItem(religionType.getIdReligionType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getReligionType() throws Exception {
        // Initialize the database
        religionTypeRepository.saveAndFlush(religionType);

        // Get the religionType
        restReligionTypeMockMvc.perform(get("/api/religion-types/{id}", religionType.getIdReligionType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idReligionType").value(religionType.getIdReligionType().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReligionType() throws Exception {
        // Get the religionType
        restReligionTypeMockMvc.perform(get("/api/religion-types/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReligionType() throws Exception {
        // Initialize the database
        religionTypeRepository.saveAndFlush(religionType);
        religionTypeSearchRepository.save(religionType);
        int databaseSizeBeforeUpdate = religionTypeRepository.findAll().size();

        // Update the religionType
        ReligionType updatedReligionType = religionTypeRepository.findOne(religionType.getIdReligionType());
        updatedReligionType
            .description(UPDATED_DESCRIPTION);
        ReligionTypeDTO religionTypeDTO = religionTypeMapper.toDto(updatedReligionType);

        restReligionTypeMockMvc.perform(put("/api/religion-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religionTypeDTO)))
            .andExpect(status().isOk());

        // Validate the ReligionType in the database
        List<ReligionType> religionTypeList = religionTypeRepository.findAll();
        assertThat(religionTypeList).hasSize(databaseSizeBeforeUpdate);
        ReligionType testReligionType = religionTypeList.get(religionTypeList.size() - 1);
        assertThat(testReligionType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ReligionType in Elasticsearch
        ReligionType religionTypeEs = religionTypeSearchRepository.findOne(testReligionType.getIdReligionType());
        assertThat(religionTypeEs).isEqualToComparingFieldByField(testReligionType);
    }

    @Test
    @Transactional
    public void updateNonExistingReligionType() throws Exception {
        int databaseSizeBeforeUpdate = religionTypeRepository.findAll().size();

        // Create the ReligionType
        ReligionTypeDTO religionTypeDTO = religionTypeMapper.toDto(religionType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReligionTypeMockMvc.perform(put("/api/religion-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(religionTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ReligionType in the database
        List<ReligionType> religionTypeList = religionTypeRepository.findAll();
        assertThat(religionTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReligionType() throws Exception {
        // Initialize the database
        religionTypeRepository.saveAndFlush(religionType);
        religionTypeSearchRepository.save(religionType);
        int databaseSizeBeforeDelete = religionTypeRepository.findAll().size();

        // Get the religionType
        restReligionTypeMockMvc.perform(delete("/api/religion-types/{id}", religionType.getIdReligionType())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean religionTypeExistsInEs = religionTypeSearchRepository.exists(religionType.getIdReligionType());
        assertThat(religionTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<ReligionType> religionTypeList = religionTypeRepository.findAll();
        assertThat(religionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReligionType() throws Exception {
        // Initialize the database
        religionTypeRepository.saveAndFlush(religionType);
        religionTypeSearchRepository.save(religionType);

        // Search the religionType
        restReligionTypeMockMvc.perform(get("/api/_search/religion-types?query=idReligionType:" + religionType.getIdReligionType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idReligionType").value(hasItem(religionType.getIdReligionType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReligionType.class);
        /*
        ReligionType religionType1 = new ReligionType();
<<<<<<< HEAD
        religionType1.setIdReligionType(1L);
=======
        religionType1.setId(1L);
>>>>>>> Branch_v4.5.4
        ReligionType religionType2 = new ReligionType();
        religionType2.setIdReligionType(religionType1.getId());
        assertThat(religionType1).isEqualTo(religionType2);
<<<<<<< HEAD
        religionType2.setIdReligionType(2L);
=======
        religionType2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(religionType1).isNotEqualTo(religionType2);
        religionType1.setIdReligionType(null);
        assertThat(religionType1).isNotEqualTo(religionType2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReligionTypeDTO.class);
        /*
        ReligionTypeDTO religionTypeDTO1 = new ReligionTypeDTO();
<<<<<<< HEAD
        religionTypeDTO1.setIdReligionType(1L);
=======
        religionTypeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ReligionTypeDTO religionTypeDTO2 = new ReligionTypeDTO();
        assertThat(religionTypeDTO1).isNotEqualTo(religionTypeDTO2);
        religionTypeDTO2.setIdReligionType(religionTypeDTO1.getIdReligionType());
        assertThat(religionTypeDTO1).isEqualTo(religionTypeDTO2);
<<<<<<< HEAD
        religionTypeDTO2.setIdReligionType(2L);
=======
        religionTypeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(religionTypeDTO1).isNotEqualTo(religionTypeDTO2);
        religionTypeDTO1.setIdReligionType(null);
        assertThat(religionTypeDTO1).isNotEqualTo(religionTypeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(religionTypeMapper.fromidReligionType(42L).getidReligionType()).isEqualTo(42);
        //assertThat(religionTypeMapper.fromIdReligionType(null)).isNull();
    }
}
