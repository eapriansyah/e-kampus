package id.eara.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Faculty entity.
 * atiila consulting
 */

public class FacultyDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    private String idInternal;

    private String name;

    private UUID universityId;

    private String universityName;

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

    public UUID getUniversityId() {
        return universityId;
    }

    public void setUniversityId(UUID universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FacultyDTO facultyDTO = (FacultyDTO) o;
        if(facultyDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), facultyDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "FacultyDTO{" +
            "id=" + getIdPartyRole() +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
