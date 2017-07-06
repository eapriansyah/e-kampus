package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.AcademicPeriodsDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity AcademicPeriods and its DTO AcademicPeriodsDTO.
 */
@Mapper(componentModel = "spring", uses = {PeriodTypeMapper.class, })
public interface AcademicPeriodsMapper extends EntityMapper <AcademicPeriodsDTO, AcademicPeriods> {

    @Mapping(source = "periodType.idPeriodType", target = "periodTypeId")
    @Mapping(source = "periodType.description", target = "periodTypeDescription")
    AcademicPeriodsDTO toDto(AcademicPeriods academicPeriods); 

    @Mapping(source = "periodTypeId", target = "periodType")
    AcademicPeriods toEntity(AcademicPeriodsDTO academicPeriodsDTO); 

    default AcademicPeriods fromId(UUID id) {
        if (id == null) {
            return null;
        }
        AcademicPeriods academicPeriods = new AcademicPeriods();
        academicPeriods.setIdPeriod(id);
        return academicPeriods;
    }
}
