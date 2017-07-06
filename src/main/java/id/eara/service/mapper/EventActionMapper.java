package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.EventActionDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity EventAction and its DTO EventActionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventActionMapper extends EntityMapper <EventActionDTO, EventAction> {
    
    

    default EventAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventAction eventAction = new EventAction();
        eventAction.setIdEventAction(id);
        return eventAction;
    }
}
