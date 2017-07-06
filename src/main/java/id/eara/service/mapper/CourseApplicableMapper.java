package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.CourseApplicableDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity CourseApplicable and its DTO CourseApplicableDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgramStudyMapper.class, CourseMapper.class, PeriodTypeMapper.class, })
public interface CourseApplicableMapper extends EntityMapper <CourseApplicableDTO, CourseApplicable> {

    @Mapping(source = "prody.idPartyRole", target = "prodyId")
    @Mapping(source = "prody.name", target = "prodyName")

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")

    @Mapping(source = "periodType.idPeriodType", target = "periodTypeId")
    @Mapping(source = "periodType.description", target = "periodTypeDescription")
    CourseApplicableDTO toDto(CourseApplicable courseApplicable); 

    @Mapping(source = "prodyId", target = "prody")

    @Mapping(source = "courseId", target = "course")

    @Mapping(source = "periodTypeId", target = "periodType")
    CourseApplicable toEntity(CourseApplicableDTO courseApplicableDTO); 

    default CourseApplicable fromId(UUID id) {
        if (id == null) {
            return null;
        }
        CourseApplicable courseApplicable = new CourseApplicable();
        courseApplicable.setIdApplCourse(id);
        return courseApplicable;
    }
}
