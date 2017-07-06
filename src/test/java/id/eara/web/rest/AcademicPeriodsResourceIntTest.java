package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.AcademicPeriods;
import id.eara.repository.AcademicPeriodsRepository;
import id.eara.service.AcademicPeriodsService;
import id.eara.repository.search.AcademicPeriodsSearchRepository;
import id.eara.service.dto.AcademicPeriodsDTO;
import id.eara.service.mapper.AcademicPeriodsMapper;
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
 * Test class for the AcademicPeriodsResource REST controller.
 *
 * @see AcademicPeriodsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class AcademicPeriodsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String DEFAULT_FORMAT_1 = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT_1 = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_THRU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_THRU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AcademicPeriodsRepository academicPeriodsRepository;

    @Autowired
    private AcademicPeriodsMapper academicPeriodsMapper;

    @Autowired
    private AcademicPeriodsService academicPeriodsService;

    @Autowired
    private AcademicPeriodsSearchRepository academicPeriodsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAcademicPeriodsMockMvc;

    private AcademicPeriods academicPeriods;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AcademicPeriodsResource academicPeriodsResource = new AcademicPeriodsResource(academicPeriodsService);
        this.restAcademicPeriodsMockMvc = MockMvcBuilders.standaloneSetup(academicPeriodsResource)
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
    public static AcademicPeriods createEntity(EntityManager em) {
        AcademicPeriods academicPeriods = new AcademicPeriods()
            .description(DEFAULT_DESCRIPTION)
            .year(DEFAULT_YEAR)
            .format1(DEFAULT_FORMAT_1)
            .dateFrom(DEFAULT_DATE_FROM)
            .dateThru(DEFAULT_DATE_THRU);
        return academicPeriods;
    }

    @Before
    public void initTest() {
        academicPeriodsSearchRepository.deleteAll();
        academicPeriods = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcademicPeriods() throws Exception {
        int databaseSizeBeforeCreate = academicPeriodsRepository.findAll().size();

        // Create the AcademicPeriods
        AcademicPeriodsDTO academicPeriodsDTO = academicPeriodsMapper.toDto(academicPeriods);

        restAcademicPeriodsMockMvc.perform(post("/api/academic-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicPeriodsDTO)))
            .andExpect(status().isCreated());

        // Validate the AcademicPeriods in the database
        List<AcademicPeriods> academicPeriodsList = academicPeriodsRepository.findAll();
        assertThat(academicPeriodsList).hasSize(databaseSizeBeforeCreate + 1);
        AcademicPeriods testAcademicPeriods = academicPeriodsList.get(academicPeriodsList.size() - 1);
        assertThat(testAcademicPeriods.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAcademicPeriods.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testAcademicPeriods.getFormat1()).isEqualTo(DEFAULT_FORMAT_1);
        assertThat(testAcademicPeriods.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testAcademicPeriods.getDateThru()).isEqualTo(DEFAULT_DATE_THRU);

        // Validate the AcademicPeriods in Elasticsearch
        AcademicPeriods academicPeriodsEs = academicPeriodsSearchRepository.findOne(testAcademicPeriods.getIdPeriod());
        assertThat(academicPeriodsEs).isEqualToComparingFieldByField(testAcademicPeriods);
        keyEntity = testAcademicPeriods.getIdPeriod();
    }

    @Test
    @Transactional
    public void createAcademicPeriodsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = academicPeriodsRepository.findAll().size();

        // Create the AcademicPeriods with an existing ID
        academicPeriods.setIdPeriod(keyEntity);
        AcademicPeriodsDTO academicPeriodsDTO = academicPeriodsMapper.toDto(academicPeriods);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademicPeriodsMockMvc.perform(post("/api/academic-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicPeriodsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AcademicPeriods> academicPeriodsList = academicPeriodsRepository.findAll();
        assertThat(academicPeriodsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAcademicPeriods() throws Exception {
        // Initialize the database
        academicPeriodsRepository.saveAndFlush(academicPeriods);

        // Get all the academicPeriodsList
        restAcademicPeriodsMockMvc.perform(get("/api/academic-periods?sort=idPeriod,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPeriod").value(hasItem(academicPeriods.getIdPeriod().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].format1").value(hasItem(DEFAULT_FORMAT_1.toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void getAcademicPeriods() throws Exception {
        // Initialize the database
        academicPeriodsRepository.saveAndFlush(academicPeriods);

        // Get the academicPeriods
        restAcademicPeriodsMockMvc.perform(get("/api/academic-periods/{id}", academicPeriods.getIdPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPeriod").value(academicPeriods.getIdPeriod().toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.format1").value(DEFAULT_FORMAT_1.toString()))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateThru").value(sameInstant(DEFAULT_DATE_THRU)));
    }

    @Test
    @Transactional
    public void getNonExistingAcademicPeriods() throws Exception {
        // Get the academicPeriods
        restAcademicPeriodsMockMvc.perform(get("/api/academic-periods/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcademicPeriods() throws Exception {
        // Initialize the database
        academicPeriodsRepository.saveAndFlush(academicPeriods);
        academicPeriodsSearchRepository.save(academicPeriods);
        int databaseSizeBeforeUpdate = academicPeriodsRepository.findAll().size();

        // Update the academicPeriods
        AcademicPeriods updatedAcademicPeriods = academicPeriodsRepository.findOne(academicPeriods.getIdPeriod());
        updatedAcademicPeriods
            .description(UPDATED_DESCRIPTION)
            .year(UPDATED_YEAR)
            .format1(UPDATED_FORMAT_1)
            .dateFrom(UPDATED_DATE_FROM)
            .dateThru(UPDATED_DATE_THRU);
        AcademicPeriodsDTO academicPeriodsDTO = academicPeriodsMapper.toDto(updatedAcademicPeriods);

        restAcademicPeriodsMockMvc.perform(put("/api/academic-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicPeriodsDTO)))
            .andExpect(status().isOk());

        // Validate the AcademicPeriods in the database
        List<AcademicPeriods> academicPeriodsList = academicPeriodsRepository.findAll();
        assertThat(academicPeriodsList).hasSize(databaseSizeBeforeUpdate);
        AcademicPeriods testAcademicPeriods = academicPeriodsList.get(academicPeriodsList.size() - 1);
        assertThat(testAcademicPeriods.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAcademicPeriods.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAcademicPeriods.getFormat1()).isEqualTo(UPDATED_FORMAT_1);
        assertThat(testAcademicPeriods.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testAcademicPeriods.getDateThru()).isEqualTo(UPDATED_DATE_THRU);

        // Validate the AcademicPeriods in Elasticsearch
        AcademicPeriods academicPeriodsEs = academicPeriodsSearchRepository.findOne(testAcademicPeriods.getIdPeriod());
        assertThat(academicPeriodsEs).isEqualToComparingFieldByField(testAcademicPeriods);
    }

    @Test
    @Transactional
    public void updateNonExistingAcademicPeriods() throws Exception {
        int databaseSizeBeforeUpdate = academicPeriodsRepository.findAll().size();

        // Create the AcademicPeriods
        AcademicPeriodsDTO academicPeriodsDTO = academicPeriodsMapper.toDto(academicPeriods);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAcademicPeriodsMockMvc.perform(put("/api/academic-periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicPeriodsDTO)))
            .andExpect(status().isCreated());

        // Validate the AcademicPeriods in the database
        List<AcademicPeriods> academicPeriodsList = academicPeriodsRepository.findAll();
        assertThat(academicPeriodsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAcademicPeriods() throws Exception {
        // Initialize the database
        academicPeriodsRepository.saveAndFlush(academicPeriods);
        academicPeriodsSearchRepository.save(academicPeriods);
        int databaseSizeBeforeDelete = academicPeriodsRepository.findAll().size();

        // Get the academicPeriods
        restAcademicPeriodsMockMvc.perform(delete("/api/academic-periods/{id}", academicPeriods.getIdPeriod())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean academicPeriodsExistsInEs = academicPeriodsSearchRepository.exists(academicPeriods.getIdPeriod());
        assertThat(academicPeriodsExistsInEs).isFalse();

        // Validate the database is empty
        List<AcademicPeriods> academicPeriodsList = academicPeriodsRepository.findAll();
        assertThat(academicPeriodsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAcademicPeriods() throws Exception {
        // Initialize the database
        academicPeriodsRepository.saveAndFlush(academicPeriods);
        academicPeriodsSearchRepository.save(academicPeriods);

        // Search the academicPeriods
        restAcademicPeriodsMockMvc.perform(get("/api/_search/academic-periods?query=idPeriod:" + academicPeriods.getIdPeriod()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPeriod").value(hasItem(academicPeriods.getIdPeriod().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].format1").value(hasItem(DEFAULT_FORMAT_1.toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicPeriods.class);
        /*
        AcademicPeriods academicPeriods1 = new AcademicPeriods();
<<<<<<< HEAD
        academicPeriods1.setIdPeriod(1L);
=======
        academicPeriods1.setId(1L);
>>>>>>> Branch_v4.5.4
        AcademicPeriods academicPeriods2 = new AcademicPeriods();
        academicPeriods2.setIdPeriod(academicPeriods1.getId());
        assertThat(academicPeriods1).isEqualTo(academicPeriods2);
<<<<<<< HEAD
        academicPeriods2.setIdPeriod(2L);
=======
        academicPeriods2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(academicPeriods1).isNotEqualTo(academicPeriods2);
        academicPeriods1.setIdPeriod(null);
        assertThat(academicPeriods1).isNotEqualTo(academicPeriods2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicPeriodsDTO.class);
        /*
        AcademicPeriodsDTO academicPeriodsDTO1 = new AcademicPeriodsDTO();
<<<<<<< HEAD
        academicPeriodsDTO1.setIdPeriod(1L);
=======
        academicPeriodsDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        AcademicPeriodsDTO academicPeriodsDTO2 = new AcademicPeriodsDTO();
        assertThat(academicPeriodsDTO1).isNotEqualTo(academicPeriodsDTO2);
        academicPeriodsDTO2.setIdPeriod(academicPeriodsDTO1.getIdPeriod());
        assertThat(academicPeriodsDTO1).isEqualTo(academicPeriodsDTO2);
<<<<<<< HEAD
        academicPeriodsDTO2.setIdPeriod(2L);
=======
        academicPeriodsDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(academicPeriodsDTO1).isNotEqualTo(academicPeriodsDTO2);
        academicPeriodsDTO1.setIdPeriod(null);
        assertThat(academicPeriodsDTO1).isNotEqualTo(academicPeriodsDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(academicPeriodsMapper.fromidPeriod(42L).getidPeriod()).isEqualTo(42);
        //assertThat(academicPeriodsMapper.fromIdPeriod(null)).isNull();
    }
}
