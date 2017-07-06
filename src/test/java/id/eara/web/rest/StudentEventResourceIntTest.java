package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.StudentEvent;
import id.eara.repository.StudentEventRepository;
import id.eara.service.StudentEventService;
import id.eara.repository.search.StudentEventSearchRepository;
import id.eara.service.dto.StudentEventDTO;
import id.eara.service.mapper.StudentEventMapper;
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
 * Test class for the StudentEventResource REST controller.
 *
 * @see StudentEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class StudentEventResourceIntTest {

    private static final String DEFAULT_ID_EVENT = "AAAAAAAAAA";
    private static final String UPDATED_ID_EVENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_REGISTRATIONTYPE = 1;
    private static final Integer UPDATED_REGISTRATIONTYPE = 2;

    @Autowired
    private StudentEventRepository studentEventRepository;

    @Autowired
    private StudentEventMapper studentEventMapper;

    @Autowired
    private StudentEventService studentEventService;

    @Autowired
    private StudentEventSearchRepository studentEventSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentEventMockMvc;

    private StudentEvent studentEvent;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentEventResource studentEventResource = new StudentEventResource(studentEventService);
        this.restStudentEventMockMvc = MockMvcBuilders.standaloneSetup(studentEventResource)
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
    public static StudentEvent createEntity(EntityManager em) {
        StudentEvent studentEvent = new StudentEvent()
            .idEvent(DEFAULT_ID_EVENT)
            .description(DEFAULT_DESCRIPTION)
            .registrationtype(DEFAULT_REGISTRATIONTYPE);
        return studentEvent;
    }

    @Before
    public void initTest() {
        studentEventSearchRepository.deleteAll();
        studentEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentEvent() throws Exception {
        int databaseSizeBeforeCreate = studentEventRepository.findAll().size();

        // Create the StudentEvent
        StudentEventDTO studentEventDTO = studentEventMapper.toDto(studentEvent);

        restStudentEventMockMvc.perform(post("/api/student-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentEventDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentEvent in the database
        List<StudentEvent> studentEventList = studentEventRepository.findAll();
        assertThat(studentEventList).hasSize(databaseSizeBeforeCreate + 1);
        StudentEvent testStudentEvent = studentEventList.get(studentEventList.size() - 1);
        assertThat(testStudentEvent.getIdEvent()).isEqualTo(DEFAULT_ID_EVENT);
        assertThat(testStudentEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStudentEvent.getRegistrationtype()).isEqualTo(DEFAULT_REGISTRATIONTYPE);

        // Validate the StudentEvent in Elasticsearch
        StudentEvent studentEventEs = studentEventSearchRepository.findOne(testStudentEvent.getIdStudentEvent());
        assertThat(studentEventEs).isEqualToComparingFieldByField(testStudentEvent);
    }

    @Test
    @Transactional
    public void createStudentEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentEventRepository.findAll().size();

        // Create the StudentEvent with an existing ID
        studentEvent.setIdStudentEvent(1L);
        StudentEventDTO studentEventDTO = studentEventMapper.toDto(studentEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentEventMockMvc.perform(post("/api/student-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentEvent> studentEventList = studentEventRepository.findAll();
        assertThat(studentEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudentEvents() throws Exception {
        // Initialize the database
        studentEventRepository.saveAndFlush(studentEvent);

        // Get all the studentEventList
        restStudentEventMockMvc.perform(get("/api/student-events?sort=idStudentEvent,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentEvent").value(hasItem(studentEvent.getIdStudentEvent().intValue())))
            .andExpect(jsonPath("$.[*].idEvent").value(hasItem(DEFAULT_ID_EVENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].registrationtype").value(hasItem(DEFAULT_REGISTRATIONTYPE)));
    }

    @Test
    @Transactional
    public void getStudentEvent() throws Exception {
        // Initialize the database
        studentEventRepository.saveAndFlush(studentEvent);

        // Get the studentEvent
        restStudentEventMockMvc.perform(get("/api/student-events/{id}", studentEvent.getIdStudentEvent()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idStudentEvent").value(studentEvent.getIdStudentEvent().intValue()))
            .andExpect(jsonPath("$.idEvent").value(DEFAULT_ID_EVENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.registrationtype").value(DEFAULT_REGISTRATIONTYPE));
    }

    @Test
    @Transactional
    public void getNonExistingStudentEvent() throws Exception {
        // Get the studentEvent
        restStudentEventMockMvc.perform(get("/api/student-events/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentEvent() throws Exception {
        // Initialize the database
        studentEventRepository.saveAndFlush(studentEvent);
        studentEventSearchRepository.save(studentEvent);
        int databaseSizeBeforeUpdate = studentEventRepository.findAll().size();

        // Update the studentEvent
        StudentEvent updatedStudentEvent = studentEventRepository.findOne(studentEvent.getIdStudentEvent());
        updatedStudentEvent
            .idEvent(UPDATED_ID_EVENT)
            .description(UPDATED_DESCRIPTION)
            .registrationtype(UPDATED_REGISTRATIONTYPE);
        StudentEventDTO studentEventDTO = studentEventMapper.toDto(updatedStudentEvent);

        restStudentEventMockMvc.perform(put("/api/student-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentEventDTO)))
            .andExpect(status().isOk());

        // Validate the StudentEvent in the database
        List<StudentEvent> studentEventList = studentEventRepository.findAll();
        assertThat(studentEventList).hasSize(databaseSizeBeforeUpdate);
        StudentEvent testStudentEvent = studentEventList.get(studentEventList.size() - 1);
        assertThat(testStudentEvent.getIdEvent()).isEqualTo(UPDATED_ID_EVENT);
        assertThat(testStudentEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStudentEvent.getRegistrationtype()).isEqualTo(UPDATED_REGISTRATIONTYPE);

        // Validate the StudentEvent in Elasticsearch
        StudentEvent studentEventEs = studentEventSearchRepository.findOne(testStudentEvent.getIdStudentEvent());
        assertThat(studentEventEs).isEqualToComparingFieldByField(testStudentEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentEvent() throws Exception {
        int databaseSizeBeforeUpdate = studentEventRepository.findAll().size();

        // Create the StudentEvent
        StudentEventDTO studentEventDTO = studentEventMapper.toDto(studentEvent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentEventMockMvc.perform(put("/api/student-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentEventDTO)))
            .andExpect(status().isCreated());

        // Validate the StudentEvent in the database
        List<StudentEvent> studentEventList = studentEventRepository.findAll();
        assertThat(studentEventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudentEvent() throws Exception {
        // Initialize the database
        studentEventRepository.saveAndFlush(studentEvent);
        studentEventSearchRepository.save(studentEvent);
        int databaseSizeBeforeDelete = studentEventRepository.findAll().size();

        // Get the studentEvent
        restStudentEventMockMvc.perform(delete("/api/student-events/{id}", studentEvent.getIdStudentEvent())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentEventExistsInEs = studentEventSearchRepository.exists(studentEvent.getIdStudentEvent());
        assertThat(studentEventExistsInEs).isFalse();

        // Validate the database is empty
        List<StudentEvent> studentEventList = studentEventRepository.findAll();
        assertThat(studentEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudentEvent() throws Exception {
        // Initialize the database
        studentEventRepository.saveAndFlush(studentEvent);
        studentEventSearchRepository.save(studentEvent);

        // Search the studentEvent
        restStudentEventMockMvc.perform(get("/api/_search/student-events?query=idStudentEvent:" + studentEvent.getIdStudentEvent()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idStudentEvent").value(hasItem(studentEvent.getIdStudentEvent().intValue())))
            .andExpect(jsonPath("$.[*].idEvent").value(hasItem(DEFAULT_ID_EVENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].registrationtype").value(hasItem(DEFAULT_REGISTRATIONTYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentEvent.class);
        /*
        StudentEvent studentEvent1 = new StudentEvent();
<<<<<<< HEAD
        studentEvent1.setIdStudentEvent(1L);
=======
        studentEvent1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentEvent studentEvent2 = new StudentEvent();
        studentEvent2.setIdStudentEvent(studentEvent1.getId());
        assertThat(studentEvent1).isEqualTo(studentEvent2);
<<<<<<< HEAD
        studentEvent2.setIdStudentEvent(2L);
=======
        studentEvent2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentEvent1).isNotEqualTo(studentEvent2);
        studentEvent1.setIdStudentEvent(null);
        assertThat(studentEvent1).isNotEqualTo(studentEvent2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentEventDTO.class);
        /*
        StudentEventDTO studentEventDTO1 = new StudentEventDTO();
<<<<<<< HEAD
        studentEventDTO1.setIdStudentEvent(1L);
=======
        studentEventDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        StudentEventDTO studentEventDTO2 = new StudentEventDTO();
        assertThat(studentEventDTO1).isNotEqualTo(studentEventDTO2);
        studentEventDTO2.setIdStudentEvent(studentEventDTO1.getIdStudentEvent());
        assertThat(studentEventDTO1).isEqualTo(studentEventDTO2);
<<<<<<< HEAD
        studentEventDTO2.setIdStudentEvent(2L);
=======
        studentEventDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(studentEventDTO1).isNotEqualTo(studentEventDTO2);
        studentEventDTO1.setIdStudentEvent(null);
        assertThat(studentEventDTO1).isNotEqualTo(studentEventDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(studentEventMapper.fromidStudentEvent(42L).getidStudentEvent()).isEqualTo(42);
        //assertThat(studentEventMapper.fromIdStudentEvent(null)).isNull();
    }
}
