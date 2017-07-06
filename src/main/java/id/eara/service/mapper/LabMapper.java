package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.LabDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Lab and its DTO LabDTO.
 */
@Mapper(componentModel = "spring", uses = {BuildingMapper.class, })
public interface LabMapper extends EntityMapper <LabDTO, Lab> {

    @Mapping(source = "building.idFacility", target = "buildingId")
    @Mapping(source = "building.description", target = "buildingDescription")
    LabDTO toDto(Lab lab); 

    @Mapping(source = "buildingId", target = "building")
    Lab toEntity(LabDTO labDTO); 

    default Lab fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Lab lab = new Lab();
        lab.setIdFacility(id);
        return lab;
    }
}
