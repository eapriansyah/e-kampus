package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.Location;
import id.eara.repository.LocationRepository;
import id.eara.service.LocationService;
import id.eara.repository.search.LocationSearchRepository;
import id.eara.service.dto.LocationDTO;
import id.eara.service.mapper.LocationMapper;
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
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class LocationResourceIntTest {

    private static final String DEFAULT_GEO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GEO_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationSearchRepository locationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLocationMockMvc;

    private Location location;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationResource locationResource = new LocationResource(locationService);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
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
    public static Location createEntity(EntityManager em) {
        Location location = new Location()
            .geoCode(DEFAULT_GEO_CODE)
            .description(DEFAULT_DESCRIPTION);
        return location;
    }

    @Before
    public void initTest() {
        locationSearchRepository.deleteAll();
        location = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getGeoCode()).isEqualTo(DEFAULT_GEO_CODE);
        assertThat(testLocation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Location in Elasticsearch
        Location locationEs = locationSearchRepository.findOne(testLocation.getIdGeoBoundary());
        assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
        keyEntity = testLocation.getIdGeoBoundary();
    }

    @Test
    @Transactional
    public void createLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location with an existing ID
        location.setIdGeoBoundary(keyEntity);
        LocationDTO locationDTO = locationMapper.toDto(location);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc.perform(post("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc.perform(get("/api/locations?sort=idGeoBoundary,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idGeoBoundary").value(hasItem(location.getIdGeoBoundary().toString())))
            .andExpect(jsonPath("$.[*].geoCode").value(hasItem(DEFAULT_GEO_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getIdGeoBoundary()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idGeoBoundary").value(location.getIdGeoBoundary().toString()))
            .andExpect(jsonPath("$.geoCode").value(DEFAULT_GEO_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        Location updatedLocation = locationRepository.findOne(location.getIdGeoBoundary());
        updatedLocation
            .geoCode(UPDATED_GEO_CODE)
            .description(UPDATED_DESCRIPTION);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locationList.get(locationList.size() - 1);
        assertThat(testLocation.getGeoCode()).isEqualTo(UPDATED_GEO_CODE);
        assertThat(testLocation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Location in Elasticsearch
        Location locationEs = locationSearchRepository.findOne(testLocation.getIdGeoBoundary());
        assertThat(locationEs).isEqualToComparingFieldByField(testLocation);
    }

    @Test
    @Transactional
    public void updateNonExistingLocation() throws Exception {
        int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLocationMockMvc.perform(put("/api/locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationDTO)))
            .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);
        int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Get the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getIdGeoBoundary())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean locationExistsInEs = locationSearchRepository.exists(location.getIdGeoBoundary());
        assertThat(locationExistsInEs).isFalse();

        // Validate the database is empty
        List<Location> locationList = locationRepository.findAll();
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);
        locationSearchRepository.save(location);

        // Search the location
        restLocationMockMvc.perform(get("/api/_search/locations?query=idGeoBoundary:" + location.getIdGeoBoundary()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idGeoBoundary").value(hasItem(location.getIdGeoBoundary().toString())))
            .andExpect(jsonPath("$.[*].geoCode").value(hasItem(DEFAULT_GEO_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        /*
        Location location1 = new Location();
<<<<<<< HEAD
        location1.setIdGeoBoundary(1L);
=======
        location1.setId(1L);
>>>>>>> Branch_v4.5.4
        Location location2 = new Location();
        location2.setIdGeoBoundary(location1.getId());
        assertThat(location1).isEqualTo(location2);
<<<<<<< HEAD
        location2.setIdGeoBoundary(2L);
=======
        location2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(location1).isNotEqualTo(location2);
        location1.setIdGeoBoundary(null);
        assertThat(location1).isNotEqualTo(location2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationDTO.class);
        /*
        LocationDTO locationDTO1 = new LocationDTO();
<<<<<<< HEAD
        locationDTO1.setIdGeoBoundary(1L);
=======
        locationDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        LocationDTO locationDTO2 = new LocationDTO();
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO2.setIdGeoBoundary(locationDTO1.getIdGeoBoundary());
        assertThat(locationDTO1).isEqualTo(locationDTO2);
<<<<<<< HEAD
        locationDTO2.setIdGeoBoundary(2L);
=======
        locationDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO1.setIdGeoBoundary(null);
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(locationMapper.fromidGeoBoundary(42L).getidGeoBoundary()).isEqualTo(42);
        //assertThat(locationMapper.fromIdGeoBoundary(null)).isNull();
    }
}
