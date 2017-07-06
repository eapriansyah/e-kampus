package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ContactMechanism;
import id.eara.repository.ContactMechanismRepository;
import id.eara.service.ContactMechanismService;
import id.eara.repository.search.ContactMechanismSearchRepository;
import id.eara.service.dto.ContactMechanismDTO;
import id.eara.service.mapper.ContactMechanismMapper;
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
 * Test class for the ContactMechanismResource REST controller.
 *
 * @see ContactMechanismResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ContactMechanismResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ContactMechanismRepository contactMechanismRepository;

    @Autowired
    private ContactMechanismMapper contactMechanismMapper;

    @Autowired
    private ContactMechanismService contactMechanismService;

    @Autowired
    private ContactMechanismSearchRepository contactMechanismSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactMechanismMockMvc;

    private ContactMechanism contactMechanism;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactMechanismResource contactMechanismResource = new ContactMechanismResource(contactMechanismService);
        this.restContactMechanismMockMvc = MockMvcBuilders.standaloneSetup(contactMechanismResource)
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
    public static ContactMechanism createEntity(EntityManager em) {
        ContactMechanism contactMechanism = new ContactMechanism()
            .description(DEFAULT_DESCRIPTION);
        return contactMechanism;
    }

    @Before
    public void initTest() {
        contactMechanismSearchRepository.deleteAll();
        contactMechanism = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactMechanism() throws Exception {
        int databaseSizeBeforeCreate = contactMechanismRepository.findAll().size();

        // Create the ContactMechanism
        ContactMechanismDTO contactMechanismDTO = contactMechanismMapper.toDto(contactMechanism);

        restContactMechanismMockMvc.perform(post("/api/contact-mechanisms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactMechanism in the database
        List<ContactMechanism> contactMechanismList = contactMechanismRepository.findAll();
        assertThat(contactMechanismList).hasSize(databaseSizeBeforeCreate + 1);
        ContactMechanism testContactMechanism = contactMechanismList.get(contactMechanismList.size() - 1);
        assertThat(testContactMechanism.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ContactMechanism in Elasticsearch
        ContactMechanism contactMechanismEs = contactMechanismSearchRepository.findOne(testContactMechanism.getIdContact());
        assertThat(contactMechanismEs).isEqualToComparingFieldByField(testContactMechanism);
        keyEntity = testContactMechanism.getIdContact();
    }

    @Test
    @Transactional
    public void createContactMechanismWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactMechanismRepository.findAll().size();

        // Create the ContactMechanism with an existing ID
        contactMechanism.setIdContact(keyEntity);
        ContactMechanismDTO contactMechanismDTO = contactMechanismMapper.toDto(contactMechanism);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMechanismMockMvc.perform(post("/api/contact-mechanisms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ContactMechanism> contactMechanismList = contactMechanismRepository.findAll();
        assertThat(contactMechanismList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContactMechanisms() throws Exception {
        // Initialize the database
        contactMechanismRepository.saveAndFlush(contactMechanism);

        // Get all the contactMechanismList
        restContactMechanismMockMvc.perform(get("/api/contact-mechanisms?sort=idContact,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(contactMechanism.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getContactMechanism() throws Exception {
        // Initialize the database
        contactMechanismRepository.saveAndFlush(contactMechanism);

        // Get the contactMechanism
        restContactMechanismMockMvc.perform(get("/api/contact-mechanisms/{id}", contactMechanism.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idContact").value(contactMechanism.getIdContact().toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactMechanism() throws Exception {
        // Get the contactMechanism
        restContactMechanismMockMvc.perform(get("/api/contact-mechanisms/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactMechanism() throws Exception {
        // Initialize the database
        contactMechanismRepository.saveAndFlush(contactMechanism);
        contactMechanismSearchRepository.save(contactMechanism);
        int databaseSizeBeforeUpdate = contactMechanismRepository.findAll().size();

        // Update the contactMechanism
        ContactMechanism updatedContactMechanism = contactMechanismRepository.findOne(contactMechanism.getIdContact());
        updatedContactMechanism
            .description(UPDATED_DESCRIPTION);
        ContactMechanismDTO contactMechanismDTO = contactMechanismMapper.toDto(updatedContactMechanism);

        restContactMechanismMockMvc.perform(put("/api/contact-mechanisms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismDTO)))
            .andExpect(status().isOk());

        // Validate the ContactMechanism in the database
        List<ContactMechanism> contactMechanismList = contactMechanismRepository.findAll();
        assertThat(contactMechanismList).hasSize(databaseSizeBeforeUpdate);
        ContactMechanism testContactMechanism = contactMechanismList.get(contactMechanismList.size() - 1);
        assertThat(testContactMechanism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ContactMechanism in Elasticsearch
        ContactMechanism contactMechanismEs = contactMechanismSearchRepository.findOne(testContactMechanism.getIdContact());
        assertThat(contactMechanismEs).isEqualToComparingFieldByField(testContactMechanism);
    }

    @Test
    @Transactional
    public void updateNonExistingContactMechanism() throws Exception {
        int databaseSizeBeforeUpdate = contactMechanismRepository.findAll().size();

        // Create the ContactMechanism
        ContactMechanismDTO contactMechanismDTO = contactMechanismMapper.toDto(contactMechanism);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactMechanismMockMvc.perform(put("/api/contact-mechanisms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactMechanismDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactMechanism in the database
        List<ContactMechanism> contactMechanismList = contactMechanismRepository.findAll();
        assertThat(contactMechanismList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContactMechanism() throws Exception {
        // Initialize the database
        contactMechanismRepository.saveAndFlush(contactMechanism);
        contactMechanismSearchRepository.save(contactMechanism);
        int databaseSizeBeforeDelete = contactMechanismRepository.findAll().size();

        // Get the contactMechanism
        restContactMechanismMockMvc.perform(delete("/api/contact-mechanisms/{id}", contactMechanism.getIdContact())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contactMechanismExistsInEs = contactMechanismSearchRepository.exists(contactMechanism.getIdContact());
        assertThat(contactMechanismExistsInEs).isFalse();

        // Validate the database is empty
        List<ContactMechanism> contactMechanismList = contactMechanismRepository.findAll();
        assertThat(contactMechanismList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContactMechanism() throws Exception {
        // Initialize the database
        contactMechanismRepository.saveAndFlush(contactMechanism);
        contactMechanismSearchRepository.save(contactMechanism);

        // Search the contactMechanism
        restContactMechanismMockMvc.perform(get("/api/_search/contact-mechanisms?query=idContact:" + contactMechanism.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(contactMechanism.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechanism.class);
        /*
        ContactMechanism contactMechanism1 = new ContactMechanism();
<<<<<<< HEAD
        contactMechanism1.setIdContact(1L);
=======
        contactMechanism1.setId(1L);
>>>>>>> Branch_v4.5.4
        ContactMechanism contactMechanism2 = new ContactMechanism();
        contactMechanism2.setIdContact(contactMechanism1.getId());
        assertThat(contactMechanism1).isEqualTo(contactMechanism2);
<<<<<<< HEAD
        contactMechanism2.setIdContact(2L);
=======
        contactMechanism2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(contactMechanism1).isNotEqualTo(contactMechanism2);
        contactMechanism1.setIdContact(null);
        assertThat(contactMechanism1).isNotEqualTo(contactMechanism2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactMechanismDTO.class);
        /*
        ContactMechanismDTO contactMechanismDTO1 = new ContactMechanismDTO();
<<<<<<< HEAD
        contactMechanismDTO1.setIdContact(1L);
=======
        contactMechanismDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ContactMechanismDTO contactMechanismDTO2 = new ContactMechanismDTO();
        assertThat(contactMechanismDTO1).isNotEqualTo(contactMechanismDTO2);
        contactMechanismDTO2.setIdContact(contactMechanismDTO1.getIdContact());
        assertThat(contactMechanismDTO1).isEqualTo(contactMechanismDTO2);
<<<<<<< HEAD
        contactMechanismDTO2.setIdContact(2L);
=======
        contactMechanismDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(contactMechanismDTO1).isNotEqualTo(contactMechanismDTO2);
        contactMechanismDTO1.setIdContact(null);
        assertThat(contactMechanismDTO1).isNotEqualTo(contactMechanismDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(contactMechanismMapper.fromidContact(42L).getidContact()).isEqualTo(42);
        //assertThat(contactMechanismMapper.fromIdContact(null)).isNull();
    }
}
