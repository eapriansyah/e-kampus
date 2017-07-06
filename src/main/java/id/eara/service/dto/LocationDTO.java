package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Location entity.
 * atiila consulting
 */

public class LocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idGeoBoundary;

    private String geoCode;

    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if(locationDTO.getIdGeoBoundary() == null || getIdGeoBoundary() == null) {
            return false;
        }
        return Objects.equals(getIdGeoBoundary(), locationDTO.getIdGeoBoundary());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdGeoBoundary());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getIdGeoBoundary() +
            ", geoCode='" + getGeoCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
