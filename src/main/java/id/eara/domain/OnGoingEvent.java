package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity OnGoingEvent.
 */

@Entity
@Table(name = "on_going_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "ongoingevent")
public class OnGoingEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ideventgo", columnDefinition = "BINARY(16)")
    private UUID idEventGo;

    @Column(name = "id_event")
    private String idEvent;

    @Column(name = "description")
    private String description;

    @Column(name = "date_from")
    private ZonedDateTime dateFrom;

    @Column(name = "date_thru")
    private ZonedDateTime dateThru;

    @ManyToOne
    @JoinColumn(name="idowner", referencedColumnName="idparrol")
    private Internal owner;

    @ManyToOne
    @JoinColumn(name="idperiod", referencedColumnName="idperiod")
    private AcademicPeriods period;

    @ManyToOne
    @JoinColumn(name="idstueve", referencedColumnName="idstueve")
    private StudentEvent event;

    public UUID getIdEventGo() {
        return this.idEventGo;
    }

    public void setIdEventGo(UUID id) {
        this.idEventGo = id;
    }


    public String getIdEvent() {
        return idEvent;
    }

    public OnGoingEvent idEvent(String idEvent) {
        this.idEvent = idEvent;
        return this;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getDescription() {
        return description;
    }

    public OnGoingEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public OnGoingEvent dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public OnGoingEvent dateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
        return this;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public Internal getOwner() {
        return owner;
    }

    public OnGoingEvent owner(Internal internal) {
        this.owner = internal;
        return this;
    }

    public void setOwner(Internal internal) {
        this.owner = internal;
    }

    public AcademicPeriods getPeriod() {
        return period;
    }

    public OnGoingEvent period(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
        return this;
    }

    public void setPeriod(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
    }

    public StudentEvent getEvent() {
        return event;
    }

    public OnGoingEvent event(StudentEvent studentEvent) {
        this.event = studentEvent;
        return this;
    }

    public void setEvent(StudentEvent studentEvent) {
        this.event = studentEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OnGoingEvent onGoingEvent = (OnGoingEvent) o;
        if (onGoingEvent.idEventGo == null || this.idEventGo == null) {
            return false;
        }
        return Objects.equals(this.idEventGo, onGoingEvent.idEventGo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idEventGo);
    }

    @Override
    public String toString() {
        return "OnGoingEvent{" +
            "idEventGo=" + this.idEventGo +
            ", idEvent='" + getIdEvent() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            '}';
    }
}
