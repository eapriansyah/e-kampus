package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity PurposeType.
 */

@Entity
@Table(name = "purpose_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "purposetype")
public class PurposeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpurposetype")
    private Long idPurposeType;

    @Column(name = "description")
    private String description;

    public Long getIdPurposeType() {
        return this.idPurposeType;
    }

    public void setIdPurposeType(Long id) {
        this.idPurposeType = id;
    }


    public String getDescription() {
        return description;
    }

    public PurposeType description(String description) {
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
        PurposeType purposeType = (PurposeType) o;
        if (purposeType.idPurposeType == null || this.idPurposeType == null) {
            return false;
        }
        return Objects.equals(this.idPurposeType, purposeType.idPurposeType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPurposeType);
    }

    @Override
    public String toString() {
        return "PurposeType{" +
            "idPurposeType=" + this.idPurposeType +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
