package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.OrganizationDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity Organization and its DTO OrganizationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper <OrganizationDTO, Organization> {
    
    

    default Organization fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setIdParty(id);
        return organization;
    }
}
