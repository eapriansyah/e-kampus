package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity StudyPath.
 */

@Entity
@Table(name = "study_path")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studypath")
public class StudyPath implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstupat")
    private Long idStudyPath;

    @Column(name = "description")
    private String description;

    public Long getIdStudyPath() {
        return this.idStudyPath;
    }

    public void setIdStudyPath(Long id) {
        this.idStudyPath = id;
    }


    public String getDescription() {
        return description;
    }

    public StudyPath description(String description) {
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
        StudyPath studyPath = (StudyPath) o;
        if (studyPath.idStudyPath == null || this.idStudyPath == null) {
            return false;
        }
        return Objects.equals(this.idStudyPath, studyPath.idStudyPath);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudyPath);
    }

    @Override
    public String toString() {
        return "StudyPath{" +
            "idStudyPath=" + this.idStudyPath +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
