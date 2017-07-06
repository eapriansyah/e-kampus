package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentPeriodDataDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity StudentPeriodData and its DTO StudentPeriodDataDTO.
 */
@Mapper(componentModel = "spring", uses = {AcademicPeriodsMapper.class, CourseMapper.class, StudentMapper.class, })
public interface StudentPeriodDataMapper extends EntityMapper <StudentPeriodDataDTO, StudentPeriodData> {

    @Mapping(source = "period.idPeriod", target = "periodId")
    @Mapping(source = "period.description", target = "periodDescription")

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")

    @Mapping(source = "student.idPartyRole", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    StudentPeriodDataDTO toDto(StudentPeriodData studentPeriodData); 

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "courseId", target = "course")

    @Mapping(source = "studentId", target = "student")
    StudentPeriodData toEntity(StudentPeriodDataDTO studentPeriodDataDTO); 

    default StudentPeriodData fromId(UUID id) {
        if (id == null) {
            return null;
        }
        StudentPeriodData studentPeriodData = new StudentPeriodData();
        studentPeriodData.setIdStudentPeriod(id);
        return studentPeriodData;
    }
}
