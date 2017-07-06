package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the EventAction entity.
 * atiila consulting
 */

public class EventActionDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idEventAction;

    private String description;

    public Long getIdEventAction() {
        return this.idEventAction;
    }

    public void setIdEventAction(Long id) {
        this.idEventAction = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventActionDTO eventActionDTO = (EventActionDTO) o;
        if(eventActionDTO.getIdEventAction() == null || getIdEventAction() == null) {
            return false;
        }
        return Objects.equals(getIdEventAction(), eventActionDTO.getIdEventAction());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdEventAction());
    }

    @Override
    public String toString() {
        return "EventActionDTO{" +
            "id=" + getIdEventAction() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
