package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgramStudyMapper.class, StudyPathMapper.class, })
public interface StudentMapper extends EntityMapper <StudentDTO, Student> {

    @Mapping(source = "prody.idPartyRole", target = "prodyId")
    @Mapping(source = "prody.name", target = "prodyName")

    @Mapping(source = "studyPath.idStudyPath", target = "studyPathId")
    @Mapping(source = "studyPath.description", target = "studyPathDescription")
    StudentDTO toDto(Student student); 

    @Mapping(source = "prodyId", target = "prody")

    @Mapping(source = "studyPathId", target = "studyPath")
    Student toEntity(StudentDTO studentDTO); 

    default Student fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setIdPartyRole(id);
        return student;
    }
}
