package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity EducationType.
 */

@Entity
@Table(name = "education_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "educationtype")
public class EducationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ideducationtype")
    private Long idEducationType;

    @Column(name = "description")
    private String description;

    public Long getIdEducationType() {
        return this.idEducationType;
    }

    public void setIdEducationType(Long id) {
        this.idEducationType = id;
    }


    public String getDescription() {
        return description;
    }

    public EducationType description(String description) {
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
        EducationType educationType = (EducationType) o;
        if (educationType.idEducationType == null || this.idEducationType == null) {
            return false;
        }
        return Objects.equals(this.idEducationType, educationType.idEducationType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idEducationType);
    }

    @Override
    public String toString() {
        return "EducationType{" +
            "idEducationType=" + this.idEducationType +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
