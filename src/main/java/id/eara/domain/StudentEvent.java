package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity StudentEvent.
 */

@Entity
@Table(name = "student_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studentevent")
public class StudentEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstueve")
    private Long idStudentEvent;

    @Column(name = "id_event")
    private String idEvent;

    @Column(name = "description")
    private String description;

    @Column(name = "registrationtype")
    private Integer registrationtype;

    @ManyToOne
    @JoinColumn(name="ideveactdone", referencedColumnName="ideveact")
    private EventAction actionDone;

    @ManyToOne
    @JoinColumn(name="ideveactfailed", referencedColumnName="ideveact")
    private EventAction actionFailed;

    public Long getIdStudentEvent() {
        return this.idStudentEvent;
    }

    public void setIdStudentEvent(Long id) {
        this.idStudentEvent = id;
    }


    public String getIdEvent() {
        return idEvent;
    }

    public StudentEvent idEvent(String idEvent) {
        this.idEvent = idEvent;
        return this;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getDescription() {
        return description;
    }

    public StudentEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegistrationtype() {
        return registrationtype;
    }

    public StudentEvent registrationtype(Integer registrationtype) {
        this.registrationtype = registrationtype;
        return this;
    }

    public void setRegistrationtype(Integer registrationtype) {
        this.registrationtype = registrationtype;
    }

    public EventAction getActionDone() {
        return actionDone;
    }

    public StudentEvent actionDone(EventAction eventAction) {
        this.actionDone = eventAction;
        return this;
    }

    public void setActionDone(EventAction eventAction) {
        this.actionDone = eventAction;
    }

    public EventAction getActionFailed() {
        return actionFailed;
    }

    public StudentEvent actionFailed(EventAction eventAction) {
        this.actionFailed = eventAction;
        return this;
    }

    public void setActionFailed(EventAction eventAction) {
        this.actionFailed = eventAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentEvent studentEvent = (StudentEvent) o;
        if (studentEvent.idStudentEvent == null || this.idStudentEvent == null) {
            return false;
        }
        return Objects.equals(this.idStudentEvent, studentEvent.idStudentEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudentEvent);
    }

    @Override
    public String toString() {
        return "StudentEvent{" +
            "idStudentEvent=" + this.idStudentEvent +
            ", idEvent='" + getIdEvent() + "'" +
            ", description='" + getDescription() + "'" +
            ", registrationtype='" + getRegistrationtype() + "'" +
            '}';
    }
}
