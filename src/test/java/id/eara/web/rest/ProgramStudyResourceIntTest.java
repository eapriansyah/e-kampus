package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ProgramStudy;
import id.eara.repository.ProgramStudyRepository;
import id.eara.service.ProgramStudyService;
import id.eara.repository.search.ProgramStudySearchRepository;
import id.eara.service.dto.ProgramStudyDTO;
import id.eara.service.mapper.ProgramStudyMapper;
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
 * Test class for the ProgramStudyResource REST controller.
 *
 * @see ProgramStudyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ProgramStudyResourceIntTest {

    private static final String DEFAULT_ID_INTERNAL = "AAAAAAAAAA";
    private static final String UPDATED_ID_INTERNAL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private ProgramStudyRepository programStudyRepository;

    @Autowired
    private ProgramStudyMapper programStudyMapper;

    @Autowired
    private ProgramStudyService programStudyService;

    @Autowired
    private ProgramStudySearchRepository programStudySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramStudyMockMvc;

    private ProgramStudy programStudy;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProgramStudyResource programStudyResource = new ProgramStudyResource(programStudyService);
        this.restProgramStudyMockMvc = MockMvcBuilders.standaloneSetup(programStudyResource)
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
    public static ProgramStudy createEntity(EntityManager em) {
        ProgramStudy programStudy = new ProgramStudy()
            .idInternal(DEFAULT_ID_INTERNAL)
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return programStudy;
    }

    @Before
    public void initTest() {
        programStudySearchRepository.deleteAll();
        programStudy = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgramStudy() throws Exception {
        int databaseSizeBeforeCreate = programStudyRepository.findAll().size();

        // Create the ProgramStudy
        ProgramStudyDTO programStudyDTO = programStudyMapper.toDto(programStudy);

        restProgramStudyMockMvc.perform(post("/api/program-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStudyDTO)))
            .andExpect(status().isCreated());

        // Validate the ProgramStudy in the database
        List<ProgramStudy> programStudyList = programStudyRepository.findAll();
        assertThat(programStudyList).hasSize(databaseSizeBeforeCreate + 1);
        ProgramStudy testProgramStudy = programStudyList.get(programStudyList.size() - 1);
        assertThat(testProgramStudy.getIdInternal()).isEqualTo(DEFAULT_ID_INTERNAL);
        assertThat(testProgramStudy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgramStudy.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ProgramStudy in Elasticsearch
        ProgramStudy programStudyEs = programStudySearchRepository.findOne(testProgramStudy.getIdPartyRole());
        assertThat(programStudyEs).isEqualToComparingFieldByField(testProgramStudy);
        keyEntity = testProgramStudy.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createProgramStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programStudyRepository.findAll().size();

        // Create the ProgramStudy with an existing ID
        programStudy.setIdPartyRole(keyEntity);
        ProgramStudyDTO programStudyDTO = programStudyMapper.toDto(programStudy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramStudyMockMvc.perform(post("/api/program-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStudyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProgramStudy> programStudyList = programStudyRepository.findAll();
        assertThat(programStudyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProgramStudies() throws Exception {
        // Initialize the database
        programStudyRepository.saveAndFlush(programStudy);

        // Get all the programStudyList
        restProgramStudyMockMvc.perform(get("/api/program-studies?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(programStudy.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getProgramStudy() throws Exception {
        // Initialize the database
        programStudyRepository.saveAndFlush(programStudy);

        // Get the programStudy
        restProgramStudyMockMvc.perform(get("/api/program-studies/{id}", programStudy.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(programStudy.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idInternal").value(DEFAULT_ID_INTERNAL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingProgramStudy() throws Exception {
        // Get the programStudy
        restProgramStudyMockMvc.perform(get("/api/program-studies/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgramStudy() throws Exception {
        // Initialize the database
        programStudyRepository.saveAndFlush(programStudy);
        programStudySearchRepository.save(programStudy);
        int databaseSizeBeforeUpdate = programStudyRepository.findAll().size();

        // Update the programStudy
        ProgramStudy updatedProgramStudy = programStudyRepository.findOne(programStudy.getIdPartyRole());
        updatedProgramStudy
            .idInternal(UPDATED_ID_INTERNAL)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        ProgramStudyDTO programStudyDTO = programStudyMapper.toDto(updatedProgramStudy);

        restProgramStudyMockMvc.perform(put("/api/program-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStudyDTO)))
            .andExpect(status().isOk());

        // Validate the ProgramStudy in the database
        List<ProgramStudy> programStudyList = programStudyRepository.findAll();
        assertThat(programStudyList).hasSize(databaseSizeBeforeUpdate);
        ProgramStudy testProgramStudy = programStudyList.get(programStudyList.size() - 1);
        assertThat(testProgramStudy.getIdInternal()).isEqualTo(UPDATED_ID_INTERNAL);
        assertThat(testProgramStudy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgramStudy.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ProgramStudy in Elasticsearch
        ProgramStudy programStudyEs = programStudySearchRepository.findOne(testProgramStudy.getIdPartyRole());
        assertThat(programStudyEs).isEqualToComparingFieldByField(testProgramStudy);
    }

    @Test
    @Transactional
    public void updateNonExistingProgramStudy() throws Exception {
        int databaseSizeBeforeUpdate = programStudyRepository.findAll().size();

        // Create the ProgramStudy
        ProgramStudyDTO programStudyDTO = programStudyMapper.toDto(programStudy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgramStudyMockMvc.perform(put("/api/program-studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(programStudyDTO)))
            .andExpect(status().isCreated());

        // Validate the ProgramStudy in the database
        List<ProgramStudy> programStudyList = programStudyRepository.findAll();
        assertThat(programStudyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgramStudy() throws Exception {
        // Initialize the database
        programStudyRepository.saveAndFlush(programStudy);
        programStudySearchRepository.save(programStudy);
        int databaseSizeBeforeDelete = programStudyRepository.findAll().size();

        // Get the programStudy
        restProgramStudyMockMvc.perform(delete("/api/program-studies/{id}", programStudy.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean programStudyExistsInEs = programStudySearchRepository.exists(programStudy.getIdPartyRole());
        assertThat(programStudyExistsInEs).isFalse();

        // Validate the database is empty
        List<ProgramStudy> programStudyList = programStudyRepository.findAll();
        assertThat(programStudyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProgramStudy() throws Exception {
        // Initialize the database
        programStudyRepository.saveAndFlush(programStudy);
        programStudySearchRepository.save(programStudy);

        // Search the programStudy
        restProgramStudyMockMvc.perform(get("/api/_search/program-studies?query=idPartyRole:" + programStudy.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(programStudy.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idInternal").value(hasItem(DEFAULT_ID_INTERNAL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramStudy.class);
        /*
        ProgramStudy programStudy1 = new ProgramStudy();
<<<<<<< HEAD
        programStudy1.setIdPartyRole(1L);
=======
        programStudy1.setId(1L);
>>>>>>> Branch_v4.5.4
        ProgramStudy programStudy2 = new ProgramStudy();
        programStudy2.setIdPartyRole(programStudy1.getId());
        assertThat(programStudy1).isEqualTo(programStudy2);
<<<<<<< HEAD
        programStudy2.setIdPartyRole(2L);
=======
        programStudy2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(programStudy1).isNotEqualTo(programStudy2);
        programStudy1.setIdPartyRole(null);
        assertThat(programStudy1).isNotEqualTo(programStudy2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramStudyDTO.class);
        /*
        ProgramStudyDTO programStudyDTO1 = new ProgramStudyDTO();
<<<<<<< HEAD
        programStudyDTO1.setIdPartyRole(1L);
=======
        programStudyDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ProgramStudyDTO programStudyDTO2 = new ProgramStudyDTO();
        assertThat(programStudyDTO1).isNotEqualTo(programStudyDTO2);
        programStudyDTO2.setIdPartyRole(programStudyDTO1.getIdPartyRole());
        assertThat(programStudyDTO1).isEqualTo(programStudyDTO2);
<<<<<<< HEAD
        programStudyDTO2.setIdPartyRole(2L);
=======
        programStudyDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(programStudyDTO1).isNotEqualTo(programStudyDTO2);
        programStudyDTO1.setIdPartyRole(null);
        assertThat(programStudyDTO1).isNotEqualTo(programStudyDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(programStudyMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(programStudyMapper.fromIdPartyRole(null)).isNull();
    }
}
