package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ClassRoom entity.
 * atiila consulting
 */

public class ClassRoomDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idFacility;

    private String facilityCode;

    private String description;

    private UUID buildingId;

    private String buildingDescription;

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

    public UUID getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(UUID buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassRoomDTO classRoomDTO = (ClassRoomDTO) o;
        if(classRoomDTO.getIdFacility() == null || getIdFacility() == null) {
            return false;
        }
        return Objects.equals(getIdFacility(), classRoomDTO.getIdFacility());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdFacility());
    }

    @Override
    public String toString() {
        return "ClassRoomDTO{" +
            "id=" + getIdFacility() +
            ", facilityCode='" + getFacilityCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
