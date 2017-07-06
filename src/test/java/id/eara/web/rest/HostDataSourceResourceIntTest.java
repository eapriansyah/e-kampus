package id.eara.web.rest;

import id.eara.KampusApp;

import id.eara.domain.HostDataSource;
import id.eara.repository.HostDataSourceRepository;
import id.eara.service.HostDataSourceService;
import id.eara.repository.search.HostDataSourceSearchRepository;
import id.eara.service.dto.HostDataSourceDTO;
import id.eara.service.mapper.HostDataSourceMapper;
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
 * Test class for the HostDataSourceResource REST controller.
 *
 * @see HostDataSourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KampusApp.class)
public class HostDataSourceResourceIntTest {

    private static final String DEFAULT_TABEL_MAHASISWA = "AAAAAAAAAA";
    private static final String UPDATED_TABEL_MAHASISWA = "BBBBBBBBBB";

    private static final String DEFAULT_TABEL_MATA_KULIAH = "AAAAAAAAAA";
    private static final String UPDATED_TABEL_MATA_KULIAH = "BBBBBBBBBB";

    private static final String DEFAULT_TABEL_DOSEN = "AAAAAAAAAA";
    private static final String UPDATED_TABEL_DOSEN = "BBBBBBBBBB";

    private static final String DEFAULT_TABEL_KELAS = "AAAAAAAAAA";
    private static final String UPDATED_TABEL_KELAS = "BBBBBBBBBB";

    private static final String DEFAULT_TABEL_NILAI = "AAAAAAAAAA";
    private static final String UPDATED_TABEL_NILAI = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_JDBC_URL = "AAAAAAAAAA";
    private static final String UPDATED_JDBC_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONNECTION_TIME_OUT = 1;
    private static final Integer UPDATED_CONNECTION_TIME_OUT = 2;

    private static final Integer DEFAULT_MINIMUM_POOL_SIZE = 1;
    private static final Integer UPDATED_MINIMUM_POOL_SIZE = 2;

    private static final Integer DEFAULT_MAXIMUM_POOL_SIZE = 1;
    private static final Integer UPDATED_MAXIMUM_POOL_SIZE = 2;

    private static final String DEFAULT_IS_ACTIVE = "AAAAAAAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBBBBBBB";

    @Autowired
    private HostDataSourceRepository hostDataSourceRepository;

    @Autowired
    private HostDataSourceMapper hostDataSourceMapper;

    @Autowired
    private HostDataSourceService hostDataSourceService;

    @Autowired
    private HostDataSourceSearchRepository hostDataSourceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHostDataSourceMockMvc;

    private HostDataSource hostDataSource;

    private UUID keyEntity = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HostDataSourceResource hostDataSourceResource = new HostDataSourceResource(hostDataSourceService);
        this.restHostDataSourceMockMvc = MockMvcBuilders.standaloneSetup(hostDataSourceResource)
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
    public static HostDataSource createEntity(EntityManager em) {
        HostDataSource hostDataSource = new HostDataSource()
            .tabelMahasiswa(DEFAULT_TABEL_MAHASISWA)
            .tabelMataKuliah(DEFAULT_TABEL_MATA_KULIAH)
            .tabelDosen(DEFAULT_TABEL_DOSEN)
            .tabelKelas(DEFAULT_TABEL_KELAS)
            .tabelNilai(DEFAULT_TABEL_NILAI)
            .className(DEFAULT_CLASS_NAME)
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .jdbcUrl(DEFAULT_JDBC_URL)
            .connectionTimeOut(DEFAULT_CONNECTION_TIME_OUT)
            .minimumPoolSize(DEFAULT_MINIMUM_POOL_SIZE)
            .maximumPoolSize(DEFAULT_MAXIMUM_POOL_SIZE)
            .isActive(DEFAULT_IS_ACTIVE);
        return hostDataSource;
    }

    @Before
    public void initTest() {
        hostDataSourceSearchRepository.deleteAll();
        hostDataSource = createEntity(em);
    }

