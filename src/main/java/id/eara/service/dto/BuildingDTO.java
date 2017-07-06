package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Building entity.
 * atiila consulting
 */

public class BuildingDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idFacility;

    private String facilityCode;

    private String description;

    private UUID zoneId;

    private String zoneDescription;

    public UUID getIdFacility() {
        return this.idFacility;
    }

    public void setIdFacility(UUID id) {
        this.idFacility = id;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getZoneId() {
        return zoneId;
    }

    public void setZoneId(UUID zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneDescription() {
        return zoneDescription;
    }

    public void setZoneDescription(String zoneDescription) {
        this.zoneDescription = zoneDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildingDTO buildingDTO = (BuildingDTO) o;
        if(buildingDTO.getIdFacility() == null || getIdFacility() == null) {
            return false;
        }
        return Objects.equals(getIdFacility(), buildingDTO.getIdFacility());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdFacility());
    }

    @Override
    public String toString() {
        return "BuildingDTO{" +
            "id=" + getIdFacility() +
            ", facilityCode='" + getFacilityCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
