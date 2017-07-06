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
 * Class definition for Entity Lab.
 */

@Entity
@Table(name = "labs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "lab")
public class Lab implements Serializable {

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
    @JoinColumn(name="idpartof", referencedColumnName="idfacility")
    private Building building;

    public UUID getIdFacility() {
        return this.idFacility;
    }

    public void setIdFacility(UUID id) {
        this.idFacility = id;
    }


    public String getFacilityCode() {
        return facilityCode;
    }

    public Lab facilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
        return this;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getDescription() {
        return description;
    }

    public Lab description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Building getBuilding() {
        return building;
    }

    public Lab building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lab lab = (Lab) o;
        if (lab.idFacility == null || this.idFacility == null) {
            return false;
        }
        return Objects.equals(this.idFacility, lab.idFacility);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idFacility);
    }

    @Override
    public String toString() {
        return "Lab{" +
            "idFacility=" + this.idFacility +
            ", facilityCode='" + getFacilityCode() + "'" +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
