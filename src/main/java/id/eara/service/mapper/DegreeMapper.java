package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.DegreeDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity Degree and its DTO DegreeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DegreeMapper extends EntityMapper <DegreeDTO, Degree> {
    
    

    default Degree fromId(Long id) {
        if (id == null) {
            return null;
        }
        Degree degree = new Degree();
        degree.setIdDegree(id);
        return degree;
    }
}
