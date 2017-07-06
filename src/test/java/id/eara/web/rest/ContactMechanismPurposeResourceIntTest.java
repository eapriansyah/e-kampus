package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ContactMechanismPurpose;
import id.eara.repository.ContactMechanismPurposeRepository;
import id.eara.service.ContactMechanismPurposeService;
import id.eara.repository.search.ContactMechanismPurposeSearchRepository;
import id.eara.service.dto.ContactMechanismPurposeDTO;
import id.eara.service.mapper.ContactMechanismPurposeMapper;
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
 * Test class for the ContactMechanismPurposeResource REST controller.
 *
 * @see ContactMechanismPurposeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ContactMechanismPurposeResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_THRU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_THRU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ContactMechanismPurposeRepository contactMechanismPurposeRepository;

    @Autowired
    private ContactMechanismPurposeMapper contactMechanismPurposeMapper;

    @Autowired
    private ContactMechanismPurposeService contactMechanismPurposeService;

    @Autowired
    private ContactMechanismPurposeSearchRepository contactMechanismPurposeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactMechanismPurposeMockMvc;

    private ContactMechanismPurpose contactMechanismPurpose;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactMechanismPurposeResource contactMechanismPurposeResource = new ContactMechanismPurposeResource(contactMechanismPurposeService);
        this.restContactMechanismPurposeMockMvc = MockMvcBuilders.standaloneSetup(contactMechanismPurposeResource)
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
    public static ContactMechanismPurpose createEntity(EntityManager em) {
        ContactMechanismPurpose contactMechanismPurpose = new ContactMechanismPurpose()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateThru(DEFAULT_DATE_THRU);
        return contactMechanismPurpose;
    }

    @Before
    public void initTest() {
        contactMechanismPurposeSearchRepository.deleteAll();
        contactMechanismPurpose = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactMechanismPurpose() throws Exception {
        int databaseSizeBeforeCreate = contactMechanismPurposeRepository.findAll().size();

        // Create the ContactMechanismPurpose
        ContactMechanismPurposeDTO contactMechanismPurposeDTO = contactMechanismPurposeMapper.toDto(contactMechanismPurpose);

        restContactMechanismPurposeMockMvc.perform(post("/api/contact-mechanism-purposes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismPurposeDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactMechanismPurpose in the database
        List<ContactMechanismPurpose> contactMechanismPurposeList = contactMechanismPurposeRepository.findAll();
        assertThat(contactMechanismPurposeList).hasSize(databaseSizeBeforeCreate + 1);
        ContactMechanismPurpose testContactMechanismPurpose = contactMechanismPurposeList.get(contactMechanismPurposeList.size() - 1);
        assertThat(testContactMechanismPurpose.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testContactMechanismPurpose.getDateThru()).isEqualTo(DEFAULT_DATE_THRU);

        // Validate the ContactMechanismPurpose in Elasticsearch
        ContactMechanismPurpose contactMechanismPurposeEs = contactMechanismPurposeSearchRepository.findOne(testContactMechanismPurpose.getIdContactMechPurpose());
        assertThat(contactMechanismPurposeEs).isEqualToComparingFieldByField(testContactMechanismPurpose);
        keyEntity = testContactMechanismPurpose.getIdContactMechPurpose();
    }

    @Test
    @Transactional
    public void createContactMechanismPurposeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactMechanismPurposeRepository.findAll().size();

        // Create the ContactMechanismPurpose with an existing ID
        contactMechanismPurpose.setIdContactMechPurpose(keyEntity);
        ContactMechanismPurposeDTO contactMechanismPurposeDTO = contactMechanismPurposeMapper.toDto(contactMechanismPurpose);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMechanismPurposeMockMvc.perform(post("/api/contact-mechanism-purposes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismPurposeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ContactMechanismPurpose> contactMechanismPurposeList = contactMechanismPurposeRepository.findAll();
        assertThat(contactMechanismPurposeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContactMechanismPurposes() throws Exception {
        // Initialize the database
        contactMechanismPurposeRepository.saveAndFlush(contactMechanismPurpose);

        // Get all the contactMechanismPurposeList
        restContactMechanismPurposeMockMvc.perform(get("/api/contact-mechanism-purposes?sort=idContactMechPurpose,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContactMechPurpose").value(hasItem(contactMechanismPurpose.getIdContactMechPurpose().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void getContactMechanismPurpose() throws Exception {
        // Initialize the database
        contactMechanismPurposeRepository.saveAndFlush(contactMechanismPurpose);

        // Get the contactMechanismPurpose
        restContactMechanismPurposeMockMvc.perform(get("/api/contact-mechanism-purposes/{id}", contactMechanismPurpose.getIdContactMechPurpose()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idContactMechPurpose").value(contactMechanismPurpose.getIdContactMechPurpose().toString()))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateThru").value(sameInstant(DEFAULT_DATE_THRU)));
    }

    @Test
    @Transactional
    public void getNonExistingContactMechanismPurpose() throws Exception {
        // Get the contactMechanismPurpose
        restContactMechanismPurposeMockMvc.perform(get("/api/contact-mechanism-purposes/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactMechanismPurpose() throws Exception {
        // Initialize the database
        contactMechanismPurposeRepository.saveAndFlush(contactMechanismPurpose);
        contactMechanismPurposeSearchRepository.save(contactMechanismPurpose);
        int databaseSizeBeforeUpdate = contactMechanismPurposeRepository.findAll().size();

        // Update the contactMechanismPurpose
        ContactMechanismPurpose updatedContactMechanismPurpose = contactMechanismPurposeRepository.findOne(contactMechanismPurpose.getIdContactMechPurpose());
        updatedContactMechanismPurpose
            .dateFrom(UPDATED_DATE_FROM)
            .dateThru(UPDATED_DATE_THRU);
        ContactMechanismPurposeDTO contactMechanismPurposeDTO = contactMechanismPurposeMapper.toDto(updatedContactMechanismPurpose);

        restContactMechanismPurposeMockMvc.perform(put("/api/contact-mechanism-purposes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismPurposeDTO)))
            .andExpect(status().isOk());

        // Validate the ContactMechanismPurpose in the database
        List<ContactMechanismPurpose> contactMechanismPurposeList = contactMechanismPurposeRepository.findAll();
        assertThat(contactMechanismPurposeList).hasSize(databaseSizeBeforeUpdate);
        ContactMechanismPurpose testContactMechanismPurpose = contactMechanismPurposeList.get(contactMechanismPurposeList.size() - 1);
        assertThat(testContactMechanismPurpose.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testContactMechanismPurpose.getDateThru()).isEqualTo(UPDATED_DATE_THRU);

        // Validate the ContactMechanismPurpose in Elasticsearch
        ContactMechanismPurpose contactMechanismPurposeEs = contactMechanismPurposeSearchRepository.findOne(testContactMechanismPurpose.getIdContactMechPurpose());
        assertThat(contactMechanismPurposeEs).isEqualToComparingFieldByField(testContactMechanismPurpose);
    }

    @Test
    @Transactional
    public void updateNonExistingContactMechanismPurpose() throws Exception {
        int databaseSizeBeforeUpdate = contactMechanismPurposeRepository.findAll().size();

        // Create the ContactMechanismPurpose
        ContactMechanismPurposeDTO contactMechanismPurposeDTO = contactMechanismPurposeMapper.toDto(contactMechanismPurpose);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactMechanismPurposeMockMvc.perform(put("/api/contact-mechanism-purposes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismPurposeDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactMechanismPurpose in the database
        List<ContactMechanismPurpose> contactMechanismPurposeList = contactMechanismPurposeRepository.findAll();
        assertThat(contactMechanismPurposeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactMechanismPurpose() throws Exception {
        // Initialize the database
        contactMechanismPurposeRepository.saveAndFlush(contactMechanismPurpose);
        contactMechanismPurposeSearchRepository.save(contactMechanismPurpose);
        int databaseSizeBeforeDelete = contactMechanismPurposeRepository.findAll().size();

        // Get the contactMechanismPurpose
        restContactMechanismPurposeMockMvc.perform(delete("/api/contact-mechanism-purposes/{id}", contactMechanismPurpose.getIdContactMechPurpose())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contactMechanismPurposeExistsInEs = contactMechanismPurposeSearchRepository.exists(contactMechanismPurpose.getIdContactMechPurpose());
        assertThat(contactMechanismPurposeExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactMechanismPurpose> contactMechanismPurposeList = contactMechanismPurposeRepository.findAll();
        assertThat(contactMechanismPurposeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactMechanismPurpose() throws Exception {
        // Initialize the database
        contactMechanismPurposeRepository.saveAndFlush(contactMechanismPurpose);
        contactMechanismPurposeSearchRepository.save(contactMechanismPurpose);

        // Search the contactMechanismPurpose
        restContactMechanismPurposeMockMvc.perform(get("/api/_search/contact-mechanism-purposes?query=idContactMechPurpose:" + contactMechanismPurpose.getIdContactMechPurpose()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContactMechPurpose").value(hasItem(contactMechanismPurpose.getIdContactMechPurpose().toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechanismPurpose.class);
        /*
        ContactMechanismPurpose contactMechanismPurpose1 = new ContactMechanismPurpose();
<<<<<<< HEAD
        contactMechanismPurpose1.setIdContactMechPurpose(1L);
=======
        contactMechanismPurpose1.setId(1L);
>>>>>>> Branch_v4.5.4
        ContactMechanismPurpose contactMechanismPurpose2 = new ContactMechanismPurpose();
        contactMechanismPurpose2.setIdContactMechPurpose(contactMechanismPurpose1.getId());
        assertThat(contactMechanismPurpose1).isEqualTo(contactMechanismPurpose2);
<<<<<<< HEAD
        contactMechanismPurpose2.setIdContactMechPurpose(2L);
=======
        contactMechanismPurpose2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(contactMechanismPurpose1).isNotEqualTo(contactMechanismPurpose2);
        contactMechanismPurpose1.setIdContactMechPurpose(null);
        assertThat(contactMechanismPurpose1).isNotEqualTo(contactMechanismPurpose2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechanismPurposeDTO.class);
        /*
        ContactMechanismPurposeDTO contactMechanismPurposeDTO1 = new ContactMechanismPurposeDTO();
<<<<<<< HEAD
        contactMechanismPurposeDTO1.setIdContactMechPurpose(1L);
=======
        contactMechanismPurposeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ContactMechanismPurposeDTO contactMechanismPurposeDTO2 = new ContactMechanismPurposeDTO();
        assertThat(contactMechanismPurposeDTO1).isNotEqualTo(contactMechanismPurposeDTO2);
        contactMechanismPurposeDTO2.setIdContactMechPurpose(contactMechanismPurposeDTO1.getIdContactMechPurpose());
        assertThat(contactMechanismPurposeDTO1).isEqualTo(contactMechanismPurposeDTO2);
<<<<<<< HEAD
        contactMechanismPurposeDTO2.setIdContactMechPurpose(2L);
=======
        contactMechanismPurposeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(contactMechanismPurposeDTO1).isNotEqualTo(contactMechanismPurposeDTO2);
        contactMechanismPurposeDTO1.setIdContactMechPurpose(null);
        assertThat(contactMechanismPurposeDTO1).isNotEqualTo(contactMechanismPurposeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(contactMechanismPurposeMapper.fromidContactMechPurpose(42L).getidContactMechPurpose()).isEqualTo(42);
        //assertThat(contactMechanismPurposeMapper.fromIdContactMechPurpose(null)).isNull();
    }
}
