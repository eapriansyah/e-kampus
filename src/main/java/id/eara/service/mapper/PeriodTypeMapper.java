package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PeriodTypeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity PeriodType and its DTO PeriodTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodTypeMapper extends EntityMapper <PeriodTypeDTO, PeriodType> {
    
    

    default PeriodType fromId(Long id) {
        if (id == null) {
            return null;
        }
        PeriodType periodType = new PeriodType();
        periodType.setIdPeriodType(id);
        return periodType;
    }
}
