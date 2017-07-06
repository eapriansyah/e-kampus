package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.UniversityDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity University and its DTO UniversityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniversityMapper extends EntityMapper <UniversityDTO, University> {
    
    

    default University fromId(UUID id) {
        if (id == null) {
            return null;
        }
        University university = new University();
        university.setIdPartyRole(id);
        return university;
    }
}
