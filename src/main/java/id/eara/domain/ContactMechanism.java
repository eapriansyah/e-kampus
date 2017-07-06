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
 * Class definition for Entity ContactMechanism.
 */

@Entity
@Table(name = "contact_mechanism")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "contactmechanism")
public class ContactMechanism implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idcontact", columnDefinition = "BINARY(16)")
    private UUID idContact;

    @Column(name = "description")
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

    public ContactMechanism description(String description) {
        this.description = description;
        return this;
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
        ContactMechanism contactMechanism = (ContactMechanism) o;
        if (contactMechanism.idContact == null || this.idContact == null) {
            return false;
        }
        return Objects.equals(this.idContact, contactMechanism.idContact);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idContact);
    }

    @Override
    public String toString() {
        return "ContactMechanism{" +
            "idContact=" + this.idContact +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
