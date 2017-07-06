package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentCoursePeriodDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity StudentCoursePeriod and its DTO StudentCoursePeriodDTO.
 */
@Mapper(componentModel = "spring", uses = {AcademicPeriodsMapper.class, CourseMapper.class, StudentMapper.class, })
public interface StudentCoursePeriodMapper extends EntityMapper <StudentCoursePeriodDTO, StudentCoursePeriod> {

    @Mapping(source = "period.idPeriod", target = "periodId")
    @Mapping(source = "period.description", target = "periodDescription")

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")

    @Mapping(source = "student.idPartyRole", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    StudentCoursePeriodDTO toDto(StudentCoursePeriod studentCoursePeriod); 

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "courseId", target = "course")

    @Mapping(source = "studentId", target = "student")
    StudentCoursePeriod toEntity(StudentCoursePeriodDTO studentCoursePeriodDTO); 

    default StudentCoursePeriod fromId(UUID id) {
        if (id == null) {
            return null;
        }
        StudentCoursePeriod studentCoursePeriod = new StudentCoursePeriod();
        studentCoursePeriod.setIdStudentCoursePeriod(id);
        return studentCoursePeriod;
    }
}
