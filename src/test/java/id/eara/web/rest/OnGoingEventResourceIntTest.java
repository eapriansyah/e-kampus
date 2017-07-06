package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.OnGoingEvent;
import id.eara.repository.OnGoingEventRepository;
import id.eara.service.OnGoingEventService;
import id.eara.repository.search.OnGoingEventSearchRepository;
import id.eara.service.dto.OnGoingEventDTO;
import id.eara.service.mapper.OnGoingEventMapper;
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
 * Test class for the OnGoingEventResource REST controller.
 *
 * @see OnGoingEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class OnGoingEventResourceIntTest {

    private static final String DEFAULT_ID_EVENT = "AAAAAAAAAA";
    private static final String UPDATED_ID_EVENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_THRU = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_THRU = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private OnGoingEventRepository onGoingEventRepository;

    @Autowired
    private OnGoingEventMapper onGoingEventMapper;

    @Autowired
    private OnGoingEventService onGoingEventService;

    @Autowired
    private OnGoingEventSearchRepository onGoingEventSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOnGoingEventMockMvc;

    private OnGoingEvent onGoingEvent;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OnGoingEventResource onGoingEventResource = new OnGoingEventResource(onGoingEventService);
        this.restOnGoingEventMockMvc = MockMvcBuilders.standaloneSetup(onGoingEventResource)
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
    public static OnGoingEvent createEntity(EntityManager em) {
        OnGoingEvent onGoingEvent = new OnGoingEvent()
            .idEvent(DEFAULT_ID_EVENT)
            .description(DEFAULT_DESCRIPTION)
            .dateFrom(DEFAULT_DATE_FROM)
            .dateThru(DEFAULT_DATE_THRU);
        return onGoingEvent;
    }

    @Before
    public void initTest() {
        onGoingEventSearchRepository.deleteAll();
        onGoingEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createOnGoingEvent() throws Exception {
        int databaseSizeBeforeCreate = onGoingEventRepository.findAll().size();

        // Create the OnGoingEvent
        OnGoingEventDTO onGoingEventDTO = onGoingEventMapper.toDto(onGoingEvent);

        restOnGoingEventMockMvc.perform(post("/api/on-going-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onGoingEventDTO)))
            .andExpect(status().isCreated());

        // Validate the OnGoingEvent in the database
        List<OnGoingEvent> onGoingEventList = onGoingEventRepository.findAll();
        assertThat(onGoingEventList).hasSize(databaseSizeBeforeCreate + 1);
        OnGoingEvent testOnGoingEvent = onGoingEventList.get(onGoingEventList.size() - 1);
        assertThat(testOnGoingEvent.getIdEvent()).isEqualTo(DEFAULT_ID_EVENT);
        assertThat(testOnGoingEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOnGoingEvent.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testOnGoingEvent.getDateThru()).isEqualTo(DEFAULT_DATE_THRU);

        // Validate the OnGoingEvent in Elasticsearch
        OnGoingEvent onGoingEventEs = onGoingEventSearchRepository.findOne(testOnGoingEvent.getIdEventGo());
        assertThat(onGoingEventEs).isEqualToComparingFieldByField(testOnGoingEvent);
        keyEntity = testOnGoingEvent.getIdEventGo();
    }

    @Test
    @Transactional
    public void createOnGoingEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = onGoingEventRepository.findAll().size();

        // Create the OnGoingEvent with an existing ID
        onGoingEvent.setIdEventGo(keyEntity);
        OnGoingEventDTO onGoingEventDTO = onGoingEventMapper.toDto(onGoingEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOnGoingEventMockMvc.perform(post("/api/on-going-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onGoingEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OnGoingEvent> onGoingEventList = onGoingEventRepository.findAll();
        assertThat(onGoingEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOnGoingEvents() throws Exception {
        // Initialize the database
        onGoingEventRepository.saveAndFlush(onGoingEvent);

        // Get all the onGoingEventList
        restOnGoingEventMockMvc.perform(get("/api/on-going-events?sort=idEventGo,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEventGo").value(hasItem(onGoingEvent.getIdEventGo().toString())))
            .andExpect(jsonPath("$.[*].idEvent").value(hasItem(DEFAULT_ID_EVENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void getOnGoingEvent() throws Exception {
        // Initialize the database
        onGoingEventRepository.saveAndFlush(onGoingEvent);

        // Get the onGoingEvent
        restOnGoingEventMockMvc.perform(get("/api/on-going-events/{id}", onGoingEvent.getIdEventGo()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idEventGo").value(onGoingEvent.getIdEventGo().toString()))
            .andExpect(jsonPath("$.idEvent").value(DEFAULT_ID_EVENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateThru").value(sameInstant(DEFAULT_DATE_THRU)));
    }

    @Test
    @Transactional
    public void getNonExistingOnGoingEvent() throws Exception {
        // Get the onGoingEvent
        restOnGoingEventMockMvc.perform(get("/api/on-going-events/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOnGoingEvent() throws Exception {
        // Initialize the database
        onGoingEventRepository.saveAndFlush(onGoingEvent);
        onGoingEventSearchRepository.save(onGoingEvent);
        int databaseSizeBeforeUpdate = onGoingEventRepository.findAll().size();

        // Update the onGoingEvent
        OnGoingEvent updatedOnGoingEvent = onGoingEventRepository.findOne(onGoingEvent.getIdEventGo());
        updatedOnGoingEvent
            .idEvent(UPDATED_ID_EVENT)
            .description(UPDATED_DESCRIPTION)
            .dateFrom(UPDATED_DATE_FROM)
            .dateThru(UPDATED_DATE_THRU);
        OnGoingEventDTO onGoingEventDTO = onGoingEventMapper.toDto(updatedOnGoingEvent);

        restOnGoingEventMockMvc.perform(put("/api/on-going-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onGoingEventDTO)))
            .andExpect(status().isOk());

        // Validate the OnGoingEvent in the database
        List<OnGoingEvent> onGoingEventList = onGoingEventRepository.findAll();
        assertThat(onGoingEventList).hasSize(databaseSizeBeforeUpdate);
        OnGoingEvent testOnGoingEvent = onGoingEventList.get(onGoingEventList.size() - 1);
        assertThat(testOnGoingEvent.getIdEvent()).isEqualTo(UPDATED_ID_EVENT);
        assertThat(testOnGoingEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOnGoingEvent.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testOnGoingEvent.getDateThru()).isEqualTo(UPDATED_DATE_THRU);

        // Validate the OnGoingEvent in Elasticsearch
        OnGoingEvent onGoingEventEs = onGoingEventSearchRepository.findOne(testOnGoingEvent.getIdEventGo());
        assertThat(onGoingEventEs).isEqualToComparingFieldByField(testOnGoingEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingOnGoingEvent() throws Exception {
        int databaseSizeBeforeUpdate = onGoingEventRepository.findAll().size();

        // Create the OnGoingEvent
        OnGoingEventDTO onGoingEventDTO = onGoingEventMapper.toDto(onGoingEvent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOnGoingEventMockMvc.perform(put("/api/on-going-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onGoingEventDTO)))
            .andExpect(status().isCreated());

        // Validate the OnGoingEvent in the database
        List<OnGoingEvent> onGoingEventList = onGoingEventRepository.findAll();
        assertThat(onGoingEventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOnGoingEvent() throws Exception {
        // Initialize the database
        onGoingEventRepository.saveAndFlush(onGoingEvent);
        onGoingEventSearchRepository.save(onGoingEvent);
        int databaseSizeBeforeDelete = onGoingEventRepository.findAll().size();

        // Get the onGoingEvent
        restOnGoingEventMockMvc.perform(delete("/api/on-going-events/{id}", onGoingEvent.getIdEventGo())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean onGoingEventExistsInEs = onGoingEventSearchRepository.exists(onGoingEvent.getIdEventGo());
        assertThat(onGoingEventExistsInEs).isFalse();

        // Validate the database is empty
        List<OnGoingEvent> onGoingEventList = onGoingEventRepository.findAll();
        assertThat(onGoingEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOnGoingEvent() throws Exception {
        // Initialize the database
        onGoingEventRepository.saveAndFlush(onGoingEvent);
        onGoingEventSearchRepository.save(onGoingEvent);

        // Search the onGoingEvent
        restOnGoingEventMockMvc.perform(get("/api/_search/on-going-events?query=idEventGo:" + onGoingEvent.getIdEventGo()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idEventGo").value(hasItem(onGoingEvent.getIdEventGo().toString())))
            .andExpect(jsonPath("$.[*].idEvent").value(hasItem(DEFAULT_ID_EVENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateThru").value(hasItem(sameInstant(DEFAULT_DATE_THRU))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnGoingEvent.class);
        /*
        OnGoingEvent onGoingEvent1 = new OnGoingEvent();
<<<<<<< HEAD
        onGoingEvent1.setIdEventGo(1L);
=======
        onGoingEvent1.setId(1L);
>>>>>>> Branch_v4.5.4
        OnGoingEvent onGoingEvent2 = new OnGoingEvent();
        onGoingEvent2.setIdEventGo(onGoingEvent1.getId());
        assertThat(onGoingEvent1).isEqualTo(onGoingEvent2);
<<<<<<< HEAD
        onGoingEvent2.setIdEventGo(2L);
=======
        onGoingEvent2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(onGoingEvent1).isNotEqualTo(onGoingEvent2);
        onGoingEvent1.setIdEventGo(null);
        assertThat(onGoingEvent1).isNotEqualTo(onGoingEvent2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnGoingEventDTO.class);
        /*
        OnGoingEventDTO onGoingEventDTO1 = new OnGoingEventDTO();
<<<<<<< HEAD
        onGoingEventDTO1.setIdEventGo(1L);
=======
        onGoingEventDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        OnGoingEventDTO onGoingEventDTO2 = new OnGoingEventDTO();
        assertThat(onGoingEventDTO1).isNotEqualTo(onGoingEventDTO2);
        onGoingEventDTO2.setIdEventGo(onGoingEventDTO1.getIdEventGo());
        assertThat(onGoingEventDTO1).isEqualTo(onGoingEventDTO2);
<<<<<<< HEAD
        onGoingEventDTO2.setIdEventGo(2L);
=======
        onGoingEventDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(onGoingEventDTO1).isNotEqualTo(onGoingEventDTO2);
        onGoingEventDTO1.setIdEventGo(null);
        assertThat(onGoingEventDTO1).isNotEqualTo(onGoingEventDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(onGoingEventMapper.fromidEventGo(42L).getidEventGo()).isEqualTo(42);
        //assertThat(onGoingEventMapper.fromIdEventGo(null)).isNull();
    }
}
