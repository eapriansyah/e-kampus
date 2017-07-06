package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Degree;
import id.eara.repository.DegreeRepository;
import id.eara.service.DegreeService;
import id.eara.repository.search.DegreeSearchRepository;
import id.eara.service.dto.DegreeDTO;
import id.eara.service.mapper.DegreeMapper;
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
 * Test class for the DegreeResource REST controller.
 *
 * @see DegreeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class DegreeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_STUDY = 1;
    private static final Integer UPDATED_MAX_STUDY = 2;

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private DegreeMapper degreeMapper;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private DegreeSearchRepository degreeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDegreeMockMvc;

    private Degree degree;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DegreeResource degreeResource = new DegreeResource(degreeService);
        this.restDegreeMockMvc = MockMvcBuilders.standaloneSetup(degreeResource)
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
    public static Degree createEntity(EntityManager em) {
        Degree degree = new Degree()
            .description(DEFAULT_DESCRIPTION)
            .maxStudy(DEFAULT_MAX_STUDY);
        return degree;
    }

    @Before
    public void initTest() {
        degreeSearchRepository.deleteAll();
        degree = createEntity(em);
    }

    @Test
    @Transactional
    public void createDegree() throws Exception {
        int databaseSizeBeforeCreate = degreeRepository.findAll().size();

        // Create the Degree
        DegreeDTO degreeDTO = degreeMapper.toDto(degree);

        restDegreeMockMvc.perform(post("/api/degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(degreeDTO)))
            .andExpect(status().isCreated());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeCreate + 1);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDegree.getMaxStudy()).isEqualTo(DEFAULT_MAX_STUDY);

        // Validate the Degree in Elasticsearch
        Degree degreeEs = degreeSearchRepository.findOne(testDegree.getIdDegree());
        assertThat(degreeEs).isEqualToComparingFieldByField(testDegree);
    }

    @Test
    @Transactional
    public void createDegreeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = degreeRepository.findAll().size();

        // Create the Degree with an existing ID
        degree.setIdDegree(1L);
        DegreeDTO degreeDTO = degreeMapper.toDto(degree);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDegreeMockMvc.perform(post("/api/degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(degreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDegrees() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        // Get all the degreeList
        restDegreeMockMvc.perform(get("/api/degrees?sort=idDegree,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idDegree").value(hasItem(degree.getIdDegree().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].maxStudy").value(hasItem(DEFAULT_MAX_STUDY)));
    }

    @Test
    @Transactional
    public void getDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);

        // Get the degree
        restDegreeMockMvc.perform(get("/api/degrees/{id}", degree.getIdDegree()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idDegree").value(degree.getIdDegree().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.maxStudy").value(DEFAULT_MAX_STUDY));
    }

    @Test
    @Transactional
    public void getNonExistingDegree() throws Exception {
        // Get the degree
        restDegreeMockMvc.perform(get("/api/degrees/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);
        degreeSearchRepository.save(degree);
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();

        // Update the degree
        Degree updatedDegree = degreeRepository.findOne(degree.getIdDegree());
        updatedDegree
            .description(UPDATED_DESCRIPTION)
            .maxStudy(UPDATED_MAX_STUDY);
        DegreeDTO degreeDTO = degreeMapper.toDto(updatedDegree);

        restDegreeMockMvc.perform(put("/api/degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(degreeDTO)))
            .andExpect(status().isOk());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate);
        Degree testDegree = degreeList.get(degreeList.size() - 1);
        assertThat(testDegree.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDegree.getMaxStudy()).isEqualTo(UPDATED_MAX_STUDY);

        // Validate the Degree in Elasticsearch
        Degree degreeEs = degreeSearchRepository.findOne(testDegree.getIdDegree());
        assertThat(degreeEs).isEqualToComparingFieldByField(testDegree);
    }

    @Test
    @Transactional
    public void updateNonExistingDegree() throws Exception {
        int databaseSizeBeforeUpdate = degreeRepository.findAll().size();

        // Create the Degree
        DegreeDTO degreeDTO = degreeMapper.toDto(degree);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDegreeMockMvc.perform(put("/api/degrees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(degreeDTO)))
            .andExpect(status().isCreated());

        // Validate the Degree in the database
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);
        degreeSearchRepository.save(degree);
        int databaseSizeBeforeDelete = degreeRepository.findAll().size();

        // Get the degree
        restDegreeMockMvc.perform(delete("/api/degrees/{id}", degree.getIdDegree())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean degreeExistsInEs = degreeSearchRepository.exists(degree.getIdDegree());
        assertThat(degreeExistsInEs).isFalse();

        // Validate the database is empty
        List<Degree> degreeList = degreeRepository.findAll();
        assertThat(degreeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDegree() throws Exception {
        // Initialize the database
        degreeRepository.saveAndFlush(degree);
        degreeSearchRepository.save(degree);

        // Search the degree
        restDegreeMockMvc.perform(get("/api/_search/degrees?query=idDegree:" + degree.getIdDegree()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idDegree").value(hasItem(degree.getIdDegree().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].maxStudy").value(hasItem(DEFAULT_MAX_STUDY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Degree.class);
        /*
        Degree degree1 = new Degree();
<<<<<<< HEAD
        degree1.setIdDegree(1L);
=======
        degree1.setId(1L);
>>>>>>> Branch_v4.5.4
        Degree degree2 = new Degree();
        degree2.setIdDegree(degree1.getId());
        assertThat(degree1).isEqualTo(degree2);
<<<<<<< HEAD
        degree2.setIdDegree(2L);
=======
        degree2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(degree1).isNotEqualTo(degree2);
        degree1.setIdDegree(null);
        assertThat(degree1).isNotEqualTo(degree2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DegreeDTO.class);
        /*
        DegreeDTO degreeDTO1 = new DegreeDTO();
<<<<<<< HEAD
        degreeDTO1.setIdDegree(1L);
=======
        degreeDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        DegreeDTO degreeDTO2 = new DegreeDTO();
        assertThat(degreeDTO1).isNotEqualTo(degreeDTO2);
        degreeDTO2.setIdDegree(degreeDTO1.getIdDegree());
        assertThat(degreeDTO1).isEqualTo(degreeDTO2);
<<<<<<< HEAD
        degreeDTO2.setIdDegree(2L);
=======
        degreeDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(degreeDTO1).isNotEqualTo(degreeDTO2);
        degreeDTO1.setIdDegree(null);
        assertThat(degreeDTO1).isNotEqualTo(degreeDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(degreeMapper.fromidDegree(42L).getidDegree()).isEqualTo(42);
        //assertThat(degreeMapper.fromIdDegree(null)).isNull();
    }
}
