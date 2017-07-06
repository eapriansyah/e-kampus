package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the HostDataSource entity.
 * atiila consulting
 */

public class HostDataSourceDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idHostDataSource;

    private String tabelMahasiswa;

    private String tabelMataKuliah;

    private String tabelDosen;

    private String tabelKelas;

    private String tabelNilai;

    private String className;

    private String userName;

    private String password;

    private String jdbcUrl;

    private Integer connectionTimeOut;

    private Integer minimumPoolSize;

    private Integer maximumPoolSize;

    private String isActive;

    private UUID prodyId;

    private String prodyName;

    private Long studyPathId;

    private String studyPathDescription;

    public UUID getIdHostDataSource() {
        return this.idHostDataSource;
    }

    public void setIdHostDataSource(UUID id) {
        this.idHostDataSource = id;
    }

    public String getTabelMahasiswa() {
        return tabelMahasiswa;
    }

    public void setTabelMahasiswa(String tabelMahasiswa) {
        this.tabelMahasiswa = tabelMahasiswa;
    }

    public String getTabelMataKuliah() {
        return tabelMataKuliah;
    }

    public void setTabelMataKuliah(String tabelMataKuliah) {
        this.tabelMataKuliah = tabelMataKuliah;
    }

    public String getTabelDosen() {
        return tabelDosen;
    }

    public void setTabelDosen(String tabelDosen) {
        this.tabelDosen = tabelDosen;
    }

    public String getTabelKelas() {
        return tabelKelas;
    }

    public void setTabelKelas(String tabelKelas) {
        this.tabelKelas = tabelKelas;
    }

    public String getTabelNilai() {
        return tabelNilai;
    }

    public void setTabelNilai(String tabelNilai) {
        this.tabelNilai = tabelNilai;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public Integer getMinimumPoolSize() {
        return minimumPoolSize;
    }

    public void setMinimumPoolSize(Integer minimumPoolSize) {
        this.minimumPoolSize = minimumPoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public UUID getProdyId() {
        return prodyId;
    }

    public void setProdyId(UUID programStudyId) {
        this.prodyId = programStudyId;
    }

    public String getProdyName() {
        return prodyName;
    }

    public void setProdyName(String programStudyName) {
        this.prodyName = programStudyName;
    }

    public Long getStudyPathId() {
        return studyPathId;
    }

    public void setStudyPathId(Long studyPathId) {
        this.studyPathId = studyPathId;
    }

    public String getStudyPathDescription() {
        return studyPathDescription;
    }

    public void setStudyPathDescription(String studyPathDescription) {
        this.studyPathDescription = studyPathDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HostDataSourceDTO hostDataSourceDTO = (HostDataSourceDTO) o;
        if(hostDataSourceDTO.getIdHostDataSource() == null || getIdHostDataSource() == null) {
            return false;
        }
        return Objects.equals(getIdHostDataSource(), hostDataSourceDTO.getIdHostDataSource());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdHostDataSource());
    }

    @Override
    public String toString() {
        return "HostDataSourceDTO{" +
            "id=" + getIdHostDataSource() +
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
            "}";
    }
}
