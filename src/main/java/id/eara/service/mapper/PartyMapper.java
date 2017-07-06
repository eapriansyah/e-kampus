package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PartyDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Party and its DTO PartyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartyMapper extends EntityMapper <PartyDTO, Party> {
    
    

    default Party fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Party party = new Party();
        party.setIdParty(id);
        return party;
    }
}
