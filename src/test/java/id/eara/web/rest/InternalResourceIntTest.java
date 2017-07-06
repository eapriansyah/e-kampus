package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Internal;
import id.eara.repository.InternalRepository;
import id.eara.service.InternalService;
import id.eara.repository.search.InternalSearchRepository;
import id.eara.service.dto.InternalDTO;
import id.eara.service.mapper.InternalMapper;
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
 * Test class for the InternalResource REST controller.
 *
 * @see InternalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class InternalResourceIntTest {

    private static final String DEFAULT_ID_INTERNAL = "AAAAAAAAAA";
    private static final String UPDATED_ID_INTERNAL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_ROLE_TYPE = 1;
    private static final Integer UPDATED_ID_ROLE_TYPE = 2;

    private static final Integer DEFAULT_ID_STATUS_TYPE = 1;
    private static final Integer UPDATED_ID_STATUS_TYPE = 2;

    @Autowired
    private InternalRepository internalRepository;

    @Autowired
    private InternalMapper internalMapper;

    @Autowired
    private InternalService internalService;

    @Autowired
    private InternalSearchRepository internalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInternalMockMvc;

    private Internal internal;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternalResource internalResource = new InternalResource(internalService);
        this.restInternalMockMvc = MockMvcBuilders.standaloneSetup(internalResource)
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
    public static Internal createEntity(EntityManager em) {
        Internal internal = new Internal()
            .idInternal(DEFAULT_ID_INTERNAL)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .idRoleType(DEFAULT_ID_ROLE_TYPE)
            .idStatusType(DEFAULT_ID_STATUS_TYPE);
        return internal;
    }

    @Before
    public void initTest() {
        internalSearchRepository.deleteAll();
        internal = createEntity(em);
    }

    @Test
    @Transactional
    public void createInternal() throws Exception {
        int databaseSizeBeforeCreate = internalRepository.findAll().size();

        // Create the Internal
        InternalDTO internalDTO = internalMapper.toDto(internal);

        restInternalMockMvc.perform(post("/api/internals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internalDTO)))
            .andExpect(status().isCreated());

        // Validate the Internal in the database
        List<Internal> internalList = internalRepository.findAll();
        assertThat(internalList).hasSize(databaseSizeBeforeCreate + 1);
        Internal testInternal = internalList.get(internalList.size() - 1);
        assertThat(testInternal.getIdInternal()).isEqualTo(DEFAULT_ID_INTERNAL);
        assertThat(testInternal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInternal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInternal.getIdRoleType()).isEqualTo(DEFAULT_ID_ROLE_TYPE);
        assertThat(testInternal.getIdStatusType()).isEqualTo(DEFAULT_ID_STATUS_TYPE);

        // Validate the Internal in Elasticsearch
        Internal internalEs = internalSearchRepository.findOne(testInternal.getIdPartyRole());
        assertThat(internalEs).isEqualToComparingFieldByField(testInternal);
        keyEntity = testInternal.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createInternalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = internalRepository.findAll().size();

        // Create the Internal with an existing ID
        internal.setIdPartyRole(keyEntity);
        InternalDTO internalDTO = internalMapper.toDto(internal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternalMockMvc.perform(post("/api/internals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Internal> internalList = internalRepository.findAll();
        assertThat(internalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInternals() throws Exception {
        // Initialize the database
        internalRepository.saveAndFlush(internal);

        // Get all the internalList
        restInternalMockMvc.perform(get("/api/internals?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(internal.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].idRoleType").value(hasItem(DEFAULT_ID_ROLE_TYPE)))
            .andExpect(jsonPath("$.[*].idStatusType").value(hasItem(DEFAULT_ID_STATUS_TYPE)));
    }

    @Test
    @Transactional
    public void getInternal() throws Exception {
        // Initialize the database
        internalRepository.saveAndFlush(internal);

        // Get the internal
        restInternalMockMvc.perform(get("/api/internals/{id}", internal.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(internal.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idInternal").value(DEFAULT_ID_INTERNAL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.idRoleType").value(DEFAULT_ID_ROLE_TYPE))
            .andExpect(jsonPath("$.idStatusType").value(DEFAULT_ID_STATUS_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingInternal() throws Exception {
        // Get the internal
        restInternalMockMvc.perform(get("/api/internals/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInternal() throws Exception {
        // Initialize the database
        internalRepository.saveAndFlush(internal);
        internalSearchRepository.save(internal);
        int databaseSizeBeforeUpdate = internalRepository.findAll().size();

        // Update the internal
        Internal updatedInternal = internalRepository.findOne(internal.getIdPartyRole());
        updatedInternal
            .idInternal(UPDATED_ID_INTERNAL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idRoleType(UPDATED_ID_ROLE_TYPE)
            .idStatusType(UPDATED_ID_STATUS_TYPE);
        InternalDTO internalDTO = internalMapper.toDto(updatedInternal);

        restInternalMockMvc.perform(put("/api/internals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internalDTO)))
            .andExpect(status().isOk());

        // Validate the Internal in the database
        List<Internal> internalList = internalRepository.findAll();
        assertThat(internalList).hasSize(databaseSizeBeforeUpdate);
        Internal testInternal = internalList.get(internalList.size() - 1);
        assertThat(testInternal.getIdInternal()).isEqualTo(UPDATED_ID_INTERNAL);
        assertThat(testInternal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInternal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInternal.getIdRoleType()).isEqualTo(UPDATED_ID_ROLE_TYPE);
        assertThat(testInternal.getIdStatusType()).isEqualTo(UPDATED_ID_STATUS_TYPE);

        // Validate the Internal in Elasticsearch
        Internal internalEs = internalSearchRepository.findOne(testInternal.getIdPartyRole());
        assertThat(internalEs).isEqualToComparingFieldByField(testInternal);
    }

    @Test
    @Transactional
    public void updateNonExistingInternal() throws Exception {
        int databaseSizeBeforeUpdate = internalRepository.findAll().size();

        // Create the Internal
        InternalDTO internalDTO = internalMapper.toDto(internal);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInternalMockMvc.perform(put("/api/internals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(internalDTO)))
            .andExpect(status().isCreated());

        // Validate the Internal in the database
        List<Internal> internalList = internalRepository.findAll();
        assertThat(internalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInternal() throws Exception {
        // Initialize the database
        internalRepository.saveAndFlush(internal);
        internalSearchRepository.save(internal);
        int databaseSizeBeforeDelete = internalRepository.findAll().size();

        // Get the internal
        restInternalMockMvc.perform(delete("/api/internals/{id}", internal.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean internalExistsInEs = internalSearchRepository.exists(internal.getIdPartyRole());
        assertThat(internalExistsInEs).isFalse();

        // Validate the database is empty
        List<Internal> internalList = internalRepository.findAll();
        assertThat(internalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInternal() throws Exception {
        // Initialize the database
        internalRepository.saveAndFlush(internal);
        internalSearchRepository.save(internal);

        // Search the internal
        restInternalMockMvc.perform(get("/api/_search/internals?query=idPartyRole:" + internal.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(internal.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].idRoleType").value(hasItem(DEFAULT_ID_ROLE_TYPE)))
            .andExpect(jsonPath("$.[*].idStatusType").value(hasItem(DEFAULT_ID_STATUS_TYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Internal.class);
        /*
        Internal internal1 = new Internal();
<<<<<<< HEAD
        internal1.setIdPartyRole(1L);
=======
        internal1.setId(1L);
>>>>>>> Branch_v4.5.4
        Internal internal2 = new Internal();
        internal2.setIdPartyRole(internal1.getId());
        assertThat(internal1).isEqualTo(internal2);
<<<<<<< HEAD
        internal2.setIdPartyRole(2L);
=======
        internal2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(internal1).isNotEqualTo(internal2);
        internal1.setIdPartyRole(null);
        assertThat(internal1).isNotEqualTo(internal2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternalDTO.class);
        /*
        InternalDTO internalDTO1 = new InternalDTO();
<<<<<<< HEAD
        internalDTO1.setIdPartyRole(1L);
=======
        internalDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        InternalDTO internalDTO2 = new InternalDTO();
        assertThat(internalDTO1).isNotEqualTo(internalDTO2);
        internalDTO2.setIdPartyRole(internalDTO1.getIdPartyRole());
        assertThat(internalDTO1).isEqualTo(internalDTO2);
<<<<<<< HEAD
        internalDTO2.setIdPartyRole(2L);
=======
        internalDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(internalDTO1).isNotEqualTo(internalDTO2);
        internalDTO1.setIdPartyRole(null);
        assertThat(internalDTO1).isNotEqualTo(internalDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(internalMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(internalMapper.fromIdPartyRole(null)).isNull();
    }
}
