package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity PostalAddress.
 */

@Entity
@Table(name = "postal_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "postaladdress")
public class PostalAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idcontact", columnDefinition = "BINARY(16)")
    private UUID idContact;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
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

    public PostalAddress address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public PostalAddress address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public PostalAddress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public PostalAddress province(String province) {
        this.province = province;
        return this;
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
        PostalAddress postalAddress = (PostalAddress) o;
        if (postalAddress.idContact == null || this.idContact == null) {
            return false;
        }
        return Objects.equals(this.idContact, postalAddress.idContact);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idContact);
    }

    @Override
    public String toString() {
        return "PostalAddress{" +
            "idContact=" + this.idContact +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            '}';
    }
}
