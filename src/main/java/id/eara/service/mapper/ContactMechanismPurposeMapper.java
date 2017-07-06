package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ContactMechanismPurposeDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ContactMechanismPurpose and its DTO ContactMechanismPurposeDTO.
 */
@Mapper(componentModel = "spring", uses = {PurposeTypeMapper.class, PartyMapper.class, ContactMechanismMapper.class, })
public interface ContactMechanismPurposeMapper extends EntityMapper <ContactMechanismPurposeDTO, ContactMechanismPurpose> {

    @Mapping(source = "purpose.idPurposeType", target = "purposeId")
    @Mapping(source = "purpose.description", target = "purposeDescription")

    @Mapping(source = "party.idParty", target = "partyId")
    @Mapping(source = "party.name", target = "partyName")

    @Mapping(source = "contact.idContact", target = "contactId")
    @Mapping(source = "contact.description", target = "contactDescription")
    ContactMechanismPurposeDTO toDto(ContactMechanismPurpose contactMechanismPurpose); 

    @Mapping(source = "purposeId", target = "purpose")

    @Mapping(source = "partyId", target = "party")

    @Mapping(source = "contactId", target = "contact")
    ContactMechanismPurpose toEntity(ContactMechanismPurposeDTO contactMechanismPurposeDTO); 

    default ContactMechanismPurpose fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ContactMechanismPurpose contactMechanismPurpose = new ContactMechanismPurpose();
        contactMechanismPurpose.setIdContactMechPurpose(id);
        return contactMechanismPurpose;
    }
}
