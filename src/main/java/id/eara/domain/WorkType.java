package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity WorkType.
 */

@Entity
@Table(name = "work_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "worktype")
public class WorkType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idworktype")
    private Long idWorkType;

    @Column(name = "description")
    private String description;

    public Long getIdWorkType() {
        return this.idWorkType;
    }

    public void setIdWorkType(Long id) {
        this.idWorkType = id;
    }


    public String getDescription() {
        return description;
    }

    public WorkType description(String description) {
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
        WorkType workType = (WorkType) o;
        if (workType.idWorkType == null || this.idWorkType == null) {
            return false;
        }
        return Objects.equals(this.idWorkType, workType.idWorkType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idWorkType);
    }

    @Override
    public String toString() {
        return "WorkType{" +
            "idWorkType=" + this.idWorkType +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
