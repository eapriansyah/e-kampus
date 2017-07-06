package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.CourseDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Course and its DTO CourseDTO.
 */
@Mapper(componentModel = "spring", uses = {InternalMapper.class, })
public interface CourseMapper extends EntityMapper <CourseDTO, Course> {

    @Mapping(source = "owner.idPartyRole", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    CourseDTO toDto(Course course); 

    @Mapping(source = "ownerId", target = "owner")
    Course toEntity(CourseDTO courseDTO); 

    default Course fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setIdCourse(id);
        return course;
    }
}
