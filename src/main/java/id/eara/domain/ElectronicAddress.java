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
 * Class definition for Entity ElectronicAddress.
 */

@Entity
@Table(name = "electronic_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "electronicaddress")
public class ElectronicAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idcontact", columnDefinition = "BINARY(16)")
    private UUID idContact;

    @Column(name = "address")
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

    public ElectronicAddress address(String address) {
        this.address = address;
        return this;
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
        ElectronicAddress electronicAddress = (ElectronicAddress) o;
        if (electronicAddress.idContact == null || this.idContact == null) {
            return false;
        }
        return Objects.equals(this.idContact, electronicAddress.idContact);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idContact);
    }

    @Override
    public String toString() {
        return "ElectronicAddress{" +
            "idContact=" + this.idContact +
            ", address='" + getAddress() + "'" +
            '}';
    }
}
