package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.PostalAddress;
import id.eara.repository.PostalAddressRepository;
import id.eara.service.PostalAddressService;
import id.eara.repository.search.PostalAddressSearchRepository;
import id.eara.service.dto.PostalAddressDTO;
import id.eara.service.mapper.PostalAddressMapper;
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
 * Test class for the PostalAddressResource REST controller.
 *
 * @see PostalAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class PostalAddressResourceIntTest {

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private PostalAddressMapper postalAddressMapper;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private PostalAddressSearchRepository postalAddressSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostalAddressMockMvc;

    private PostalAddress postalAddress;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostalAddressResource postalAddressResource = new PostalAddressResource(postalAddressService);
        this.restPostalAddressMockMvc = MockMvcBuilders.standaloneSetup(postalAddressResource)
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
    public static PostalAddress createEntity(EntityManager em) {
        PostalAddress postalAddress = new PostalAddress()
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE);
        return postalAddress;
    }

    @Before
    public void initTest() {
        postalAddressSearchRepository.deleteAll();
        postalAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalAddress() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testPostalAddress.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testPostalAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPostalAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);

        // Validate the PostalAddress in Elasticsearch
        PostalAddress postalAddressEs = postalAddressSearchRepository.findOne(testPostalAddress.getIdContact());
        assertThat(postalAddressEs).isEqualToComparingFieldByField(testPostalAddress);
        keyEntity = testPostalAddress.getIdContact();
    }

    @Test
    @Transactional
    public void createPostalAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress with an existing ID
        postalAddress.setIdContact(keyEntity);
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostalAddresses() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList
        restPostalAddressMockMvc.perform(get("/api/postal-addresses?sort=idContact,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(postalAddress.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())));
    }

    @Test
    @Transactional
    public void getPostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", postalAddress.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idContact").value(postalAddress.getIdContact().toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostalAddress() throws Exception {
        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);
        postalAddressSearchRepository.save(postalAddress);
        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Update the postalAddress
        PostalAddress updatedPostalAddress = postalAddressRepository.findOne(postalAddress.getIdContact());
        updatedPostalAddress
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE);
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(updatedPostalAddress);

        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testPostalAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testPostalAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPostalAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);

        // Validate the PostalAddress in Elasticsearch
        PostalAddress postalAddressEs = postalAddressSearchRepository.findOne(testPostalAddress.getIdContact());
        assertThat(postalAddressEs).isEqualToComparingFieldByField(testPostalAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalAddress() throws Exception {
        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Create the PostalAddress
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);
        postalAddressSearchRepository.save(postalAddress);
        int databaseSizeBeforeDelete = postalAddressRepository.findAll().size();

        // Get the postalAddress
        restPostalAddressMockMvc.perform(delete("/api/postal-addresses/{id}", postalAddress.getIdContact())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean postalAddressExistsInEs = postalAddressSearchRepository.exists(postalAddress.getIdContact());
        assertThat(postalAddressExistsInEs).isFalse();

        // Validate the database is empty
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);
        postalAddressSearchRepository.save(postalAddress);

        // Search the postalAddress
        restPostalAddressMockMvc.perform(get("/api/_search/postal-addresses?query=idContact:" + postalAddress.getIdContact()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idContact").value(hasItem(postalAddress.getIdContact().toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddress.class);
        /*
        PostalAddress postalAddress1 = new PostalAddress();
<<<<<<< HEAD
        postalAddress1.setIdContact(1L);
=======
        postalAddress1.setId(1L);
>>>>>>> Branch_v4.5.4
        PostalAddress postalAddress2 = new PostalAddress();
        postalAddress2.setIdContact(postalAddress1.getId());
        assertThat(postalAddress1).isEqualTo(postalAddress2);
<<<<<<< HEAD
        postalAddress2.setIdContact(2L);
=======
        postalAddress2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
        postalAddress1.setIdContact(null);
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddressDTO.class);
        /*
        PostalAddressDTO postalAddressDTO1 = new PostalAddressDTO();
<<<<<<< HEAD
        postalAddressDTO1.setIdContact(1L);
=======
        postalAddressDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        PostalAddressDTO postalAddressDTO2 = new PostalAddressDTO();
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
        postalAddressDTO2.setIdContact(postalAddressDTO1.getIdContact());
        assertThat(postalAddressDTO1).isEqualTo(postalAddressDTO2);
<<<<<<< HEAD
        postalAddressDTO2.setIdContact(2L);
=======
        postalAddressDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
        postalAddressDTO1.setIdContact(null);
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(postalAddressMapper.fromidContact(42L).getidContact()).isEqualTo(42);
        //assertThat(postalAddressMapper.fromIdContact(null)).isNull();
    }
}
