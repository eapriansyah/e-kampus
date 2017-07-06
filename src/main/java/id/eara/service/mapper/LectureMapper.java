package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.LectureDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Lecture and its DTO LectureDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LectureMapper extends EntityMapper <LectureDTO, Lecture> {
    
    

    default Lecture fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Lecture lecture = new Lecture();
        lecture.setIdPartyRole(id);
        return lecture;
    }
}
