package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PeriodType;
import id.eara.repository.PeriodTypeRepository;
import id.eara.service.PeriodTypeService;
import id.eara.repository.search.PeriodTypeSearchRepository;
import id.eara.service.dto.PeriodTypeDTO;
import id.eara.service.mapper.PeriodTypeMapper;
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
 * Test class for the PeriodTypeResource REST controller.
 *
 * @see PeriodTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PeriodTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_FROM_DAY = 1;
    private static final Integer UPDATED_FROM_DAY = 2;

    private static final Integer DEFAULT_FROM_MONTH = 1;
    private static final Integer UPDATED_FROM_MONTH = 2;

    private static final Integer DEFAULT_THRU_DAY = 1;
    private static final Integer UPDATED_THRU_DAY = 2;

    private static final Integer DEFAULT_THRU_MONTH = 1;
    private static final Integer UPDATED_THRU_MONTH = 2;

    private static final Integer DEFAULT_FROM_ADD_YEAR = 1;
    private static final Integer UPDATED_FROM_ADD_YEAR = 2;

    private static final Integer DEFAULT_THRU_ADD_YEAR = 1;
    private static final Integer UPDATED_THRU_ADD_YEAR = 2;

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;

    private static final Integer DEFAULT_ID_PARENT = 1;
    private static final Integer UPDATED_ID_PARENT = 2;

    @Autowired
    private PeriodTypeRepository periodTypeRepository;

    @Autowired
    private PeriodTypeMapper periodTypeMapper;

    @Autowired
    private PeriodTypeService periodTypeService;

    @Autowired
    private PeriodTypeSearchRepository periodTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeriodTypeMockMvc;

    private PeriodType periodType;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeriodTypeResource periodTypeResource = new PeriodTypeResource(periodTypeService);
        this.restPeriodTypeMockMvc = MockMvcBuilders.standaloneSetup(periodTypeResource)
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
    public static PeriodType createEntity(EntityManager em) {
        PeriodType periodType = new PeriodType()
            .description(DEFAULT_DESCRIPTION)
            .fromDay(DEFAULT_FROM_DAY)
            .fromMonth(DEFAULT_FROM_MONTH)
            .thruDay(DEFAULT_THRU_DAY)
            .thruMonth(DEFAULT_THRU_MONTH)
            .fromAddYear(DEFAULT_FROM_ADD_YEAR)
            .thruAddYear(DEFAULT_THRU_ADD_YEAR)
            .sequence(DEFAULT_SEQUENCE)
            .idParent(DEFAULT_ID_PARENT);
        return periodType;
    }

    @Before
    public void initTest() {
        periodTypeSearchRepository.deleteAll();
        periodType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodType() throws Exception {
        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();

        // Create the PeriodType
        PeriodTypeDTO periodTypeDTO = periodTypeMapper.toDto(periodType);

        restPeriodTypeMockMvc.perform(post("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPeriodType.getFromDay()).isEqualTo(DEFAULT_FROM_DAY);
        assertThat(testPeriodType.getFromMonth()).isEqualTo(DEFAULT_FROM_MONTH);
        assertThat(testPeriodType.getThruDay()).isEqualTo(DEFAULT_THRU_DAY);
        assertThat(testPeriodType.getThruMonth()).isEqualTo(DEFAULT_THRU_MONTH);
        assertThat(testPeriodType.getFromAddYear()).isEqualTo(DEFAULT_FROM_ADD_YEAR);
        assertThat(testPeriodType.getThruAddYear()).isEqualTo(DEFAULT_THRU_ADD_YEAR);
        assertThat(testPeriodType.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testPeriodType.getIdParent()).isEqualTo(DEFAULT_ID_PARENT);

        // Validate the PeriodType in Elasticsearch
        PeriodType periodTypeEs = periodTypeSearchRepository.findOne(testPeriodType.getIdPeriodType());
        assertThat(periodTypeEs).isEqualToComparingFieldByField(testPeriodType);
    }

    @Test
    @Transactional
    public void createPeriodTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();

        // Create the PeriodType with an existing ID
        periodType.setIdPeriodType(1L);
        PeriodTypeDTO periodTypeDTO = periodTypeMapper.toDto(periodType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodTypeMockMvc.perform(post("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPeriodTypes() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList
        restPeriodTypeMockMvc.perform(get("/api/period-types?sort=idPeriodType,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPeriodType").value(hasItem(periodType.getIdPeriodType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fromDay").value(hasItem(DEFAULT_FROM_DAY)))
            .andExpect(jsonPath("$.[*].fromMonth").value(hasItem(DEFAULT_FROM_MONTH)))
            .andExpect(jsonPath("$.[*].thruDay").value(hasItem(DEFAULT_THRU_DAY)))
            .andExpect(jsonPath("$.[*].thruMonth").value(hasItem(DEFAULT_THRU_MONTH)))
            .andExpect(jsonPath("$.[*].fromAddYear").value(hasItem(DEFAULT_FROM_ADD_YEAR)))
            .andExpect(jsonPath("$.[*].thruAddYear").value(hasItem(DEFAULT_THRU_ADD_YEAR)))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].idParent").value(hasItem(DEFAULT_ID_PARENT)));
    }

    @Test
    @Transactional
    public void getPeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get the periodType
        restPeriodTypeMockMvc.perform(get("/api/period-types/{id}", periodType.getIdPeriodType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPeriodType").value(periodType.getIdPeriodType().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fromDay").value(DEFAULT_FROM_DAY))
            .andExpect(jsonPath("$.fromMonth").value(DEFAULT_FROM_MONTH))
            .andExpect(jsonPath("$.thruDay").value(DEFAULT_THRU_DAY))
            .andExpect(jsonPath("$.thruMonth").value(DEFAULT_THRU_MONTH))
            .andExpect(jsonPath("$.fromAddYear").value(DEFAULT_FROM_ADD_YEAR))
            .andExpect(jsonPath("$.thruAddYear").value(DEFAULT_THRU_ADD_YEAR))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE))
            .andExpect(jsonPath("$.idParent").value(DEFAULT_ID_PARENT));
    }

    @Test
    @Transactional
    public void getNonExistingPeriodType() throws Exception {
        // Get the periodType
        restPeriodTypeMockMvc.perform(get("/api/period-types/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);
        periodTypeSearchRepository.save(periodType);
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Update the periodType
        PeriodType updatedPeriodType = periodTypeRepository.findOne(periodType.getIdPeriodType());
        updatedPeriodType
            .description(UPDATED_DESCRIPTION)
            .fromDay(UPDATED_FROM_DAY)
            .fromMonth(UPDATED_FROM_MONTH)
            .thruDay(UPDATED_THRU_DAY)
            .thruMonth(UPDATED_THRU_MONTH)
            .fromAddYear(UPDATED_FROM_ADD_YEAR)
            .thruAddYear(UPDATED_THRU_ADD_YEAR)
            .sequence(UPDATED_SEQUENCE)
            .idParent(UPDATED_ID_PARENT);
        PeriodTypeDTO periodTypeDTO = periodTypeMapper.toDto(updatedPeriodType);

        restPeriodTypeMockMvc.perform(put("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodTypeDTO)))
            .andExpect(status().isOk());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPeriodType.getFromDay()).isEqualTo(UPDATED_FROM_DAY);
        assertThat(testPeriodType.getFromMonth()).isEqualTo(UPDATED_FROM_MONTH);
        assertThat(testPeriodType.getThruDay()).isEqualTo(UPDATED_THRU_DAY);
        assertThat(testPeriodType.getThruMonth()).isEqualTo(UPDATED_THRU_MONTH);
        assertThat(testPeriodType.getFromAddYear()).isEqualTo(UPDATED_FROM_ADD_YEAR);
        assertThat(testPeriodType.getThruAddYear()).isEqualTo(UPDATED_THRU_ADD_YEAR);
        assertThat(testPeriodType.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testPeriodType.getIdParent()).isEqualTo(UPDATED_ID_PARENT);

        // Validate the PeriodType in Elasticsearch
        PeriodType periodTypeEs = periodTypeSearchRepository.findOne(testPeriodType.getIdPeriodType());
        assertThat(periodTypeEs).isEqualToComparingFieldByField(testPeriodType);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Create the PeriodType
        PeriodTypeDTO periodTypeDTO = periodTypeMapper.toDto(periodType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeriodTypeMockMvc.perform(put("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);
        periodTypeSearchRepository.save(periodType);
        int databaseSizeBeforeDelete = periodTypeRepository.findAll().size();

        // Get the periodType
        restPeriodTypeMockMvc.perform(delete("/api/period-types/{id}", periodType.getIdPeriodType())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean periodTypeExistsInEs = periodTypeSearchRepository.exists(periodType.getIdPeriodType());
        assertThat(periodTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);
        periodTypeSearchRepository.save(periodType);

        // Search the periodType
        restPeriodTypeMockMvc.perform(get("/api/_search/period-types?query=idPeriodType:" + periodType.getIdPeriodType()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPeriodType").value(hasItem(periodType.getIdPeriodType().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fromDay").value(hasItem(DEFAULT_FROM_DAY)))
            .andExpect(jsonPath("$.[*].fromMonth").value(hasItem(DEFAULT_FROM_MONTH)))
            .andExpect(jsonPath("$.[*].thruDay").value(hasItem(DEFAULT_THRU_DAY)))
            .andExpect(jsonPath("$.[*].thruMonth").value(hasItem(DEFAULT_THRU_MONTH)))
            .andExpect(jsonPath("$.[*].fromAddYear").value(hasItem(DEFAULT_FROM_ADD_YEAR)))
            .andExpect(jsonPath("$.[*].thruAddYear").value(hasItem(DEFAULT_THRU_ADD_YEAR)))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].idParent").value(hasItem(DEFAULT_ID_PARENT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodType.class);
        /*
        PeriodType periodType1 = new PeriodType();
<<<<<<< HEAD
        periodType1.setIdPeriodType(1L);
=======
        periodType1.setId(1L);
>>>>>>> Branch_v4.5.4
        PeriodType periodType2 = new PeriodType();
        periodType2.setIdPeriodType(periodType1.getId());
        assertThat(periodType1).isEqualTo(periodType2);
<<<<<<< HEAD
        periodType2.setIdPeriodType(2L);
=======
        periodType2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(periodType1).isNotEqualTo(periodType2);
        periodType1.setIdPeriodType(null);
        assertThat(periodType1).isNotEqualTo(periodType2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodTypeDTO.class);
        /*
        PeriodTypeDTO periodTypeDTO1 = new PeriodTypeDTO();
<<<<<<< HEAD
        periodTypeDTO1.setIdPeriodType(1L);
=======
        periodTypeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PeriodTypeDTO periodTypeDTO2 = new PeriodTypeDTO();
        assertThat(periodTypeDTO1).isNotEqualTo(periodTypeDTO2);
        periodTypeDTO2.setIdPeriodType(periodTypeDTO1.getIdPeriodType());
        assertThat(periodTypeDTO1).isEqualTo(periodTypeDTO2);
<<<<<<< HEAD
        periodTypeDTO2.setIdPeriodType(2L);
=======
        periodTypeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(periodTypeDTO1).isNotEqualTo(periodTypeDTO2);
        periodTypeDTO1.setIdPeriodType(null);
        assertThat(periodTypeDTO1).isNotEqualTo(periodTypeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(periodTypeMapper.fromidPeriodType(42L).getidPeriodType()).isEqualTo(42);
        //assertThat(periodTypeMapper.fromIdPeriodType(null)).isNull();
    }
}
