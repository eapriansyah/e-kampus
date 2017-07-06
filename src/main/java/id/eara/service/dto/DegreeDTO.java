package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the Degree entity.
 * atiila consulting
 */

public class DegreeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idDegree;

    private String description;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxStudy() {
        return maxStudy;
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

        DegreeDTO degreeDTO = (DegreeDTO) o;
        if(degreeDTO.getIdDegree() == null || getIdDegree() == null) {
            return false;
        }
        return Objects.equals(getIdDegree(), degreeDTO.getIdDegree());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdDegree());
    }

    @Override
    public String toString() {
        return "DegreeDTO{" +
            "id=" + getIdDegree() +
            ", description='" + getDescription() + "'" +
            ", maxStudy='" + getMaxStudy() + "'" +
            "}";
    }
}
