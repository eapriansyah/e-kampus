package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the WorkType entity.
 * atiila consulting
 */

public class WorkTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idWorkType;

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

        WorkTypeDTO workTypeDTO = (WorkTypeDTO) o;
        if(workTypeDTO.getIdWorkType() == null || getIdWorkType() == null) {
            return false;
        }
        return Objects.equals(getIdWorkType(), workTypeDTO.getIdWorkType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdWorkType());
    }

    @Override
    public String toString() {
        return "WorkTypeDTO{" +
            "id=" + getIdWorkType() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
