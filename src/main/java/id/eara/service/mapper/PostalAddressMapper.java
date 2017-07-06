package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PostalAddressDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity PostalAddress and its DTO PostalAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostalAddressMapper extends EntityMapper <PostalAddressDTO, PostalAddress> {
    
    

    default PostalAddress fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setIdContact(id);
        return postalAddress;
    }
}
