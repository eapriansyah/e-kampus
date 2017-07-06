package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the TelecomunicationNumber entity.
 * atiila consulting
 */

public class TelecomunicationNumberDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idContact;

    private String number;

    public UUID getIdContact() {
        return this.idContact;
    }

    public void setIdContact(UUID id) {
        this.idContact = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelecomunicationNumberDTO telecomunicationNumberDTO = (TelecomunicationNumberDTO) o;
        if(telecomunicationNumberDTO.getIdContact() == null || getIdContact() == null) {
            return false;
        }
        return Objects.equals(getIdContact(), telecomunicationNumberDTO.getIdContact());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContact());
    }

    @Override
    public String toString() {
        return "TelecomunicationNumberDTO{" +
            "id=" + getIdContact() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
