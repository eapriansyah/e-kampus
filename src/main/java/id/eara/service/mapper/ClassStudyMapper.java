package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ClassStudyDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ClassStudy and its DTO ClassStudyDTO.
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class, ProgramStudyMapper.class, AcademicPeriodsMapper.class, LectureMapper.class, })
public interface ClassStudyMapper extends EntityMapper <ClassStudyDTO, ClassStudy> {

    @Mapping(source = "course.idCourse", target = "courseId")
    @Mapping(source = "course.description", target = "courseDescription")

    @Mapping(source = "prody.idPartyRole", target = "prodyId")
    @Mapping(source = "prody.name", target = "prodyName")

    @Mapping(source = "period.idPeriod", target = "periodId")
    @Mapping(source = "period.description", target = "periodDescription")

    @Mapping(source = "lecture.idPartyRole", target = "lectureId")
    @Mapping(source = "lecture.name", target = "lectureName")

    @Mapping(source = "secondaryLecture.idPartyRole", target = "secondaryLectureId")
    @Mapping(source = "secondaryLecture.name", target = "secondaryLectureName")

    @Mapping(source = "thirdLecture.idPartyRole", target = "thirdLectureId")
    @Mapping(source = "thirdLecture.name", target = "thirdLectureName")
    ClassStudyDTO toDto(ClassStudy classStudy); 

    @Mapping(source = "courseId", target = "course")

    @Mapping(source = "prodyId", target = "prody")

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "lectureId", target = "lecture")

    @Mapping(source = "secondaryLectureId", target = "secondaryLecture")

    @Mapping(source = "thirdLectureId", target = "thirdLecture")
    ClassStudy toEntity(ClassStudyDTO classStudyDTO); 

    default ClassStudy fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ClassStudy classStudy = new ClassStudy();
        classStudy.setIdClassStudy(id);
        return classStudy;
    }
}
