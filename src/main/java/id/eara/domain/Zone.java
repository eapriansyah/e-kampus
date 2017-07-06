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
 * Class definition for Entity Zone.
 */

@Entity
@Table(name = "zone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "zone")
public class Zone implements Serializable {

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

    @ManyToOne
    @JoinColumn(name="idparent", referencedColumnName="idgeobou")
    private Location parent;

    public UUID getIdGeoBoundary() {
        return this.idGeoBoundary;
    }

    public void setIdGeoBoundary(UUID id) {
        this.idGeoBoundary = id;
    }


    public String getGeoCode() {
        return geoCode;
    }

    public Zone geoCode(String geoCode) {
        this.geoCode = geoCode;
        return this;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getDescription() {
        return description;
    }

    public Zone description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getParent() {
        return parent;
    }

    public Zone parent(Location location) {
        this.parent = location;
        return this;
    }

    public void setParent(Location location) {
        this.parent = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Zone zone = (Zone) o;
        if (zone.idGeoBoundary == null || this.idGeoBoundary == null) {
            return false;
        }
        return Objects.equals(this.idGeoBoundary, zone.idGeoBoundary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idGeoBoundary);
    }

    @Override
    public String toString() {
        return "Zone{" +
            "idGeoBoundary=" + this.idGeoBoundary +
            ", geoCode='" + getGeoCode() + "'" +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
