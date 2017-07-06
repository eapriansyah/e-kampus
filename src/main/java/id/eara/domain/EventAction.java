package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity EventAction.
 */

@Entity
@Table(name = "event_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "eventaction")
public class EventAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ideveact")
    private Long idEventAction;

    @Column(name = "description")
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

    public EventAction description(String description) {
        this.description = description;
        return this;
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
        EventAction eventAction = (EventAction) o;
        if (eventAction.idEventAction == null || this.idEventAction == null) {
            return false;
        }
        return Objects.equals(this.idEventAction, eventAction.idEventAction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idEventAction);
    }

    @Override
    public String toString() {
        return "EventAction{" +
            "idEventAction=" + this.idEventAction +
            ", description='" + getDescription() + "'" +
            '}';
    }
}
