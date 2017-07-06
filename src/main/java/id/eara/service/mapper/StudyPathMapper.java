package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudyPathDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity StudyPath and its DTO StudyPathDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudyPathMapper extends EntityMapper <StudyPathDTO, StudyPath> {
    
    

    default StudyPath fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudyPath studyPath = new StudyPath();
        studyPath.setIdStudyPath(id);
        return studyPath;
    }
}
