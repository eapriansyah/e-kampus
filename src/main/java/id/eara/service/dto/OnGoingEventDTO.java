package id.eara.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the OnGoingEvent entity.
 * atiila consulting
 */

public class OnGoingEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idEventGo;

    private String idEvent;

    private String description;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateThru;

    private UUID ownerId;

    private String ownerName;

    private UUID periodId;

    private String periodDescription;

    private Long eventId;

    private String eventDescription;

    public UUID getIdEventGo() {
        return this.idEventGo;
    }

    public void setIdEventGo(UUID id) {
        this.idEventGo = id;
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

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID internalId) {
        this.ownerId = internalId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String internalName) {
        this.ownerName = internalName;
    }

    public UUID getPeriodId() {
        return periodId;
    }

    public void setPeriodId(UUID academicPeriodsId) {
        this.periodId = academicPeriodsId;
    }

    public String getPeriodDescription() {
        return periodDescription;
    }

    public void setPeriodDescription(String academicPeriodsDescription) {
        this.periodDescription = academicPeriodsDescription;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long studentEventId) {
        this.eventId = studentEventId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String studentEventDescription) {
        this.eventDescription = studentEventDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OnGoingEventDTO onGoingEventDTO = (OnGoingEventDTO) o;
        if(onGoingEventDTO.getIdEventGo() == null || getIdEventGo() == null) {
            return false;
        }
        return Objects.equals(getIdEventGo(), onGoingEventDTO.getIdEventGo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdEventGo());
    }

    @Override
    public String toString() {
        return "OnGoingEventDTO{" +
            "id=" + getIdEventGo() +
            ", idEvent='" + getIdEvent() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            "}";
    }
}
