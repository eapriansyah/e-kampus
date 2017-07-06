package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PostStudentDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity PostStudent and its DTO PostStudentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostStudentMapper extends EntityMapper <PostStudentDTO, PostStudent> {
    
    

    default PostStudent fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PostStudent postStudent = new PostStudent();
        postStudent.setIdPartyRole(id);
        return postStudent;
    }
}
