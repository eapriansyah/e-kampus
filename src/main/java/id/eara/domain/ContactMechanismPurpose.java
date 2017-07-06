package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity ContactMechanismPurpose.
 */

@Entity
@Table(name = "contact_mechanism_purpose")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "contactmechanismpurpose")
public class ContactMechanismPurpose implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idconmecpur", columnDefinition = "BINARY(16)")
    private UUID idContactMechPurpose;

    @Column(name = "date_from")
    private ZonedDateTime dateFrom;

    @Column(name = "date_thru")
    private ZonedDateTime dateThru;

    @ManyToOne
    @JoinColumn(name="idpurposetype", referencedColumnName="idpurposetype")
    private PurposeType purpose;

    @ManyToOne
    @JoinColumn(name="idparty", referencedColumnName="idparty")
    private Party party;

    @ManyToOne
    @JoinColumn(name="idcontact", referencedColumnName="idcontact")
    private ContactMechanism contact;

    public UUID getIdContactMechPurpose() {
        return this.idContactMechPurpose;
    }

    public void setIdContactMechPurpose(UUID id) {
        this.idContactMechPurpose = id;
    }


    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public ContactMechanismPurpose dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public ContactMechanismPurpose dateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
        return this;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public PurposeType getPurpose() {
        return purpose;
    }

    public ContactMechanismPurpose purpose(PurposeType purposeType) {
        this.purpose = purposeType;
        return this;
    }

    public void setPurpose(PurposeType purposeType) {
        this.purpose = purposeType;
    }

    public Party getParty() {
        return party;
    }

    public ContactMechanismPurpose party(Party party) {
        this.party = party;
        return this;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public ContactMechanism getContact() {
        return contact;
    }

    public ContactMechanismPurpose contact(ContactMechanism contactMechanism) {
        this.contact = contactMechanism;
        return this;
    }

    public void setContact(ContactMechanism contactMechanism) {
        this.contact = contactMechanism;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactMechanismPurpose contactMechanismPurpose = (ContactMechanismPurpose) o;
        if (contactMechanismPurpose.idContactMechPurpose == null || this.idContactMechPurpose == null) {
            return false;
        }
        return Objects.equals(this.idContactMechPurpose, contactMechanismPurpose.idContactMechPurpose);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idContactMechPurpose);
    }

    @Override
    public String toString() {
        return "ContactMechanismPurpose{" +
            "idContactMechPurpose=" + this.idContactMechPurpose +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            '}';
    }
}
