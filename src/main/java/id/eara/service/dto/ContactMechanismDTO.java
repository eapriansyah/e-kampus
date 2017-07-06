package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ContactMechanism entity.
 * atiila consulting
 */

public class ContactMechanismDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idContact;

    private String description;

    public UUID getIdContact() {
        return this.idContact;
    }

    public void setIdContact(UUID id) {
        this.idContact = id;
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

        ContactMechanismDTO contactMechanismDTO = (ContactMechanismDTO) o;
        if(contactMechanismDTO.getIdContact() == null || getIdContact() == null) {
            return false;
        }
        return Objects.equals(getIdContact(), contactMechanismDTO.getIdContact());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContact());
    }

    @Override
    public String toString() {
        return "ContactMechanismDTO{" +
            "id=" + getIdContact() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
