package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ProgramStudyDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ProgramStudy and its DTO ProgramStudyDTO.
 */
@Mapper(componentModel = "spring", uses = {DegreeMapper.class, FacultyMapper.class, })
public interface ProgramStudyMapper extends EntityMapper <ProgramStudyDTO, ProgramStudy> {

    @Mapping(source = "degree.idDegree", target = "degreeId")
    @Mapping(source = "degree.description", target = "degreeDescription")

    @Mapping(source = "faculty.idPartyRole", target = "facultyId")
    @Mapping(source = "faculty.name", target = "facultyName")
    ProgramStudyDTO toDto(ProgramStudy programStudy); 

    @Mapping(source = "degreeId", target = "degree")

    @Mapping(source = "facultyId", target = "faculty")
    ProgramStudy toEntity(ProgramStudyDTO programStudyDTO); 

    default ProgramStudy fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ProgramStudy programStudy = new ProgramStudy();
        programStudy.setIdPartyRole(id);
        return programStudy;
    }
}
