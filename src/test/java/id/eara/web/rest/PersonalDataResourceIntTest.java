package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PersonalData;
import id.eara.repository.PersonalDataRepository;
import id.eara.service.PersonalDataService;
import id.eara.repository.search.PersonalDataSearchRepository;
import id.eara.service.dto.PersonalDataDTO;
import id.eara.service.mapper.PersonalDataMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersonalDataResource REST controller.
 *
 * @see PersonalDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PersonalDataResourceIntTest {

    private static final String DEFAULT_NISN = "AAAAAAAAAA";
    private static final String UPDATED_NISN = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FATHER_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FATHER_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MOTHER_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MOTHER_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_HAS_PAUD = 1;
    private static final Integer UPDATED_HAS_PAUD = 2;

    private static final Integer DEFAULT_HAS_TK = 1;
    private static final Integer UPDATED_HAS_TK = 2;

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Autowired
    private PersonalDataMapper personalDataMapper;

    @Autowired
    private PersonalDataService personalDataService;

    @Autowired
    private PersonalDataSearchRepository personalDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPersonalDataMockMvc;

    private PersonalData personalData;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonalDataResource personalDataResource = new PersonalDataResource(personalDataService);
        this.restPersonalDataMockMvc = MockMvcBuilders.standaloneSetup(personalDataResource)
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
    public static PersonalData createEntity(EntityManager em) {
        PersonalData personalData = new PersonalData()
            .nisn(DEFAULT_NISN)
            .motherName(DEFAULT_MOTHER_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .fatherDob(DEFAULT_FATHER_DOB)
            .motherDob(DEFAULT_MOTHER_DOB)
            .hasPaud(DEFAULT_HAS_PAUD)
            .hasTk(DEFAULT_HAS_TK);
        return personalData;
    }

    @Before
    public void initTest() {
        personalDataSearchRepository.deleteAll();
        personalData = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonalData() throws Exception {
        int databaseSizeBeforeCreate = personalDataRepository.findAll().size();

        // Create the PersonalData
        PersonalDataDTO personalDataDTO = personalDataMapper.toDto(personalData);

        restPersonalDataMockMvc.perform(post("/api/personal-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalDataDTO)))
            .andExpect(status().isCreated());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getNisn()).isEqualTo(DEFAULT_NISN);
        assertThat(testPersonalData.getMotherName()).isEqualTo(DEFAULT_MOTHER_NAME);
        assertThat(testPersonalData.getFatherName()).isEqualTo(DEFAULT_FATHER_NAME);
        assertThat(testPersonalData.getFatherDob()).isEqualTo(DEFAULT_FATHER_DOB);
        assertThat(testPersonalData.getMotherDob()).isEqualTo(DEFAULT_MOTHER_DOB);
        assertThat(testPersonalData.getHasPaud()).isEqualTo(DEFAULT_HAS_PAUD);
        assertThat(testPersonalData.getHasTk()).isEqualTo(DEFAULT_HAS_TK);

        // Validate the PersonalData in Elasticsearch
        PersonalData personalDataEs = personalDataSearchRepository.findOne(testPersonalData.getIdPersonalData());
        assertThat(personalDataEs).isEqualToComparingFieldByField(testPersonalData);
        keyEntity = testPersonalData.getIdPersonalData();
    }

    @Test
    @Transactional
    public void createPersonalDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personalDataRepository.findAll().size();

        // Create the PersonalData with an existing ID
        personalData.setIdPersonalData(keyEntity);
        PersonalDataDTO personalDataDTO = personalDataMapper.toDto(personalData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDataMockMvc.perform(post("/api/personal-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        // Get all the personalDataList
        restPersonalDataMockMvc.perform(get("/api/personal-data?sort=idPersonalData,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPersonalData").value(hasItem(personalData.getIdPersonalData().toString())))
            .andExpect(jsonPath("$.[*].nisn").value(hasItem(DEFAULT_NISN.toString())))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].fatherDob").value(hasItem(DEFAULT_FATHER_DOB.toString())))
            .andExpect(jsonPath("$.[*].motherDob").value(hasItem(DEFAULT_MOTHER_DOB.toString())))
            .andExpect(jsonPath("$.[*].hasPaud").value(hasItem(DEFAULT_HAS_PAUD)))
            .andExpect(jsonPath("$.[*].hasTk").value(hasItem(DEFAULT_HAS_TK)));
    }

    @Test
    @Transactional
    public void getPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        // Get the personalData
        restPersonalDataMockMvc.perform(get("/api/personal-data/{id}", personalData.getIdPersonalData()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPersonalData").value(personalData.getIdPersonalData().toString()))
            .andExpect(jsonPath("$.nisn").value(DEFAULT_NISN.toString()))
            .andExpect(jsonPath("$.motherName").value(DEFAULT_MOTHER_NAME.toString()))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME.toString()))
            .andExpect(jsonPath("$.fatherDob").value(DEFAULT_FATHER_DOB.toString()))
            .andExpect(jsonPath("$.motherDob").value(DEFAULT_MOTHER_DOB.toString()))
            .andExpect(jsonPath("$.hasPaud").value(DEFAULT_HAS_PAUD))
            .andExpect(jsonPath("$.hasTk").value(DEFAULT_HAS_TK));
    }

    @Test
    @Transactional
    public void getNonExistingPersonalData() throws Exception {
        // Get the personalData
        restPersonalDataMockMvc.perform(get("/api/personal-data/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);
        personalDataSearchRepository.save(personalData);
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();

        // Update the personalData
        PersonalData updatedPersonalData = personalDataRepository.findOne(personalData.getIdPersonalData());
        updatedPersonalData
            .nisn(UPDATED_NISN)
            .motherName(UPDATED_MOTHER_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .fatherDob(UPDATED_FATHER_DOB)
            .motherDob(UPDATED_MOTHER_DOB)
            .hasPaud(UPDATED_HAS_PAUD)
            .hasTk(UPDATED_HAS_TK);
        PersonalDataDTO personalDataDTO = personalDataMapper.toDto(updatedPersonalData);

        restPersonalDataMockMvc.perform(put("/api/personal-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalDataDTO)))
            .andExpect(status().isOk());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getNisn()).isEqualTo(UPDATED_NISN);
        assertThat(testPersonalData.getMotherName()).isEqualTo(UPDATED_MOTHER_NAME);
        assertThat(testPersonalData.getFatherName()).isEqualTo(UPDATED_FATHER_NAME);
        assertThat(testPersonalData.getFatherDob()).isEqualTo(UPDATED_FATHER_DOB);
        assertThat(testPersonalData.getMotherDob()).isEqualTo(UPDATED_MOTHER_DOB);
        assertThat(testPersonalData.getHasPaud()).isEqualTo(UPDATED_HAS_PAUD);
        assertThat(testPersonalData.getHasTk()).isEqualTo(UPDATED_HAS_TK);

        // Validate the PersonalData in Elasticsearch
        PersonalData personalDataEs = personalDataSearchRepository.findOne(testPersonalData.getIdPersonalData());
        assertThat(personalDataEs).isEqualToComparingFieldByField(testPersonalData);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();

        // Create the PersonalData
        PersonalDataDTO personalDataDTO = personalDataMapper.toDto(personalData);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonalDataMockMvc.perform(put("/api/personal-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalDataDTO)))
            .andExpect(status().isCreated());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);
        personalDataSearchRepository.save(personalData);
        int databaseSizeBeforeDelete = personalDataRepository.findAll().size();

        // Get the personalData
        restPersonalDataMockMvc.perform(delete("/api/personal-data/{id}", personalData.getIdPersonalData())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean personalDataExistsInEs = personalDataSearchRepository.exists(personalData.getIdPersonalData());
        assertThat(personalDataExistsInEs).isFalse();

        // Validate the database is empty
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);
        personalDataSearchRepository.save(personalData);

        // Search the personalData
        restPersonalDataMockMvc.perform(get("/api/_search/personal-data?query=idPersonalData:" + personalData.getIdPersonalData()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPersonalData").value(hasItem(personalData.getIdPersonalData().toString())))
            .andExpect(jsonPath("$.[*].nisn").value(hasItem(DEFAULT_NISN.toString())))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].fatherDob").value(hasItem(DEFAULT_FATHER_DOB.toString())))
            .andExpect(jsonPath("$.[*].motherDob").value(hasItem(DEFAULT_MOTHER_DOB.toString())))
            .andExpect(jsonPath("$.[*].hasPaud").value(hasItem(DEFAULT_HAS_PAUD)))
            .andExpect(jsonPath("$.[*].hasTk").value(hasItem(DEFAULT_HAS_TK)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalData.class);
        /*
        PersonalData personalData1 = new PersonalData();
<<<<<<< HEAD
        personalData1.setIdPersonalData(1L);
=======
        personalData1.setId(1L);
>>>>>>> Branch_v4.5.4
        PersonalData personalData2 = new PersonalData();
        personalData2.setIdPersonalData(personalData1.getId());
        assertThat(personalData1).isEqualTo(personalData2);
<<<<<<< HEAD
        personalData2.setIdPersonalData(2L);
=======
        personalData2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(personalData1).isNotEqualTo(personalData2);
        personalData1.setIdPersonalData(null);
        assertThat(personalData1).isNotEqualTo(personalData2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalDataDTO.class);
        /*
        PersonalDataDTO personalDataDTO1 = new PersonalDataDTO();
<<<<<<< HEAD
        personalDataDTO1.setIdPersonalData(1L);
=======
        personalDataDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PersonalDataDTO personalDataDTO2 = new PersonalDataDTO();
        assertThat(personalDataDTO1).isNotEqualTo(personalDataDTO2);
        personalDataDTO2.setIdPersonalData(personalDataDTO1.getIdPersonalData());
        assertThat(personalDataDTO1).isEqualTo(personalDataDTO2);
<<<<<<< HEAD
        personalDataDTO2.setIdPersonalData(2L);
=======
        personalDataDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(personalDataDTO1).isNotEqualTo(personalDataDTO2);
        personalDataDTO1.setIdPersonalData(null);
        assertThat(personalDataDTO1).isNotEqualTo(personalDataDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(personalDataMapper.fromidPersonalData(42L).getidPersonalData()).isEqualTo(42);
        //assertThat(personalDataMapper.fromIdPersonalData(null)).isNull();
    }
}
