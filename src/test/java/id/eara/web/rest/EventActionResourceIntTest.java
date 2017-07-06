package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.EventAction;
import id.eara.repository.EventActionRepository;
import id.eara.service.EventActionService;
import id.eara.repository.search.EventActionSearchRepository;
import id.eara.service.dto.EventActionDTO;
import id.eara.service.mapper.EventActionMapper;
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
 * Test class for the EventActionResource REST controller.
 *
 * @see EventActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class EventActionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private EventActionRepository eventActionRepository;

    @Autowired
    private EventActionMapper eventActionMapper;

    @Autowired
    private EventActionService eventActionService;

    @Autowired
    private EventActionSearchRepository eventActionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventActionMockMvc;

    private EventAction eventAction;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventActionResource eventActionResource = new EventActionResource(eventActionService);
        this.restEventActionMockMvc = MockMvcBuilders.standaloneSetup(eventActionResource)
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
    public static EventAction createEntity(EntityManager em) {
        EventAction eventAction = new EventAction()
            .description(DEFAULT_DESCRIPTION);
        return eventAction;
    }

    @Before
    public void initTest() {
        eventActionSearchRepository.deleteAll();
        eventAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventAction() throws Exception {
        int databaseSizeBeforeCreate = eventActionRepository.findAll().size();

        // Create the EventAction
        EventActionDTO eventActionDTO = eventActionMapper.toDto(eventAction);

        restEventActionMockMvc.perform(post("/api/event-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventActionDTO)))
            .andExpect(status().isCreated());

        // Validate the EventAction in the database
        List<EventAction> eventActionList = eventActionRepository.findAll();
        assertThat(eventActionList).hasSize(databaseSizeBeforeCreate + 1);
        EventAction testEventAction = eventActionList.get(eventActionList.size() - 1);
        assertThat(testEventAction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the EventAction in Elasticsearch
        EventAction eventActionEs = eventActionSearchRepository.findOne(testEventAction.getIdEventAction());
        assertThat(eventActionEs).isEqualToComparingFieldByField(testEventAction);
    }

    @Test
    @Transactional
    public void createEventActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventActionRepository.findAll().size();

        // Create the EventAction with an existing ID
        eventAction.setIdEventAction(1L);
        EventActionDTO eventActionDTO = eventActionMapper.toDto(eventAction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventActionMockMvc.perform(post("/api/event-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EventAction> eventActionList = eventActionRepository.findAll();
        assertThat(eventActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventActions() throws Exception {
        // Initialize the database
        eventActionRepository.saveAndFlush(eventAction);

        // Get all the eventActionList
        restEventActionMockMvc.perform(get("/api/event-actions?sort=idEventAction,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEventAction").value(hasItem(eventAction.getIdEventAction().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEventAction() throws Exception {
        // Initialize the database
        eventActionRepository.saveAndFlush(eventAction);

        // Get the eventAction
        restEventActionMockMvc.perform(get("/api/event-actions/{id}", eventAction.getIdEventAction()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idEventAction").value(eventAction.getIdEventAction().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventAction() throws Exception {
        // Get the eventAction
        restEventActionMockMvc.perform(get("/api/event-actions/{id}",  Long.MAX_VALUE ))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventAction() throws Exception {
        // Initialize the database
        eventActionRepository.saveAndFlush(eventAction);
        eventActionSearchRepository.save(eventAction);
        int databaseSizeBeforeUpdate = eventActionRepository.findAll().size();

        // Update the eventAction
        EventAction updatedEventAction = eventActionRepository.findOne(eventAction.getIdEventAction());
        updatedEventAction
            .description(UPDATED_DESCRIPTION);
        EventActionDTO eventActionDTO = eventActionMapper.toDto(updatedEventAction);

        restEventActionMockMvc.perform(put("/api/event-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventActionDTO)))
            .andExpect(status().isOk());

        // Validate the EventAction in the database
        List<EventAction> eventActionList = eventActionRepository.findAll();
        assertThat(eventActionList).hasSize(databaseSizeBeforeUpdate);
        EventAction testEventAction = eventActionList.get(eventActionList.size() - 1);
        assertThat(testEventAction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the EventAction in Elasticsearch
        EventAction eventActionEs = eventActionSearchRepository.findOne(testEventAction.getIdEventAction());
        assertThat(eventActionEs).isEqualToComparingFieldByField(testEventAction);
    }

    @Test
    @Transactional
    public void updateNonExistingEventAction() throws Exception {
        int databaseSizeBeforeUpdate = eventActionRepository.findAll().size();

        // Create the EventAction
        EventActionDTO eventActionDTO = eventActionMapper.toDto(eventAction);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventActionMockMvc.perform(put("/api/event-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventActionDTO)))
            .andExpect(status().isCreated());

        // Validate the EventAction in the database
        List<EventAction> eventActionList = eventActionRepository.findAll();
        assertThat(eventActionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEventAction() throws Exception {
        // Initialize the database
        eventActionRepository.saveAndFlush(eventAction);
        eventActionSearchRepository.save(eventAction);
        int databaseSizeBeforeDelete = eventActionRepository.findAll().size();

        // Get the eventAction
        restEventActionMockMvc.perform(delete("/api/event-actions/{id}", eventAction.getIdEventAction())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean eventActionExistsInEs = eventActionSearchRepository.exists(eventAction.getIdEventAction());
        assertThat(eventActionExistsInEs).isFalse();

        // Validate the database is empty
        List<EventAction> eventActionList = eventActionRepository.findAll();
        assertThat(eventActionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEventAction() throws Exception {
        // Initialize the database
        eventActionRepository.saveAndFlush(eventAction);
        eventActionSearchRepository.save(eventAction);

        // Search the eventAction
        restEventActionMockMvc.perform(get("/api/_search/event-actions?query=idEventAction:" + eventAction.getIdEventAction()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEventAction").value(hasItem(eventAction.getIdEventAction().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventAction.class);
        /*
        EventAction eventAction1 = new EventAction();
<<<<<<< HEAD
        eventAction1.setIdEventAction(1L);
=======
        eventAction1.setId(1L);
>>>>>>> Branch_v4.5.4
        EventAction eventAction2 = new EventAction();
        eventAction2.setIdEventAction(eventAction1.getId());
        assertThat(eventAction1).isEqualTo(eventAction2);
<<<<<<< HEAD
        eventAction2.setIdEventAction(2L);
=======
        eventAction2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(eventAction1).isNotEqualTo(eventAction2);
        eventAction1.setIdEventAction(null);
        assertThat(eventAction1).isNotEqualTo(eventAction2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventActionDTO.class);
        /*
        EventActionDTO eventActionDTO1 = new EventActionDTO();
<<<<<<< HEAD
        eventActionDTO1.setIdEventAction(1L);
=======
        eventActionDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        EventActionDTO eventActionDTO2 = new EventActionDTO();
        assertThat(eventActionDTO1).isNotEqualTo(eventActionDTO2);
        eventActionDTO2.setIdEventAction(eventActionDTO1.getIdEventAction());
        assertThat(eventActionDTO1).isEqualTo(eventActionDTO2);
<<<<<<< HEAD
        eventActionDTO2.setIdEventAction(2L);
=======
        eventActionDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(eventActionDTO1).isNotEqualTo(eventActionDTO2);
        eventActionDTO1.setIdEventAction(null);
        assertThat(eventActionDTO1).isNotEqualTo(eventActionDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(eventActionMapper.fromidEventAction(42L).getidEventAction()).isEqualTo(42);
        //assertThat(eventActionMapper.fromIdEventAction(null)).isNull();
    }
}
