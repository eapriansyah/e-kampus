package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity Location.
 */

@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idgeobou", columnDefinition = "BINARY(16)")
    private UUID idGeoBoundary;

    @Column(name = "geo_code")
    private String geoCode;

    @Column(name = "description")
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

    public Location geoCode(String geoCode) {
        this.geoCode = geoCode;
        return this;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getDescription() {
        return description;
    }

    public Location description(String description) {
        this.description = description;
        return this;
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
        Location location = (Location) o;
        if (location.idGeoBoundary == null || this.idGeoBoundary == null) {
            return false;
        }
        return Objects.equals(this.idGeoBoundary, location.idGeoBoundary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idGeoBoundary);
    }

    @Override
    public String toString() {
        return "Location{" +
            "idGeoBoundary=" + this.idGeoBoundary +
            ", geoCode='" + getGeoCode() + "'" +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
