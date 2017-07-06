package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.LocationDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper extends EntityMapper <LocationDTO, Location> {
    
    

    default Location fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setIdGeoBoundary(id);
        return location;
    }
}
