package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PersonDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonMapper extends EntityMapper <PersonDTO, Person> {
    
    

    default Person fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setIdParty(id);
        return person;
    }
}
