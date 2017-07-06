package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.EducationTypeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity EducationType and its DTO EducationTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EducationTypeMapper extends EntityMapper <EducationTypeDTO, EducationType> {
    
    

    default EducationType fromId(Long id) {
        if (id == null) {
            return null;
        }
        EducationType educationType = new EducationType();
        educationType.setIdEducationType(id);
        return educationType;
    }
}
