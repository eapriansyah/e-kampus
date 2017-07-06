package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ElectronicAddress entity.
 * atiila consulting
 */

public class ElectronicAddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idContact;

    private String address;

    public UUID getIdContact() {
        return this.idContact;
    }

    public void setIdContact(UUID id) {
        this.idContact = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElectronicAddressDTO electronicAddressDTO = (ElectronicAddressDTO) o;
        if(electronicAddressDTO.getIdContact() == null || getIdContact() == null) {
            return false;
        }
        return Objects.equals(getIdContact(), electronicAddressDTO.getIdContact());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContact());
    }

    @Override
    public String toString() {
        return "ElectronicAddressDTO{" +
            "id=" + getIdContact() +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
