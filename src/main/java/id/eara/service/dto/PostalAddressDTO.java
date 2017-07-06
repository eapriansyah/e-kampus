package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the PostalAddress entity.
 * atiila consulting
 */

public class PostalAddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idContact;

    private String address1;

    private String address2;

    private String city;

    private String province;

    public UUID getIdContact() {
        return this.idContact;
    }

    public void setIdContact(UUID id) {
        this.idContact = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostalAddressDTO postalAddressDTO = (PostalAddressDTO) o;
        if(postalAddressDTO.getIdContact() == null || getIdContact() == null) {
            return false;
        }
        return Objects.equals(getIdContact(), postalAddressDTO.getIdContact());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContact());
    }

    @Override
    public String toString() {
        return "PostalAddressDTO{" +
            "id=" + getIdContact() +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            "}";
    }
}
