package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the EducationType entity.
 * atiila consulting
 */

public class EducationTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idEducationType;

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

        EducationTypeDTO educationTypeDTO = (EducationTypeDTO) o;
        if(educationTypeDTO.getIdEducationType() == null || getIdEducationType() == null) {
            return false;
        }
        return Objects.equals(getIdEducationType(), educationTypeDTO.getIdEducationType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdEducationType());
    }

    @Override
    public String toString() {
        return "EducationTypeDTO{" +
            "id=" + getIdEducationType() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
