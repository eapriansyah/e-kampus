package id.eara.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Student entity.
 * atiila consulting
 */

public class StudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    @NotNull
    private String nim;

    private String name;

    private Integer classof;

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

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassof() {
        return classof;
    }

    public void setClassof(Integer classof) {
        this.classof = classof;
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

    public void setProdyId(UUID ProgramStudyId) {
        this.prodyId = ProgramStudyId;
    }

    public String getProdyName() {
        return prodyName;
    }

    public void setProdyName(String ProgramStudyName) {
        this.prodyName = ProgramStudyName;
    }

    public Long getStudyPathId() {
        return studyPathId;
    }

    public void setStudyPathId(Long StudyPathId) {
        this.studyPathId = StudyPathId;
    }

    public String getStudyPathDescription() {
        return studyPathDescription;
    }

    public void setStudyPathDescription(String StudyPathDescription) {
        this.studyPathDescription = StudyPathDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudentDTO studentDTO = (StudentDTO) o;
        if(studentDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), studentDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
            "id=" + getIdPartyRole() +
            ", nim='" + getNim() + "'" +
            ", name='" + getName() + "'" +
            ", classof='" + getClassof() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
