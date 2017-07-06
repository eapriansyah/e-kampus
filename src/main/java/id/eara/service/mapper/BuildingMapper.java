package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.BuildingDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Building and its DTO BuildingDTO.
 */
@Mapper(componentModel = "spring", uses = {ZoneMapper.class, })
public interface BuildingMapper extends EntityMapper <BuildingDTO, Building> {

    @Mapping(source = "zone.idGeoBoundary", target = "zoneId")
    @Mapping(source = "zone.description", target = "zoneDescription")
    BuildingDTO toDto(Building building); 

    @Mapping(source = "zoneId", target = "zone")
    Building toEntity(BuildingDTO buildingDTO); 

    default Building fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Building building = new Building();
        building.setIdFacility(id);
        return building;
    }
}
