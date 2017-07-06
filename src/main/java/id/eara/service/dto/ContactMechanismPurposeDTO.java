package id.eara.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ContactMechanismPurpose entity.
 * atiila consulting
 */

public class ContactMechanismPurposeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idContactMechPurpose;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateThru;

    private Long purposeId;

    private String purposeDescription;

    private UUID partyId;

    private String partyName;

    private UUID contactId;

    private String contactDescription;

    public UUID getIdContactMechPurpose() {
        return this.idContactMechPurpose;
    }

    public void setIdContactMechPurpose(UUID id) {
        this.idContactMechPurpose = id;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public Long getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(Long purposeTypeId) {
        this.purposeId = purposeTypeId;
    }

    public String getPurposeDescription() {
        return purposeDescription;
    }

    public void setPurposeDescription(String purposeTypeDescription) {
        this.purposeDescription = purposeTypeDescription;
    }

    public UUID getPartyId() {
        return partyId;
    }

    public void setPartyId(UUID partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public UUID getContactId() {
        return contactId;
    }

    public void setContactId(UUID contactMechanismId) {
        this.contactId = contactMechanismId;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public void setContactDescription(String contactMechanismDescription) {
        this.contactDescription = contactMechanismDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactMechanismPurposeDTO contactMechanismPurposeDTO = (ContactMechanismPurposeDTO) o;
        if(contactMechanismPurposeDTO.getIdContactMechPurpose() == null || getIdContactMechPurpose() == null) {
            return false;
        }
        return Objects.equals(getIdContactMechPurpose(), contactMechanismPurposeDTO.getIdContactMechPurpose());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdContactMechPurpose());
    }

    @Override
    public String toString() {
        return "ContactMechanismPurposeDTO{" +
            "id=" + getIdContactMechPurpose() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            "}";
    }
}
