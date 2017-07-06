package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.TelecomunicationNumberDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity TelecomunicationNumber and its DTO TelecomunicationNumberDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TelecomunicationNumberMapper extends EntityMapper <TelecomunicationNumberDTO, TelecomunicationNumber> {
    
    

    default TelecomunicationNumber fromId(UUID id) {
        if (id == null) {
            return null;
        }
        TelecomunicationNumber telecomunicationNumber = new TelecomunicationNumber();
        telecomunicationNumber.setIdContact(id);
        return telecomunicationNumber;
    }
}
