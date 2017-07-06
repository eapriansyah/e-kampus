package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Zone entity.
 * atiila consulting
 */

public class ZoneDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idGeoBoundary;

    private String geoCode;

    private String description;

    private UUID parentId;

    private String parentDescription;

    public UUID getIdGeoBoundary() {
        return this.idGeoBoundary;
    }

    public void setIdGeoBoundary(UUID id) {
        this.idGeoBoundary = id;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID locationId) {
        this.parentId = locationId;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String locationDescription) {
        this.parentDescription = locationDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZoneDTO zoneDTO = (ZoneDTO) o;
        if(zoneDTO.getIdGeoBoundary() == null || getIdGeoBoundary() == null) {
            return false;
        }
        return Objects.equals(getIdGeoBoundary(), zoneDTO.getIdGeoBoundary());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdGeoBoundary());
    }

    @Override
    public String toString() {
        return "ZoneDTO{" +
            "id=" + getIdGeoBoundary() +
            ", geoCode='" + getGeoCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