    @Test
    @Transactional
    public void createHostDataSource() throws Exception {
        int databaseSizeBeforeCreate = hostDataSourceRepository.findAll().size();

        // Create the HostDataSource
        HostDataSourceDTO hostDataSourceDTO = hostDataSourceMapper.toDto(hostDataSource);

        restHostDataSourceMockMvc.perform(post("/api/host-data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDataSourceDTO)))
            .andExpect(status().isCreated());

        // Validate the HostDataSource in the database
        List<HostDataSource> hostDataSourceList = hostDataSourceRepository.findAll();
        assertThat(hostDataSourceList).hasSize(databaseSizeBeforeCreate + 1);
        HostDataSource testHostDataSource = hostDataSourceList.get(hostDataSourceList.size() - 1);
        assertThat(testHostDataSource.getTabelMahasiswa()).isEqualTo(DEFAULT_TABEL_MAHASISWA);
        assertThat(testHostDataSource.getTabelMataKuliah()).isEqualTo(DEFAULT_TABEL_MATA_KULIAH);
        assertThat(testHostDataSource.getTabelDosen()).isEqualTo(DEFAULT_TABEL_DOSEN);
        assertThat(testHostDataSource.getTabelKelas()).isEqualTo(DEFAULT_TABEL_KELAS);
        assertThat(testHostDataSource.getTabelNilai()).isEqualTo(DEFAULT_TABEL_NILAI);
        assertThat(testHostDataSource.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testHostDataSource.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testHostDataSource.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testHostDataSource.getJdbcUrl()).isEqualTo(DEFAULT_JDBC_URL);
        assertThat(testHostDataSource.getConnectionTimeOut()).isEqualTo(DEFAULT_CONNECTION_TIME_OUT);
        assertThat(testHostDataSource.getMinimumPoolSize()).isEqualTo(DEFAULT_MINIMUM_POOL_SIZE);
        assertThat(testHostDataSource.getMaximumPoolSize()).isEqualTo(DEFAULT_MAXIMUM_POOL_SIZE);
        assertThat(testHostDataSource.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the HostDataSource in Elasticsearch
        HostDataSource hostDataSourceEs = hostDataSourceSearchRepository.findOne(testHostDataSource.getIdHostDataSource());
        assertThat(hostDataSourceEs).isEqualToComparingFieldByField(testHostDataSource);
        keyEntity = testHostDataSource.getIdHostDataSource();
    }

    @Test
    @Transactional
    public void createHostDataSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hostDataSourceRepository.findAll().size();

        // Create the HostDataSource with an existing ID
        hostDataSource.setIdHostDataSource(keyEntity);
        HostDataSourceDTO hostDataSourceDTO = hostDataSourceMapper.toDto(hostDataSource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostDataSourceMockMvc.perform(post("/api/host-data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDataSourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HostDataSource> hostDataSourceList = hostDataSourceRepository.findAll();
        assertThat(hostDataSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHostDataSources() throws Exception {
        // Initialize the database
        hostDataSourceRepository.saveAndFlush(hostDataSource);

        // Get all the hostDataSourceList
        restHostDataSourceMockMvc.perform(get("/api/host-data-sources?sort=idHostDataSource,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idHostDataSource").value(hasItem(hostDataSource.getIdHostDataSource().toString())))
            .andExpect(jsonPath("$.[*].tabelMahasiswa").value(hasItem(DEFAULT_TABEL_MAHASISWA.toString())))
            .andExpect(jsonPath("$.[*].tabelMataKuliah").value(hasItem(DEFAULT_TABEL_MATA_KULIAH.toString())))
            .andExpect(jsonPath("$.[*].tabelDosen").value(hasItem(DEFAULT_TABEL_DOSEN.toString())))
            .andExpect(jsonPath("$.[*].tabelKelas").value(hasItem(DEFAULT_TABEL_KELAS.toString())))
            .andExpect(jsonPath("$.[*].tabelNilai").value(hasItem(DEFAULT_TABEL_NILAI.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].jdbcUrl").value(hasItem(DEFAULT_JDBC_URL.toString())))
            .andExpect(jsonPath("$.[*].connectionTimeOut").value(hasItem(DEFAULT_CONNECTION_TIME_OUT)))
            .andExpect(jsonPath("$.[*].minimumPoolSize").value(hasItem(DEFAULT_MINIMUM_POOL_SIZE)))
            .andExpect(jsonPath("$.[*].maximumPoolSize").value(hasItem(DEFAULT_MAXIMUM_POOL_SIZE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.toString())));
    }

    @Test
    @Transactional
    public void getHostDataSource() throws Exception {
        // Initialize the database
        hostDataSourceRepository.saveAndFlush(hostDataSource);

        // Get the hostDataSource
        restHostDataSourceMockMvc.perform(get("/api/host-data-sources/{id}", hostDataSource.getIdHostDataSource()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.idHostDataSource").value(hostDataSource.getIdHostDataSource().toString()))
            .andExpect(jsonPath("$.tabelMahasiswa").value(DEFAULT_TABEL_MAHASISWA.toString()))
            .andExpect(jsonPath("$.tabelMataKuliah").value(DEFAULT_TABEL_MATA_KULIAH.toString()))
            .andExpect(jsonPath("$.tabelDosen").value(DEFAULT_TABEL_DOSEN.toString()))
            .andExpect(jsonPath("$.tabelKelas").value(DEFAULT_TABEL_KELAS.toString()))
            .andExpect(jsonPath("$.tabelNilai").value(DEFAULT_TABEL_NILAI.toString()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.jdbcUrl").value(DEFAULT_JDBC_URL.toString()))
            .andExpect(jsonPath("$.connectionTimeOut").value(DEFAULT_CONNECTION_TIME_OUT))
            .andExpect(jsonPath("$.minimumPoolSize").value(DEFAULT_MINIMUM_POOL_SIZE))
            .andExpect(jsonPath("$.maximumPoolSize").value(DEFAULT_MAXIMUM_POOL_SIZE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHostDataSource() throws Exception {
        // Get the hostDataSource
        restHostDataSourceMockMvc.perform(get("/api/host-data-sources/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHostDataSource() throws Exception {
        // Initialize the database
        hostDataSourceRepository.saveAndFlush(hostDataSource);
        hostDataSourceSearchRepository.save(hostDataSource);
        int databaseSizeBeforeUpdate = hostDataSourceRepository.findAll().size();

        // Update the hostDataSource
        HostDataSource updatedHostDataSource = hostDataSourceRepository.findOne(hostDataSource.getIdHostDataSource());
        updatedHostDataSource
            .tabelMahasiswa(UPDATED_TABEL_MAHASISWA)
            .tabelMataKuliah(UPDATED_TABEL_MATA_KULIAH)
            .tabelDosen(UPDATED_TABEL_DOSEN)
            .tabelKelas(UPDATED_TABEL_KELAS)
            .tabelNilai(UPDATED_TABEL_NILAI)
            .className(UPDATED_CLASS_NAME)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .jdbcUrl(UPDATED_JDBC_URL)
            .connectionTimeOut(UPDATED_CONNECTION_TIME_OUT)
            .minimumPoolSize(UPDATED_MINIMUM_POOL_SIZE)
            .maximumPoolSize(UPDATED_MAXIMUM_POOL_SIZE)
            .isActive(UPDATED_IS_ACTIVE);
        HostDataSourceDTO hostDataSourceDTO = hostDataSourceMapper.toDto(updatedHostDataSource);

        restHostDataSourceMockMvc.perform(put("/api/host-data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDataSourceDTO)))
            .andExpect(status().isOk());

        // Validate the HostDataSource in the database
        List<HostDataSource> hostDataSourceList = hostDataSourceRepository.findAll();
        assertThat(hostDataSourceList).hasSize(databaseSizeBeforeUpdate);
        HostDataSource testHostDataSource = hostDataSourceList.get(hostDataSourceList.size() - 1);
        assertThat(testHostDataSource.getTabelMahasiswa()).isEqualTo(UPDATED_TABEL_MAHASISWA);
        assertThat(testHostDataSource.getTabelMataKuliah()).isEqualTo(UPDATED_TABEL_MATA_KULIAH);
        assertThat(testHostDataSource.getTabelDosen()).isEqualTo(UPDATED_TABEL_DOSEN);
        assertThat(testHostDataSource.getTabelKelas()).isEqualTo(UPDATED_TABEL_KELAS);
        assertThat(testHostDataSource.getTabelNilai()).isEqualTo(UPDATED_TABEL_NILAI);
        assertThat(testHostDataSource.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testHostDataSource.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testHostDataSource.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testHostDataSource.getJdbcUrl()).isEqualTo(UPDATED_JDBC_URL);
        assertThat(testHostDataSource.getConnectionTimeOut()).isEqualTo(UPDATED_CONNECTION_TIME_OUT);
        assertThat(testHostDataSource.getMinimumPoolSize()).isEqualTo(UPDATED_MINIMUM_POOL_SIZE);
        assertThat(testHostDataSource.getMaximumPoolSize()).isEqualTo(UPDATED_MAXIMUM_POOL_SIZE);
        assertThat(testHostDataSource.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the HostDataSource in Elasticsearch
        HostDataSource hostDataSourceEs = hostDataSourceSearchRepository.findOne(testHostDataSource.getIdHostDataSource());
        assertThat(hostDataSourceEs).isEqualToComparingFieldByField(testHostDataSource);
    }

    @Test
    @Transactional
    public void updateNonExistingHostDataSource() throws Exception {
        int databaseSizeBeforeUpdate = hostDataSourceRepository.findAll().size();

        // Create the HostDataSource
        HostDataSourceDTO hostDataSourceDTO = hostDataSourceMapper.toDto(hostDataSource);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHostDataSourceMockMvc.perform(put("/api/host-data-sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hostDataSourceDTO)))
            .andExpect(status().isCreated());

        // Validate the HostDataSource in the database
        List<HostDataSource> hostDataSourceList = hostDataSourceRepository.findAll();
        assertThat(hostDataSourceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHostDataSource() throws Exception {
        // Initialize the database
        hostDataSourceRepository.saveAndFlush(hostDataSource);
        hostDataSourceSearchRepository.save(hostDataSource);
        int databaseSizeBeforeDelete = hostDataSourceRepository.findAll().size();

        // Get the hostDataSource
        restHostDataSourceMockMvc.perform(delete("/api/host-data-sources/{id}", hostDataSource.getIdHostDataSource())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean hostDataSourceExistsInEs = hostDataSourceSearchRepository.exists(hostDataSource.getIdHostDataSource());
        assertThat(hostDataSourceExistsInEs).isFalse();

        // Validate the database is empty
        List<HostDataSource> hostDataSourceList = hostDataSourceRepository.findAll();
        assertThat(hostDataSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHostDataSource() throws Exception {
        // Initialize the database
        hostDataSourceRepository.saveAndFlush(hostDataSource);
        hostDataSourceSearchRepository.save(hostDataSource);

        // Search the hostDataSource
        restHostDataSourceMockMvc.perform(get("/api/_search/host-data-sources?query=idHostDataSource:" + hostDataSource.getIdHostDataSource()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].idHostDataSource").value(hasItem(hostDataSource.getIdHostDataSource().toString())))
            .andExpect(jsonPath("$.[*].tabelMahasiswa").value(hasItem(DEFAULT_TABEL_MAHASISWA.toString())))
            .andExpect(jsonPath("$.[*].tabelMataKuliah").value(hasItem(DEFAULT_TABEL_MATA_KULIAH.toString())))
            .andExpect(jsonPath("$.[*].tabelDosen").value(hasItem(DEFAULT_TABEL_DOSEN.toString())))
            .andExpect(jsonPath("$.[*].tabelKelas").value(hasItem(DEFAULT_TABEL_KELAS.toString())))
            .andExpect(jsonPath("$.[*].tabelNilai").value(hasItem(DEFAULT_TABEL_NILAI.toString())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].jdbcUrl").value(hasItem(DEFAULT_JDBC_URL.toString())))
            .andExpect(jsonPath("$.[*].connectionTimeOut").value(hasItem(DEFAULT_CONNECTION_TIME_OUT)))
            .andExpect(jsonPath("$.[*].minimumPoolSize").value(hasItem(DEFAULT_MINIMUM_POOL_SIZE)))
            .andExpect(jsonPath("$.[*].maximumPoolSize").value(hasItem(DEFAULT_MAXIMUM_POOL_SIZE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HostDataSource.class);
        /*
        HostDataSource hostDataSource1 = new HostDataSource();
<<<<<<< HEAD
        hostDataSource1.setIdHostDataSource(1L);
=======
        hostDataSource1.setId(1L);
>>>>>>> Branch_v4.5.4
        HostDataSource hostDataSource2 = new HostDataSource();
        hostDataSource2.setIdHostDataSource(hostDataSource1.getId());
        assertThat(hostDataSource1).isEqualTo(hostDataSource2);
<<<<<<< HEAD
        hostDataSource2.setIdHostDataSource(2L);
=======
        hostDataSource2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(hostDataSource1).isNotEqualTo(hostDataSource2);
        hostDataSource1.setIdHostDataSource(null);
        assertThat(hostDataSource1).isNotEqualTo(hostDataSource2);
        */
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HostDataSourceDTO.class);
        /*
        HostDataSourceDTO hostDataSourceDTO1 = new HostDataSourceDTO();
<<<<<<< HEAD
        hostDataSourceDTO1.setIdHostDataSource(1L);
=======
        hostDataSourceDTO1.setId(1L);
>>>>>>> Branch_v4.5.4
        HostDataSourceDTO hostDataSourceDTO2 = new HostDataSourceDTO();
        assertThat(hostDataSourceDTO1).isNotEqualTo(hostDataSourceDTO2);
        hostDataSourceDTO2.setIdHostDataSource(hostDataSourceDTO1.getIdHostDataSource());
        assertThat(hostDataSourceDTO1).isEqualTo(hostDataSourceDTO2);
<<<<<<< HEAD
        hostDataSourceDTO2.setIdHostDataSource(2L);
=======
        hostDataSourceDTO2.setId(2L);
>>>>>>> Branch_v4.5.4
        assertThat(hostDataSourceDTO1).isNotEqualTo(hostDataSourceDTO2);
        hostDataSourceDTO1.setIdHostDataSource(null);
        assertThat(hostDataSourceDTO1).isNotEqualTo(hostDataSourceDTO2);
        */
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        //assertThat(hostDataSourceMapper.fromidHostDataSource(42L).getidHostDataSource()).isEqualTo(42);
        //assertThat(hostDataSourceMapper.fromIdHostDataSource(null)).isNull();
    }
}
