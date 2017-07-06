package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the PreStudent entity.
 * atiila consulting
 */

public class PreStudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    private String idPreStudent;

    private String name;

    private Integer yearOf;

    private Integer status;

    private UUID prodyId;

    private String prodyName;

    private Long studyPathId;

    private String studyPathDescription;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }

    public String getIdPreStudent() {
        return idPreStudent;
    }

    public void setIdPreStudent(String idPreStudent) {
        this.idPreStudent = idPreStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearOf() {
        return yearOf;
    }

    public void setYearOf(Integer yearOf) {
        this.yearOf = yearOf;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        PreStudentDTO preStudentDTO = (PreStudentDTO) o;
        if(preStudentDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), preStudentDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "PreStudentDTO{" +
            "id=" + getIdPartyRole() +
            ", idPreStudent='" + getIdPreStudent() + "'" +
            ", name='" + getName() + "'" +
            ", yearOf='" + getYearOf() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
