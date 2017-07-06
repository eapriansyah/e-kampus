package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity HostDataSource.
 */

@Entity
@Table(name = "host_data_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "hostdatasource")
public class HostDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idhosdatsou", columnDefinition = "BINARY(16)")
    private UUID idHostDataSource;

    @Column(name = "tabel_mahasiswa")
    private String tabelMahasiswa;

    @Column(name = "tabel_mata_kuliah")
    private String tabelMataKuliah;

    @Column(name = "tabel_dosen")
    private String tabelDosen;

    @Column(name = "tabel_kelas")
    private String tabelKelas;

    @Column(name = "tabel_nilai")
    private String tabelNilai;

    @Column(name = "class_name")
    private String className;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "jhi_password")
    private String password;

    @Column(name = "jdbc_url")
    private String jdbcUrl;

    @Column(name = "connection_time_out")
    private Integer connectionTimeOut;

    @Column(name = "minimum_pool_size")
    private Integer minimumPoolSize;

    @Column(name = "maximum_pool_size")
    private Integer maximumPoolSize;

    @Column(name = "is_active")
    private String isActive;

    @ManyToOne
    @JoinColumn(name="idprodi", referencedColumnName="idparrol")
    private ProgramStudy prody;

    @ManyToOne
    @JoinColumn(name="idstupat", referencedColumnName="idstupat")
    private StudyPath studyPath;

    public UUID getIdHostDataSource() {
        return this.idHostDataSource;
    }

    public void setIdHostDataSource(UUID id) {
        this.idHostDataSource = id;
    }


    public String getTabelMahasiswa() {
        return tabelMahasiswa;
    }

    public HostDataSource tabelMahasiswa(String tabelMahasiswa) {
        this.tabelMahasiswa = tabelMahasiswa;
        return this;
    }

    public void setTabelMahasiswa(String tabelMahasiswa) {
        this.tabelMahasiswa = tabelMahasiswa;
    }

    public String getTabelMataKuliah() {
        return tabelMataKuliah;
    }

    public HostDataSource tabelMataKuliah(String tabelMataKuliah) {
        this.tabelMataKuliah = tabelMataKuliah;
        return this;
    }

    public void setTabelMataKuliah(String tabelMataKuliah) {
        this.tabelMataKuliah = tabelMataKuliah;
    }

    public String getTabelDosen() {
        return tabelDosen;
    }

    public HostDataSource tabelDosen(String tabelDosen) {
        this.tabelDosen = tabelDosen;
        return this;
    }

    public void setTabelDosen(String tabelDosen) {
        this.tabelDosen = tabelDosen;
    }

    public String getTabelKelas() {
        return tabelKelas;
    }

    public HostDataSource tabelKelas(String tabelKelas) {
        this.tabelKelas = tabelKelas;
        return this;
    }

    public void setTabelKelas(String tabelKelas) {
        this.tabelKelas = tabelKelas;
    }

    public String getTabelNilai() {
        return tabelNilai;
    }

    public HostDataSource tabelNilai(String tabelNilai) {
        this.tabelNilai = tabelNilai;
        return this;
    }

    public void setTabelNilai(String tabelNilai) {
        this.tabelNilai = tabelNilai;
    }

    public String getClassName() {
        return className;
    }

    public HostDataSource className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public HostDataSource userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public HostDataSource password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public HostDataSource jdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public HostDataSource connectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
        return this;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public Integer getMinimumPoolSize() {
        return minimumPoolSize;
    }

    public HostDataSource minimumPoolSize(Integer minimumPoolSize) {
        this.minimumPoolSize = minimumPoolSize;
        return this;
    }

    public void setMinimumPoolSize(Integer minimumPoolSize) {
        this.minimumPoolSize = minimumPoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public HostDataSource maximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getIsActive() {
        return isActive;
    }

    public HostDataSource isActive(String isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public ProgramStudy getPrody() {
        return prody;
    }

    public HostDataSource prody(ProgramStudy programStudy) {
        this.prody = programStudy;
        return this;
    }

    public void setPrody(ProgramStudy programStudy) {
        this.prody = programStudy;
    }

    public StudyPath getStudyPath() {
        return studyPath;
    }

    public HostDataSource studyPath(StudyPath studyPath) {
        this.studyPath = studyPath;
        return this;
    }

    public void setStudyPath(StudyPath studyPath) {
        this.studyPath = studyPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HostDataSource hostDataSource = (HostDataSource) o;
        if (hostDataSource.idHostDataSource == null || this.idHostDataSource == null) {
            return false;
        }
        return Objects.equals(this.idHostDataSource, hostDataSource.idHostDataSource);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idHostDataSource);
    }

    @Override
    public String toString() {
        return "HostDataSource{" +
            "idHostDataSource=" + this.idHostDataSource +
            ", tabelMahasiswa='" + getTabelMahasiswa() + "'" +
            ", tabelMataKuliah='" + getTabelMataKuliah() + "'" +
            ", tabelDosen='" + getTabelDosen() + "'" +
            ", tabelKelas='" + getTabelKelas() + "'" +
            ", tabelNilai='" + getTabelNilai() + "'" +
            ", className='" + getClassName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", jdbcUrl='" + getJdbcUrl() + "'" +
            ", connectionTimeOut='" + getConnectionTimeOut() + "'" +
            ", minimumPoolSize='" + getMinimumPoolSize() + "'" +
            ", maximumPoolSize='" + getMaximumPoolSize() + "'" +
            ", isActive='" + getIsActive() + "'" +
            '}';
    }
}
