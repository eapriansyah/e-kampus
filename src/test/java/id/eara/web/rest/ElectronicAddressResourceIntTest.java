package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.ElectronicAddress;
import id.eara.repository.ElectronicAddressRepository;
import id.eara.service.ElectronicAddressService;
import id.eara.repository.search.ElectronicAddressSearchRepository;
import id.eara.service.dto.ElectronicAddressDTO;
import id.eara.service.mapper.ElectronicAddressMapper;
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
 * Test class for the ElectronicAddressResource REST controller.
 *
 * @see ElectronicAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class ElectronicAddressResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private ElectronicAddressRepository electronicAddressRepository;

    @Autowired
    private ElectronicAddressMapper electronicAddressMapper;

    @Autowired
    private ElectronicAddressService electronicAddressService;

    @Autowired
    private ElectronicAddressSearchRepository electronicAddressSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restElectronicAddressMockMvc;

    private ElectronicAddress electronicAddress;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ElectronicAddressResource electronicAddressResource = new ElectronicAddressResource(electronicAddressService);
        this.restElectronicAddressMockMvc = MockMvcBuilders.standaloneSetup(electronicAddressResource)
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
    public static ElectronicAddress createEntity(EntityManager em) {
        ElectronicAddress electronicAddress = new ElectronicAddress()
            .address(DEFAULT_ADDRESS);
        return electronicAddress;
    }

    @Before
    public void initTest() {
        electronicAddressSearchRepository.deleteAll();
        electronicAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createElectronicAddress() throws Exception {
        int databaseSizeBeforeCreate = electronicAddressRepository.findAll().size();

        // Create the ElectronicAddress
        ElectronicAddressDTO electronicAddressDTO = electronicAddressMapper.toDto(electronicAddress);

        restElectronicAddressMockMvc.perform(post("/api/electronic-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronicAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectronicAddress in the database
        List<ElectronicAddress> electronicAddressList = electronicAddressRepository.findAll();
        assertThat(electronicAddressList).hasSize(databaseSizeBeforeCreate + 1);
        ElectronicAddress testElectronicAddress = electronicAddressList.get(electronicAddressList.size() - 1);
        assertThat(testElectronicAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the ElectronicAddress in Elasticsearch
        ElectronicAddress electronicAddressEs = electronicAddressSearchRepository.findOne(testElectronicAddress.getIdContact());
        assertThat(electronicAddressEs).isEqualToComparingFieldByField(testElectronicAddress);
        keyEntity = testElectronicAddress.getIdContact();
    }

    @Test
    @Transactional
    public void createElectronicAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electronicAddressRepository.findAll().size();

        // Create the ElectronicAddress with an existing ID
        electronicAddress.setIdContact(keyEntity);
        ElectronicAddressDTO electronicAddressDTO = electronicAddressMapper.toDto(electronicAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectronicAddressMockMvc.perform(post("/api/electronic-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronicAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ElectronicAddress> electronicAddressList = electronicAddressRepository.findAll();
        assertThat(electronicAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElectronicAddresses() throws Exception {
        // Initialize the database
        electronicAddressRepository.saveAndFlush(electronicAddress);

        // Get all the electronicAddressList
        restElectronicAddressMockMvc.perform(get("/api/electronic-addresses?sort=idContact,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(electronicAddress.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getElectronicAddress() throws Exception {
        // Initialize the database
        electronicAddressRepository.saveAndFlush(electronicAddress);

        // Get the electronicAddress
        restElectronicAddressMockMvc.perform(get("/api/electronic-addresses/{id}", electronicAddress.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idContact").value(electronicAddress.getIdContact().toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingElectronicAddress() throws Exception {
        // Get the electronicAddress
        restElectronicAddressMockMvc.perform(get("/api/electronic-addresses/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectronicAddress() throws Exception {
        // Initialize the database
        electronicAddressRepository.saveAndFlush(electronicAddress);
        electronicAddressSearchRepository.save(electronicAddress);
        int databaseSizeBeforeUpdate = electronicAddressRepository.findAll().size();

        // Update the electronicAddress
        ElectronicAddress updatedElectronicAddress = electronicAddressRepository.findOne(electronicAddress.getIdContact());
        updatedElectronicAddress
            .address(UPDATED_ADDRESS);
        ElectronicAddressDTO electronicAddressDTO = electronicAddressMapper.toDto(updatedElectronicAddress);

        restElectronicAddressMockMvc.perform(put("/api/electronic-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronicAddressDTO)))
            .andExpect(status().isOk());

        // Validate the ElectronicAddress in the database
        List<ElectronicAddress> electronicAddressList = electronicAddressRepository.findAll();
        assertThat(electronicAddressList).hasSize(databaseSizeBeforeUpdate);
        ElectronicAddress testElectronicAddress = electronicAddressList.get(electronicAddressList.size() - 1);
        assertThat(testElectronicAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the ElectronicAddress in Elasticsearch
        ElectronicAddress electronicAddressEs = electronicAddressSearchRepository.findOne(testElectronicAddress.getIdContact());
        assertThat(electronicAddressEs).isEqualToComparingFieldByField(testElectronicAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingElectronicAddress() throws Exception {
        int databaseSizeBeforeUpdate = electronicAddressRepository.findAll().size();

        // Create the ElectronicAddress
        ElectronicAddressDTO electronicAddressDTO = electronicAddressMapper.toDto(electronicAddress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restElectronicAddressMockMvc.perform(put("/api/electronic-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronicAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectronicAddress in the database
        List<ElectronicAddress> electronicAddressList = electronicAddressRepository.findAll();
        assertThat(electronicAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteElectronicAddress() throws Exception {
        // Initialize the database
        electronicAddressRepository.saveAndFlush(electronicAddress);
        electronicAddressSearchRepository.save(electronicAddress);
        int databaseSizeBeforeDelete = electronicAddressRepository.findAll().size();

        // Get the electronicAddress
        restElectronicAddressMockMvc.perform(delete("/api/electronic-addresses/{id}", electronicAddress.getIdContact())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean electronicAddressExistsInEs = electronicAddressSearchRepository.exists(electronicAddress.getIdContact());
        assertThat(electronicAddressExistsInEs).isFalse();

        // Validate the database is empty
        List<ElectronicAddress> electronicAddressList = electronicAddressRepository.findAll();
        assertThat(electronicAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchElectronicAddress() throws Exception {
        // Initialize the database
        electronicAddressRepository.saveAndFlush(electronicAddress);
        electronicAddressSearchRepository.save(electronicAddress);

        // Search the electronicAddress
        restElectronicAddressMockMvc.perform(get("/api/_search/electronic-addresses?query=idContact:" + electronicAddress.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(electronicAddress.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronicAddress.class);
        /*
        ElectronicAddress electronicAddress1 = new ElectronicAddress();
<<<<<<< HEAD
        electronicAddress1.setIdContact(1L);
=======
        electronicAddress1.setId(1L);
>>>>>>> Branch_v4.5.4
        ElectronicAddress electronicAddress2 = new ElectronicAddress();
        electronicAddress2.setIdContact(electronicAddress1.getId());
        assertThat(electronicAddress1).isEqualTo(electronicAddress2);
<<<<<<< HEAD
        electronicAddress2.setIdContact(2L);
=======
        electronicAddress2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(electronicAddress1).isNotEqualTo(electronicAddress2);
        electronicAddress1.setIdContact(null);
        assertThat(electronicAddress1).isNotEqualTo(electronicAddress2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronicAddressDTO.class);
        /*
        ElectronicAddressDTO electronicAddressDTO1 = new ElectronicAddressDTO();
<<<<<<< HEAD
        electronicAddressDTO1.setIdContact(1L);
=======
        electronicAddressDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        ElectronicAddressDTO electronicAddressDTO2 = new ElectronicAddressDTO();
        assertThat(electronicAddressDTO1).isNotEqualTo(electronicAddressDTO2);
        electronicAddressDTO2.setIdContact(electronicAddressDTO1.getIdContact());
        assertThat(electronicAddressDTO1).isEqualTo(electronicAddressDTO2);
<<<<<<< HEAD
        electronicAddressDTO2.setIdContact(2L);
=======
        electronicAddressDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(electronicAddressDTO1).isNotEqualTo(electronicAddressDTO2);
        electronicAddressDTO1.setIdContact(null);
        assertThat(electronicAddressDTO1).isNotEqualTo(electronicAddressDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(electronicAddressMapper.fromidContact(42L).getidContact()).isEqualTo(42);
        //assertThat(electronicAddressMapper.fromIdContact(null)).isNull();
    }
}
