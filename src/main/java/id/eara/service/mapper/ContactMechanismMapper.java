package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ContactMechanismDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ContactMechanism and its DTO ContactMechanismDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactMechanismMapper extends EntityMapper <ContactMechanismDTO, ContactMechanism> {
    
    

    default ContactMechanism fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ContactMechanism contactMechanism = new ContactMechanism();
        contactMechanism.setIdContact(id);
        return contactMechanism;
    }
}
