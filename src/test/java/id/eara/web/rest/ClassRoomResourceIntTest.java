package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ClassRoom;
import id.eara.repository.ClassRoomRepository;
import id.eara.service.ClassRoomService;
import id.eara.repository.search.ClassRoomSearchRepository;
import id.eara.service.dto.ClassRoomDTO;
import id.eara.service.mapper.ClassRoomMapper;
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
 * Test class for the ClassRoomResource REST controller.
 *
 * @see ClassRoomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ClassRoomResourceIntTest {

    private static final String DEFAULT_FACILITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FACILITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private ClassRoomMapper classRoomMapper;

    @Autowired
    private ClassRoomService classRoomService;

    @Autowired
    private ClassRoomSearchRepository classRoomSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassRoomMockMvc;

    private ClassRoom classRoom;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassRoomResource classRoomResource = new ClassRoomResource(classRoomService);
        this.restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
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
    public static ClassRoom createEntity(EntityManager em) {
        ClassRoom classRoom = new ClassRoom()
            .facilityCode(DEFAULT_FACILITY_CODE)
            .description(DEFAULT_DESCRIPTION);
        return classRoom;
    }

    @Before
    public void initTest() {
        classRoomSearchRepository.deleteAll();
        classRoom = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassRoom() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoomDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
        assertThat(testClassRoom.getFacilityCode()).isEqualTo(DEFAULT_FACILITY_CODE);
        assertThat(testClassRoom.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ClassRoom in Elasticsearch
        ClassRoom classRoomEs = classRoomSearchRepository.findOne(testClassRoom.getIdFacility());
        assertThat(classRoomEs).isEqualToComparingFieldByField(testClassRoom);
        keyEntity = testClassRoom.getIdFacility();
    }

    @Test
    @Transactional
    public void createClassRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom with an existing ID
        classRoom.setIdFacility(keyEntity);
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassRoomMockMvc.perform(post("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClassRooms() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=idFacility,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(classRoom.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", classRoom.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idFacility").value(classRoom.getIdFacility().toString()))
            .andExpect(jsonPath("$.facilityCode").value(DEFAULT_FACILITY_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassRoom() throws Exception {
        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);
        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Update the classRoom
        ClassRoom updatedClassRoom = classRoomRepository.findOne(classRoom.getIdFacility());
        updatedClassRoom
            .facilityCode(UPDATED_FACILITY_CODE)
            .description(UPDATED_DESCRIPTION);
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(updatedClassRoom);

        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoomDTO)))
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate);
        ClassRoom testClassRoom = classRoomList.get(classRoomList.size() - 1);
        assertThat(testClassRoom.getFacilityCode()).isEqualTo(UPDATED_FACILITY_CODE);
        assertThat(testClassRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ClassRoom in Elasticsearch
        ClassRoom classRoomEs = classRoomSearchRepository.findOne(testClassRoom.getIdFacility());
        assertThat(classRoomEs).isEqualToComparingFieldByField(testClassRoom);
    }

    @Test
    @Transactional
    public void updateNonExistingClassRoom() throws Exception {
        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassRoomMockMvc.perform(put("/api/class-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classRoomDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);
        int databaseSizeBeforeDelete = classRoomRepository.findAll().size();

        // Get the classRoom
        restClassRoomMockMvc.perform(delete("/api/class-rooms/{id}", classRoom.getIdFacility())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean classRoomExistsInEs = classRoomSearchRepository.exists(classRoom.getIdFacility());
        assertThat(classRoomExistsInEs).isFalse();

        // Validate the database is empty
        List<ClassRoom> classRoomList = classRoomRepository.findAll();
        assertThat(classRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);

        // Search the classRoom
        restClassRoomMockMvc.perform(get("/api/_search/class-rooms?query=idFacility:" + classRoom.getIdFacility()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idFacility").value(hasItem(classRoom.getIdFacility().toString())))
            .andExpect(jsonPath("$.[*].facilityCode").value(hasItem(DEFAULT_FACILITY_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoom.class);
        /*
        ClassRoom classRoom1 = new ClassRoom();
<<<<<<< HEAD
        classRoom1.setIdFacility(1L);
=======
        classRoom1.setId(1L);
>>>>>>> Branch_v4.5.4
        ClassRoom classRoom2 = new ClassRoom();
        classRoom2.setIdFacility(classRoom1.getId());
        assertThat(classRoom1).isEqualTo(classRoom2);
<<<<<<< HEAD
        classRoom2.setIdFacility(2L);
=======
        classRoom2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(classRoom1).isNotEqualTo(classRoom2);
        classRoom1.setIdFacility(null);
        assertThat(classRoom1).isNotEqualTo(classRoom2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoomDTO.class);
        /*
        ClassRoomDTO classRoomDTO1 = new ClassRoomDTO();
<<<<<<< HEAD
        classRoomDTO1.setIdFacility(1L);
=======
        classRoomDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ClassRoomDTO classRoomDTO2 = new ClassRoomDTO();
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
        classRoomDTO2.setIdFacility(classRoomDTO1.getIdFacility());
        assertThat(classRoomDTO1).isEqualTo(classRoomDTO2);
<<<<<<< HEAD
        classRoomDTO2.setIdFacility(2L);
=======
        classRoomDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
        classRoomDTO1.setIdFacility(null);
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(classRoomMapper.fromidFacility(42L).getidFacility()).isEqualTo(42);
        //assertThat(classRoomMapper.fromIdFacility(null)).isNull();
    }
}
