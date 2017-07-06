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
 * Class definition for Entity Building.
 */

@Entity
@Table(name = "building")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "building")
public class Building implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idfacility", columnDefinition = "BINARY(16)")
    private UUID idFacility;

    @Column(name = "facility_code")
    private String facilityCode;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="idzone", referencedColumnName="idgeobou")
    private Zone zone;

    public UUID getIdFacility() {
        return this.idFacility;
    }

    public void setIdFacility(UUID id) {
        this.idFacility = id;
    }


    public String getFacilityCode() {
        return facilityCode;
    }

    public Building facilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
        return this;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getDescription() {
        return description;
    }

    public Building description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Zone getZone() {
        return zone;
    }

    public Building zone(Zone zone) {
        this.zone = zone;
        return this;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        if (building.idFacility == null || this.idFacility == null) {
            return false;
        }
        return Objects.equals(this.idFacility, building.idFacility);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idFacility);
    }

    @Override
    public String toString() {
        return "Building{" +
            "idFacility=" + this.idFacility +
            ", facilityCode='" + getFacilityCode() + "'" +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
