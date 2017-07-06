package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.CourseLectureDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity CourseLecture and its DTO CourseLectureDTO.
 */
@Mapper(componentModel = "spring", uses = {LectureMapper.class, CourseMapper.class, })
public interface CourseLectureMapper extends EntityMapper <CourseLectureDTO, CourseLecture> {

    @Mapping(source = "lecture.idPartyRole", target = "lectureId")
    @Mapping(source = "lecture.name", target = "lectureName")

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")
    CourseLectureDTO toDto(CourseLecture courseLecture); 

    @Mapping(source = "lectureId", target = "lecture")

    @Mapping(source = "courseId", target = "course")
    CourseLecture toEntity(CourseLectureDTO courseLectureDTO); 

    default CourseLecture fromId(UUID id) {
        if (id == null) {
            return null;
        }
        CourseLecture courseLecture = new CourseLecture();
        courseLecture.setIdCourseLecture(id);
        return courseLecture;
    }
}
