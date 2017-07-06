package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Internal entity.
 * atiila consulting
 */

public class InternalDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPartyRole;

    private String idInternal;

    private String name;

    private String description;

    private Integer idRoleType;

    private Integer idStatusType;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdRoleType() {
        return idRoleType;
    }

    public void setIdRoleType(Integer idRoleType) {
        this.idRoleType = idRoleType;
    }

    public Integer getIdStatusType() {
        return idStatusType;
    }

    public void setIdStatusType(Integer idStatusType) {
        this.idStatusType = idStatusType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InternalDTO internalDTO = (InternalDTO) o;
        if(internalDTO.getIdPartyRole() == null || getIdPartyRole() == null) {
            return false;
        }
        return Objects.equals(getIdPartyRole(), internalDTO.getIdPartyRole());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPartyRole());
    }

    @Override
    public String toString() {
        return "InternalDTO{" +
            "id=" + getIdPartyRole() +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idRoleType='" + getIdRoleType() + "'" +
            ", idStatusType='" + getIdStatusType() + "'" +
            "}";
    }
}
