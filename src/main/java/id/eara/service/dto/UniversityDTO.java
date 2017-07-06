package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the University entity.
 * atiila consulting
 */

public class UniversityDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    private String idInternal;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniversityDTO universityDTO = (UniversityDTO) o;
        if(universityDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), universityDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "UniversityDTO{" +
            "id=" + getIdPartyRole() +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
