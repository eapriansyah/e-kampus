package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity ReligionType.
 */

@Entity
@Table(name = "religion_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "religiontype")
public class ReligionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreligiontype")
    private Long idReligionType;

    @Column(name = "description")
    private String description;

    public Long getIdReligionType() {
        return this.idReligionType;
    }

    public void setIdReligionType(Long id) {
        this.idReligionType = id;
    }


    public String getDescription() {
        return description;
    }

    public ReligionType description(String description) {
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
        ReligionType religionType = (ReligionType) o;
        if (religionType.idReligionType == null || this.idReligionType == null) {
            return false;
        }
        return Objects.equals(this.idReligionType, religionType.idReligionType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idReligionType);
    }

    @Override
    public String toString() {
        return "ReligionType{" +
            "idReligionType=" + this.idReligionType +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
