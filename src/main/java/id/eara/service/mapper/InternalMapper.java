package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.InternalDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Internal and its DTO InternalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InternalMapper extends EntityMapper <InternalDTO, Internal> {
    
    

    default Internal fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Internal internal = new Internal();
        internal.setIdPartyRole(id);
        return internal;
    }
}
