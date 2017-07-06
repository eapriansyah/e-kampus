package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.TelecomunicationNumber;
import id.eara.repository.TelecomunicationNumberRepository;
import id.eara.service.TelecomunicationNumberService;
import id.eara.repository.search.TelecomunicationNumberSearchRepository;
import id.eara.service.dto.TelecomunicationNumberDTO;
import id.eara.service.mapper.TelecomunicationNumberMapper;
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
 * Test class for the TelecomunicationNumberResource REST controller.
 *
 * @see TelecomunicationNumberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class TelecomunicationNumberResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    @Autowired
    private TelecomunicationNumberRepository telecomunicationNumberRepository;

    @Autowired
    private TelecomunicationNumberMapper telecomunicationNumberMapper;

    @Autowired
    private TelecomunicationNumberService telecomunicationNumberService;

    @Autowired
    private TelecomunicationNumberSearchRepository telecomunicationNumberSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelecomunicationNumberMockMvc;

    private TelecomunicationNumber telecomunicationNumber;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelecomunicationNumberResource telecomunicationNumberResource = new TelecomunicationNumberResource(telecomunicationNumberService);
        this.restTelecomunicationNumberMockMvc = MockMvcBuilders.standaloneSetup(telecomunicationNumberResource)
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
    public static TelecomunicationNumber createEntity(EntityManager em) {
        TelecomunicationNumber telecomunicationNumber = new TelecomunicationNumber()
            .number(DEFAULT_NUMBER);
        return telecomunicationNumber;
    }

    @Before
    public void initTest() {
        telecomunicationNumberSearchRepository.deleteAll();
        telecomunicationNumber = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelecomunicationNumber() throws Exception {
        int databaseSizeBeforeCreate = telecomunicationNumberRepository.findAll().size();

        // Create the TelecomunicationNumber
        TelecomunicationNumberDTO telecomunicationNumberDTO = telecomunicationNumberMapper.toDto(telecomunicationNumber);

        restTelecomunicationNumberMockMvc.perform(post("/api/telecomunication-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telecomunicationNumberDTO)))
            .andExpect(status().isCreated());

        // Validate the TelecomunicationNumber in the database
        List<TelecomunicationNumber> telecomunicationNumberList = telecomunicationNumberRepository.findAll();
        assertThat(telecomunicationNumberList).hasSize(databaseSizeBeforeCreate + 1);
        TelecomunicationNumber testTelecomunicationNumber = telecomunicationNumberList.get(telecomunicationNumberList.size() - 1);
        assertThat(testTelecomunicationNumber.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the TelecomunicationNumber in Elasticsearch
        TelecomunicationNumber telecomunicationNumberEs = telecomunicationNumberSearchRepository.findOne(testTelecomunicationNumber.getIdContact());
        assertThat(telecomunicationNumberEs).isEqualToComparingFieldByField(testTelecomunicationNumber);
        keyEntity = testTelecomunicationNumber.getIdContact();
    }

    @Test
    @Transactional
    public void createTelecomunicationNumberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telecomunicationNumberRepository.findAll().size();

        // Create the TelecomunicationNumber with an existing ID
        telecomunicationNumber.setIdContact(keyEntity);
        TelecomunicationNumberDTO telecomunicationNumberDTO = telecomunicationNumberMapper.toDto(telecomunicationNumber);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelecomunicationNumberMockMvc.perform(post("/api/telecomunication-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telecomunicationNumberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TelecomunicationNumber> telecomunicationNumberList = telecomunicationNumberRepository.findAll();
        assertThat(telecomunicationNumberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTelecomunicationNumbers() throws Exception {
        // Initialize the database
        telecomunicationNumberRepository.saveAndFlush(telecomunicationNumber);

        // Get all the telecomunicationNumberList
        restTelecomunicationNumberMockMvc.perform(get("/api/telecomunication-numbers?sort=idContact,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(telecomunicationNumber.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getTelecomunicationNumber() throws Exception {
        // Initialize the database
        telecomunicationNumberRepository.saveAndFlush(telecomunicationNumber);

        // Get the telecomunicationNumber
        restTelecomunicationNumberMockMvc.perform(get("/api/telecomunication-numbers/{id}", telecomunicationNumber.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idContact").value(telecomunicationNumber.getIdContact().toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTelecomunicationNumber() throws Exception {
        // Get the telecomunicationNumber
        restTelecomunicationNumberMockMvc.perform(get("/api/telecomunication-numbers/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelecomunicationNumber() throws Exception {
        // Initialize the database
        telecomunicationNumberRepository.saveAndFlush(telecomunicationNumber);
        telecomunicationNumberSearchRepository.save(telecomunicationNumber);
        int databaseSizeBeforeUpdate = telecomunicationNumberRepository.findAll().size();

        // Update the telecomunicationNumber
        TelecomunicationNumber updatedTelecomunicationNumber = telecomunicationNumberRepository.findOne(telecomunicationNumber.getIdContact());
        updatedTelecomunicationNumber
            .number(UPDATED_NUMBER);
        TelecomunicationNumberDTO telecomunicationNumberDTO = telecomunicationNumberMapper.toDto(updatedTelecomunicationNumber);

        restTelecomunicationNumberMockMvc.perform(put("/api/telecomunication-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telecomunicationNumberDTO)))
            .andExpect(status().isOk());

        // Validate the TelecomunicationNumber in the database
        List<TelecomunicationNumber> telecomunicationNumberList = telecomunicationNumberRepository.findAll();
        assertThat(telecomunicationNumberList).hasSize(databaseSizeBeforeUpdate);
        TelecomunicationNumber testTelecomunicationNumber = telecomunicationNumberList.get(telecomunicationNumberList.size() - 1);
        assertThat(testTelecomunicationNumber.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the TelecomunicationNumber in Elasticsearch
        TelecomunicationNumber telecomunicationNumberEs = telecomunicationNumberSearchRepository.findOne(testTelecomunicationNumber.getIdContact());
        assertThat(telecomunicationNumberEs).isEqualToComparingFieldByField(testTelecomunicationNumber);
    }

    @Test
    @Transactional
    public void updateNonExistingTelecomunicationNumber() throws Exception {
        int databaseSizeBeforeUpdate = telecomunicationNumberRepository.findAll().size();

        // Create the TelecomunicationNumber
        TelecomunicationNumberDTO telecomunicationNumberDTO = telecomunicationNumberMapper.toDto(telecomunicationNumber);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTelecomunicationNumberMockMvc.perform(put("/api/telecomunication-numbers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telecomunicationNumberDTO)))
            .andExpect(status().isCreated());

        // Validate the TelecomunicationNumber in the database
        List<TelecomunicationNumber> telecomunicationNumberList = telecomunicationNumberRepository.findAll();
        assertThat(telecomunicationNumberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTelecomunicationNumber() throws Exception {
        // Initialize the database
        telecomunicationNumberRepository.saveAndFlush(telecomunicationNumber);
        telecomunicationNumberSearchRepository.save(telecomunicationNumber);
        int databaseSizeBeforeDelete = telecomunicationNumberRepository.findAll().size();

        // Get the telecomunicationNumber
        restTelecomunicationNumberMockMvc.perform(delete("/api/telecomunication-numbers/{id}", telecomunicationNumber.getIdContact())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean telecomunicationNumberExistsInEs = telecomunicationNumberSearchRepository.exists(telecomunicationNumber.getIdContact());
        assertThat(telecomunicationNumberExistsInEs).isFalse();

        // Validate the database is empty
        List<TelecomunicationNumber> telecomunicationNumberList = telecomunicationNumberRepository.findAll();
        assertThat(telecomunicationNumberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTelecomunicationNumber() throws Exception {
        // Initialize the database
        telecomunicationNumberRepository.saveAndFlush(telecomunicationNumber);
        telecomunicationNumberSearchRepository.save(telecomunicationNumber);

        // Search the telecomunicationNumber
        restTelecomunicationNumberMockMvc.perform(get("/api/_search/telecomunication-numbers?query=idContact:" + telecomunicationNumber.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(telecomunicationNumber.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelecomunicationNumber.class);
        /*
        TelecomunicationNumber telecomunicationNumber1 = new TelecomunicationNumber();
<<<<<<< HEAD
        telecomunicationNumber1.setIdContact(1L);
=======
        telecomunicationNumber1.setId(1L);
>>>>>>> Branch_v4.5.4
        TelecomunicationNumber telecomunicationNumber2 = new TelecomunicationNumber();
        telecomunicationNumber2.setIdContact(telecomunicationNumber1.getId());
        assertThat(telecomunicationNumber1).isEqualTo(telecomunicationNumber2);
<<<<<<< HEAD
        telecomunicationNumber2.setIdContact(2L);
=======
        telecomunicationNumber2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(telecomunicationNumber1).isNotEqualTo(telecomunicationNumber2);
        telecomunicationNumber1.setIdContact(null);
        assertThat(telecomunicationNumber1).isNotEqualTo(telecomunicationNumber2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelecomunicationNumberDTO.class);
        /*
        TelecomunicationNumberDTO telecomunicationNumberDTO1 = new TelecomunicationNumberDTO();
<<<<<<< HEAD
        telecomunicationNumberDTO1.setIdContact(1L);
=======
        telecomunicationNumberDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        TelecomunicationNumberDTO telecomunicationNumberDTO2 = new TelecomunicationNumberDTO();
        assertThat(telecomunicationNumberDTO1).isNotEqualTo(telecomunicationNumberDTO2);
        telecomunicationNumberDTO2.setIdContact(telecomunicationNumberDTO1.getIdContact());
        assertThat(telecomunicationNumberDTO1).isEqualTo(telecomunicationNumberDTO2);
<<<<<<< HEAD
        telecomunicationNumberDTO2.setIdContact(2L);
=======
        telecomunicationNumberDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(telecomunicationNumberDTO1).isNotEqualTo(telecomunicationNumberDTO2);
        telecomunicationNumberDTO1.setIdContact(null);
        assertThat(telecomunicationNumberDTO1).isNotEqualTo(telecomunicationNumberDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(telecomunicationNumberMapper.fromidContact(42L).getidContact()).isEqualTo(42);
        //assertThat(telecomunicationNumberMapper.fromIdContact(null)).isNull();
    }
}
