package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentCourseScoreDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity StudentCourseScore and its DTO StudentCourseScoreDTO.
 */
@Mapper(componentModel = "spring", uses = {AcademicPeriodsMapper.class, CourseMapper.class, StudentMapper.class, })
public interface StudentCourseScoreMapper extends EntityMapper <StudentCourseScoreDTO, StudentCourseScore> {

    @Mapping(source = "period.idPeriod", target = "periodId")
    @Mapping(source = "period.description", target = "periodDescription")

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")

    @Mapping(source = "student.idPartyRole", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    StudentCourseScoreDTO toDto(StudentCourseScore studentCourseScore); 

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "courseId", target = "course")

    @Mapping(source = "studentId", target = "student")
    StudentCourseScore toEntity(StudentCourseScoreDTO studentCourseScoreDTO); 

    default StudentCourseScore fromId(UUID id) {
        if (id == null) {
            return null;
        }
        StudentCourseScore studentCourseScore = new StudentCourseScore();
        studentCourseScore.setIdStudentCourseScore(id);
        return studentCourseScore;
    }
}
