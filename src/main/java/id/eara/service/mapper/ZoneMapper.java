package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ZoneDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Zone and its DTO ZoneDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, })
public interface ZoneMapper extends EntityMapper <ZoneDTO, Zone> {

    @Mapping(source = "parent.idGeoBoundary", target = "parentId")
    @Mapping(source = "parent.description", target = "parentDescription")
    ZoneDTO toDto(Zone zone); 

    @Mapping(source = "parentId", target = "parent")
    Zone toEntity(ZoneDTO zoneDTO); 

    default Zone fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Zone zone = new Zone();
        zone.setIdGeoBoundary(id);
        return zone;
    }
}
