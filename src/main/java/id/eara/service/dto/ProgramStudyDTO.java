package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ProgramStudy entity.
 * atiila consulting
 */

public class ProgramStudyDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    private String idInternal;

    private String name;

    private Integer status;

    private Long degreeId;

    private String degreeDescription;

    private UUID facultyId;

    private String facultyName;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }

    public String getIdInternal() {
        return idInternal;
    }

    public void setIdInternal(String idInternal) {
        this.idInternal = idInternal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long DegreeId) {
        this.degreeId = DegreeId;
    }

    public String getDegreeDescription() {
        return degreeDescription;
    }

    public void setDegreeDescription(String DegreeDescription) {
        this.degreeDescription = DegreeDescription;
    }

    public UUID getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(UUID FacultyId) {
        this.facultyId = FacultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String FacultyName) {
        this.facultyName = FacultyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProgramStudyDTO programStudyDTO = (ProgramStudyDTO) o;
        if(programStudyDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), programStudyDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "ProgramStudyDTO{" +
            "id=" + getIdPartyRole() +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
