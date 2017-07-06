package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PostStudent;
import id.eara.repository.PostStudentRepository;
import id.eara.service.PostStudentService;
import id.eara.repository.search.PostStudentSearchRepository;
import id.eara.service.dto.PostStudentDTO;
import id.eara.service.mapper.PostStudentMapper;
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
 * Test class for the PostStudentResource REST controller.
 *
 * @see PostStudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PostStudentResourceIntTest {

    private static final String DEFAULT_IDPOSTSTUDENT = "AAAAAAAAAA";
    private static final String UPDATED_IDPOSTSTUDENT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private PostStudentRepository postStudentRepository;

    @Autowired
    private PostStudentMapper postStudentMapper;

    @Autowired
    private PostStudentService postStudentService;

    @Autowired
    private PostStudentSearchRepository postStudentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostStudentMockMvc;

    private PostStudent postStudent;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostStudentResource postStudentResource = new PostStudentResource(postStudentService);
        this.restPostStudentMockMvc = MockMvcBuilders.standaloneSetup(postStudentResource)
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
    public static PostStudent createEntity(EntityManager em) {
        PostStudent postStudent = new PostStudent()
            .idpoststudent(DEFAULT_IDPOSTSTUDENT)
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS);
        return postStudent;
    }

    @Before
    public void initTest() {
        postStudentSearchRepository.deleteAll();
        postStudent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostStudent() throws Exception {
        int databaseSizeBeforeCreate = postStudentRepository.findAll().size();

        // Create the PostStudent
        PostStudentDTO postStudentDTO = postStudentMapper.toDto(postStudent);

        restPostStudentMockMvc.perform(post("/api/post-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postStudentDTO)))
            .andExpect(status().isCreated());

        // Validate the PostStudent in the database
        List<PostStudent> postStudentList = postStudentRepository.findAll();
        assertThat(postStudentList).hasSize(databaseSizeBeforeCreate + 1);
        PostStudent testPostStudent = postStudentList.get(postStudentList.size() - 1);
        assertThat(testPostStudent.getIdpoststudent()).isEqualTo(DEFAULT_IDPOSTSTUDENT);
        assertThat(testPostStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPostStudent.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the PostStudent in Elasticsearch
        PostStudent postStudentEs = postStudentSearchRepository.findOne(testPostStudent.getIdPartyRole());
        assertThat(postStudentEs).isEqualToComparingFieldByField(testPostStudent);
        keyEntity = testPostStudent.getIdPartyRole();
    }

    @Test
    @Transactional
    public void createPostStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postStudentRepository.findAll().size();

        // Create the PostStudent with an existing ID
        postStudent.setIdPartyRole(keyEntity);
        PostStudentDTO postStudentDTO = postStudentMapper.toDto(postStudent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostStudentMockMvc.perform(post("/api/post-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postStudentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PostStudent> postStudentList = postStudentRepository.findAll();
        assertThat(postStudentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostStudents() throws Exception {
        // Initialize the database
        postStudentRepository.saveAndFlush(postStudent);

        // Get all the postStudentList
        restPostStudentMockMvc.perform(get("/api/post-students?sort=idPartyRole,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(postStudent.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idpoststudent").value(hasItem(DEFAULT_IDPOSTSTUDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getPostStudent() throws Exception {
        // Initialize the database
        postStudentRepository.saveAndFlush(postStudent);

        // Get the postStudent
        restPostStudentMockMvc.perform(get("/api/post-students/{id}", postStudent.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idPartyRole").value(postStudent.getIdPartyRole().toString()))
            .andExpect(jsonPath("$.idpoststudent").value(DEFAULT_IDPOSTSTUDENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingPostStudent() throws Exception {
        // Get the postStudent
        restPostStudentMockMvc.perform(get("/api/post-students/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostStudent() throws Exception {
        // Initialize the database
        postStudentRepository.saveAndFlush(postStudent);
        postStudentSearchRepository.save(postStudent);
        int databaseSizeBeforeUpdate = postStudentRepository.findAll().size();

        // Update the postStudent
        PostStudent updatedPostStudent = postStudentRepository.findOne(postStudent.getIdPartyRole());
        updatedPostStudent
            .idpoststudent(UPDATED_IDPOSTSTUDENT)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS);
        PostStudentDTO postStudentDTO = postStudentMapper.toDto(updatedPostStudent);

        restPostStudentMockMvc.perform(put("/api/post-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postStudentDTO)))
            .andExpect(status().isOk());

        // Validate the PostStudent in the database
        List<PostStudent> postStudentList = postStudentRepository.findAll();
        assertThat(postStudentList).hasSize(databaseSizeBeforeUpdate);
        PostStudent testPostStudent = postStudentList.get(postStudentList.size() - 1);
        assertThat(testPostStudent.getIdpoststudent()).isEqualTo(UPDATED_IDPOSTSTUDENT);
        assertThat(testPostStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPostStudent.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the PostStudent in Elasticsearch
        PostStudent postStudentEs = postStudentSearchRepository.findOne(testPostStudent.getIdPartyRole());
        assertThat(postStudentEs).isEqualToComparingFieldByField(testPostStudent);
    }

    @Test
    @Transactional
    public void updateNonExistingPostStudent() throws Exception {
        int databaseSizeBeforeUpdate = postStudentRepository.findAll().size();

        // Create the PostStudent
        PostStudentDTO postStudentDTO = postStudentMapper.toDto(postStudent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPostStudentMockMvc.perform(put("/api/post-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postStudentDTO)))
            .andExpect(status().isCreated());

        // Validate the PostStudent in the database
        List<PostStudent> postStudentList = postStudentRepository.findAll();
        assertThat(postStudentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostStudent() throws Exception {
        // Initialize the database
        postStudentRepository.saveAndFlush(postStudent);
        postStudentSearchRepository.save(postStudent);
        int databaseSizeBeforeDelete = postStudentRepository.findAll().size();

        // Get the postStudent
        restPostStudentMockMvc.perform(delete("/api/post-students/{id}", postStudent.getIdPartyRole())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean postStudentExistsInEs = postStudentSearchRepository.exists(postStudent.getIdPartyRole());
        assertThat(postStudentExistsInEs).isFalse();

        // Validate the database is empty
        List<PostStudent> postStudentList = postStudentRepository.findAll();
        assertThat(postStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPostStudent() throws Exception {
        // Initialize the database
        postStudentRepository.saveAndFlush(postStudent);
        postStudentSearchRepository.save(postStudent);

        // Search the postStudent
        restPostStudentMockMvc.perform(get("/api/_search/post-students?query=idPartyRole:" + postStudent.getIdPartyRole()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idPartyRole").value(hasItem(postStudent.getIdPartyRole().toString())))
            .andExpect(jsonPath("$.[*].idpoststudent").value(hasItem(DEFAULT_IDPOSTSTUDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostStudent.class);
        /*
        PostStudent postStudent1 = new PostStudent();
<<<<<<< HEAD
        postStudent1.setIdPartyRole(1L);
=======
        postStudent1.setId(1L);
>>>>>>> Branch_v4.5.4
        PostStudent postStudent2 = new PostStudent();
        postStudent2.setIdPartyRole(postStudent1.getId());
        assertThat(postStudent1).isEqualTo(postStudent2);
<<<<<<< HEAD
        postStudent2.setIdPartyRole(2L);
=======
        postStudent2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(postStudent1).isNotEqualTo(postStudent2);
        postStudent1.setIdPartyRole(null);
        assertThat(postStudent1).isNotEqualTo(postStudent2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostStudentDTO.class);
        /*
        PostStudentDTO postStudentDTO1 = new PostStudentDTO();
<<<<<<< HEAD
        postStudentDTO1.setIdPartyRole(1L);
=======
        postStudentDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PostStudentDTO postStudentDTO2 = new PostStudentDTO();
        assertThat(postStudentDTO1).isNotEqualTo(postStudentDTO2);
        postStudentDTO2.setIdPartyRole(postStudentDTO1.getIdPartyRole());
        assertThat(postStudentDTO1).isEqualTo(postStudentDTO2);
<<<<<<< HEAD
        postStudentDTO2.setIdPartyRole(2L);
=======
        postStudentDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(postStudentDTO1).isNotEqualTo(postStudentDTO2);
        postStudentDTO1.setIdPartyRole(null);
        assertThat(postStudentDTO1).isNotEqualTo(postStudentDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(postStudentMapper.fromidPartyRole(42L).getidPartyRole()).isEqualTo(42);
        //assertThat(postStudentMapper.fromIdPartyRole(null)).isNull();
    }
}
