package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.FacultyDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Faculty and its DTO FacultyDTO.
 */
@Mapper(componentModel = "spring", uses = {UniversityMapper.class, })
public interface FacultyMapper extends EntityMapper <FacultyDTO, Faculty> {

    @Mapping(source = "university.idPartyRole", target = "universityId")
    @Mapping(source = "university.name", target = "universityName")
    FacultyDTO toDto(Faculty faculty); 

    @Mapping(source = "universityId", target = "university")
    Faculty toEntity(FacultyDTO facultyDTO); 

    default Faculty fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Faculty faculty = new Faculty();
        faculty.setIdPartyRole(id);
        return faculty;
    }
}
