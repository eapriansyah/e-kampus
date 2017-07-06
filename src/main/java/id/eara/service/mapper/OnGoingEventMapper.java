package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.OnGoingEventDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity OnGoingEvent and its DTO OnGoingEventDTO.
 */
@Mapper(componentModel = "spring", uses = {InternalMapper.class, AcademicPeriodsMapper.class, StudentEventMapper.class, })
public interface OnGoingEventMapper extends EntityMapper <OnGoingEventDTO, OnGoingEvent> {

    @Mapping(source = "owner.idPartyRole", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")

    @Mapping(source = "period.idPeriod", target = "periodId")
    @Mapping(source = "period.description", target = "periodDescription")

    @Mapping(source = "event.idStudentEvent", target = "eventId")
    @Mapping(source = "event.description", target = "eventDescription")
    OnGoingEventDTO toDto(OnGoingEvent onGoingEvent); 

    @Mapping(source = "ownerId", target = "owner")

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "eventId", target = "event")
    OnGoingEvent toEntity(OnGoingEventDTO onGoingEventDTO); 

    default OnGoingEvent fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OnGoingEvent onGoingEvent = new OnGoingEvent();
        onGoingEvent.setIdEventGo(id);
        return onGoingEvent;
    }
}
