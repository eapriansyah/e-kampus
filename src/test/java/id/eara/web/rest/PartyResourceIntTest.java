package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Party;
import id.eara.repository.PartyRepository;
import id.eara.service.PartyService;
import id.eara.repository.search.PartySearchRepository;
import id.eara.service.dto.PartyDTO;
import id.eara.service.mapper.PartyMapper;
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
 * Test class for the PartyResource REST controller.
 *
 * @see PartyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PartyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartySearchRepository partySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPartyMockMvc;

    private Party party;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartyResource partyResource = new PartyResource(partyService);
        this.restPartyMockMvc = MockMvcBuilders.standaloneSetup(partyResource)
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
    public static Party createEntity(EntityManager em) {
        Party party = new Party()
            .name(DEFAULT_NAME);
        return party;
    }

    @Before
    public void initTest() {
        partySearchRepository.deleteAll();
        party = createEntity(em);
    }

    @Test
    @Transactional
    public void createParty() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isCreated());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate + 1);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Party in Elasticsearch
        Party partyEs = partySearchRepository.findOne(testParty.getIdParty());
        assertThat(partyEs).isEqualToComparingFieldByField(testParty);
        keyEntity = testParty.getIdParty();
    }

    @Test
    @Transactional
    public void createPartyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // Create the Party with an existing ID
        party.setIdParty(keyEntity);
        PartyDTO partyDTO = partyMapper.toDto(party);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParties() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get all the partyList
        restPartyMockMvc.perform(get("/api/parties?sort=idParty,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idParty").value(hasItem(party.getIdParty().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get the party
        restPartyMockMvc.perform(get("/api/parties/{id}", party.getIdParty()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idParty").value(party.getIdParty().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParty() throws Exception {
        // Get the party
        restPartyMockMvc.perform(get("/api/parties/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);
        partySearchRepository.save(party);
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party
        Party updatedParty = partyRepository.findOne(party.getIdParty());
        updatedParty
            .name(UPDATED_NAME);
        PartyDTO partyDTO = partyMapper.toDto(updatedParty);

        restPartyMockMvc.perform(put("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Party in Elasticsearch
        Party partyEs = partySearchRepository.findOne(testParty.getIdParty());
        assertThat(partyEs).isEqualToComparingFieldByField(testParty);
    }

    @Test
    @Transactional
    public void updateNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(party);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPartyMockMvc.perform(put("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isCreated());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);
        partySearchRepository.save(party);
        int databaseSizeBeforeDelete = partyRepository.findAll().size();

        // Get the party
        restPartyMockMvc.perform(delete("/api/parties/{id}", party.getIdParty())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean partyExistsInEs = partySearchRepository.exists(party.getIdParty());
        assertThat(partyExistsInEs).isFalse();

        // Validate the database is empty
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);
        partySearchRepository.save(party);

        // Search the party
        restPartyMockMvc.perform(get("/api/_search/parties?query=idParty:" + party.getIdParty()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idParty").value(hasItem(party.getIdParty().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Party.class);
        /*
        Party party1 = new Party();
<<<<<<< HEAD
        party1.setIdParty(1L);
=======
        party1.setId(1L);
>>>>>>> Branch_v4.5.4
        Party party2 = new Party();
        party2.setIdParty(party1.getId());
        assertThat(party1).isEqualTo(party2);
<<<<<<< HEAD
        party2.setIdParty(2L);
=======
        party2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(party1).isNotEqualTo(party2);
        party1.setIdParty(null);
        assertThat(party1).isNotEqualTo(party2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyDTO.class);
        /*
        PartyDTO partyDTO1 = new PartyDTO();
<<<<<<< HEAD
        partyDTO1.setIdParty(1L);
=======
        partyDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PartyDTO partyDTO2 = new PartyDTO();
        assertThat(partyDTO1).isNotEqualTo(partyDTO2);
        partyDTO2.setIdParty(partyDTO1.getIdParty());
        assertThat(partyDTO1).isEqualTo(partyDTO2);
<<<<<<< HEAD
        partyDTO2.setIdParty(2L);
=======
        partyDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(partyDTO1).isNotEqualTo(partyDTO2);
        partyDTO1.setIdParty(null);
        assertThat(partyDTO1).isNotEqualTo(partyDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(partyMapper.fromidParty(42L).getidParty()).isEqualTo(42);
        //assertThat(partyMapper.fromIdParty(null)).isNull();
    }
}
