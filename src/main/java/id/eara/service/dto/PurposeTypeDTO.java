package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the PurposeType entity.
 * atiila consulting
 */

public class PurposeTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idPurposeType;

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

        PurposeTypeDTO purposeTypeDTO = (PurposeTypeDTO) o;
        if(purposeTypeDTO.getIdPurposeType() == null || getIdPurposeType() == null) {
            return false;
        }
        return Objects.equals(getIdPurposeType(), purposeTypeDTO.getIdPurposeType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPurposeType());
    }

    @Override
    public String toString() {
        return "PurposeTypeDTO{" +
            "id=" + getIdPurposeType() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
