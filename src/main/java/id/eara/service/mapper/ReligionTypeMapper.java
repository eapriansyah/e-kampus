package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ReligionTypeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity ReligionType and its DTO ReligionTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReligionTypeMapper extends EntityMapper <ReligionTypeDTO, ReligionType> {
    
    

    default ReligionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReligionType religionType = new ReligionType();
        religionType.setIdReligionType(id);
        return religionType;
    }
}
