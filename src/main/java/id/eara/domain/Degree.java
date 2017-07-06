package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity Degree.
 */

@Entity
@Table(name = "degree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "degree")
public class Degree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddegree")
    private Long idDegree;

    @Column(name = "description")
    private String description;

    @Column(name = "max_study")
    private Integer maxStudy;

    public Long getIdDegree() {
        return this.idDegree;
    }

    public void setIdDegree(Long id) {
        this.idDegree = id;
    }


    public String getDescription() {
        return description;
    }

    public Degree description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxStudy() {
        return maxStudy;
    }

    public Degree maxStudy(Integer maxStudy) {
        this.maxStudy = maxStudy;
        return this;
    }

    public void setMaxStudy(Integer maxStudy) {
        this.maxStudy = maxStudy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Degree degree = (Degree) o;
        if (degree.idDegree == null || this.idDegree == null) {
            return false;
        }
        return Objects.equals(this.idDegree, degree.idDegree);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idDegree);
    }

    @Override
    public String toString() {
        return "Degree{" +
            "idDegree=" + this.idDegree +
            ", description='" + getDescription() + "'" +
            ", maxStudy='" + getMaxStudy() + "'" +
            '}';
    }
}
