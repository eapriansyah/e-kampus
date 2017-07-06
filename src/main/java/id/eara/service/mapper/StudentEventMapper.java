package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentEventDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity StudentEvent and its DTO StudentEventDTO.
 */
@Mapper(componentModel = "spring", uses = {EventActionMapper.class, })
public interface StudentEventMapper extends EntityMapper <StudentEventDTO, StudentEvent> {

    @Mapping(source = "actionDone.idEventAction", target = "actionDoneId")
    @Mapping(source = "actionDone.description", target = "actionDoneDescription")

    @Mapping(source = "actionFailed.idEventAction", target = "actionFailedId")
    @Mapping(source = "actionFailed.description", target = "actionFailedDescription")
    StudentEventDTO toDto(StudentEvent studentEvent); 

    @Mapping(source = "actionDoneId", target = "actionDone")

    @Mapping(source = "actionFailedId", target = "actionFailed")
    StudentEvent toEntity(StudentEventDTO studentEventDTO); 

    default StudentEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentEvent studentEvent = new StudentEvent();
        studentEvent.setIdStudentEvent(id);
        return studentEvent;
    }
}
