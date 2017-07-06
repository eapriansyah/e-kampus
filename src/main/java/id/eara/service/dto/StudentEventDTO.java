package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the StudentEvent entity.
 * atiila consulting
 */

public class StudentEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idStudentEvent;

    private String idEvent;

    private String description;

    private Integer registrationtype;

    private Long actionDoneId;

    private String actionDoneDescription;

    private Long actionFailedId;

    private String actionFailedDescription;

    public Long getIdStudentEvent() {
        return this.idStudentEvent;
    }

    public void setIdStudentEvent(Long id) {
        this.idStudentEvent = id;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegistrationtype() {
        return registrationtype;
    }

    public void setRegistrationtype(Integer registrationtype) {
        this.registrationtype = registrationtype;
    }

    public Long getActionDoneId() {
        return actionDoneId;
    }

    public void setActionDoneId(Long eventActionId) {
        this.actionDoneId = eventActionId;
    }

    public String getActionDoneDescription() {
        return actionDoneDescription;
    }

    public void setActionDoneDescription(String eventActionDescription) {
        this.actionDoneDescription = eventActionDescription;
    }

    public Long getActionFailedId() {
        return actionFailedId;
    }

    public void setActionFailedId(Long eventActionId) {
        this.actionFailedId = eventActionId;
    }

    public String getActionFailedDescription() {
        return actionFailedDescription;
    }

    public void setActionFailedDescription(String eventActionDescription) {
        this.actionFailedDescription = eventActionDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudentEventDTO studentEventDTO = (StudentEventDTO) o;
        if(studentEventDTO.getIdStudentEvent() == null || getIdStudentEvent() == null) {
            return false;
        }
        return Objects.equals(getIdStudentEvent(), studentEventDTO.getIdStudentEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudentEvent());
    }

    @Override
    public String toString() {
        return "StudentEventDTO{" +
            "id=" + getIdStudentEvent() +
            ", idEvent='" + getIdEvent() + "'" +
            ", description='" + getDescription() + "'" +
            ", registrationtype='" + getRegistrationtype() + "'" +
            "}";
    }
}
