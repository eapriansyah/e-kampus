package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the StudyPath entity.
 * atiila consulting
 */

public class StudyPathDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idStudyPath;

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

        StudyPathDTO studyPathDTO = (StudyPathDTO) o;
        if(studyPathDTO.getIdStudyPath() == null || getIdStudyPath() == null) {
            return false;
        }
        return Objects.equals(getIdStudyPath(), studyPathDTO.getIdStudyPath());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudyPath());
    }

    @Override
    public String toString() {
        return "StudyPathDTO{" +
            "id=" + getIdStudyPath() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
