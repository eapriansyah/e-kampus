package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PurposeTypeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity PurposeType and its DTO PurposeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PurposeTypeMapper extends EntityMapper <PurposeTypeDTO, PurposeType> {
    
    

    default PurposeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurposeType purposeType = new PurposeType();
        purposeType.setIdPurposeType(id);
        return purposeType;
    }
}
