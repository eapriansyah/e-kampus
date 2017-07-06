package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PreStudentDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity PreStudent and its DTO PreStudentDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgramStudyMapper.class, StudyPathMapper.class, })
public interface PreStudentMapper extends EntityMapper <PreStudentDTO, PreStudent> {

    @Mapping(source = "prody.idPartyRole", target = "prodyId")
    @Mapping(source = "prody.name", target = "prodyName")

    @Mapping(source = "studyPath.idStudyPath", target = "studyPathId")
    @Mapping(source = "studyPath.description", target = "studyPathDescription")
    PreStudentDTO toDto(PreStudent preStudent); 

    @Mapping(source = "prodyId", target = "prody")

    @Mapping(source = "studyPathId", target = "studyPath")
    PreStudent toEntity(PreStudentDTO preStudentDTO); 

    default PreStudent fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PreStudent preStudent = new PreStudent();
        preStudent.setIdPartyRole(id);
        return preStudent;
    }
}
