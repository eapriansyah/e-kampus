package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ElectronicAddressDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ElectronicAddress and its DTO ElectronicAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ElectronicAddressMapper extends EntityMapper <ElectronicAddressDTO, ElectronicAddress> {
    
    

    default ElectronicAddress fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ElectronicAddress electronicAddress = new ElectronicAddress();
        electronicAddress.setIdContact(id);
        return electronicAddress;
    }
}
