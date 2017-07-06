package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the ReligionType entity.
 * atiila consulting
 */

public class ReligionTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idReligionType;

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

        ReligionTypeDTO religionTypeDTO = (ReligionTypeDTO) o;
        if(religionTypeDTO.getIdReligionType() == null || getIdReligionType() == null) {
            return false;
        }
        return Objects.equals(getIdReligionType(), religionTypeDTO.getIdReligionType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdReligionType());
    }

    @Override
    public String toString() {
        return "ReligionTypeDTO{" +
            "id=" + getIdReligionType() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
