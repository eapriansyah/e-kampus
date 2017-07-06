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
 * Class definition for Entity TelecomunicationNumber.
 */

@Entity
@Table(name = "telecomunication_number")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "telecomunicationnumber")
public class TelecomunicationNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idcontact", columnDefinition = "BINARY(16)")
    private UUID idContact;

    @Column(name = "jhi_number")
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

    public TelecomunicationNumber number(String number) {
        this.number = number;
        return this;
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
        TelecomunicationNumber telecomunicationNumber = (TelecomunicationNumber) o;
        if (telecomunicationNumber.idContact == null || this.idContact == null) {
            return false;
        }
        return Objects.equals(this.idContact, telecomunicationNumber.idContact);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idContact);
    }

    @Override
    public String toString() {
        return "TelecomunicationNumber{" +
            "idContact=" + this.idContact +
            ", number='" + getNumber() + "'" +
            '}';
    }
}
