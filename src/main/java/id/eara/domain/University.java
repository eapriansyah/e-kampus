package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity University.
 */

@Entity
@Table(name = "university")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "university")
public class University implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @Column(name = "id_internal")
    private String idInternal;

    @Column(name = "name")
    private String name;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdInternal() {
        return idInternal;
    }

    public University idInternal(String idInternal) {
        this.idInternal = idInternal;
        return this;
    }

    public void setIdInternal(String idInternal) {
        this.idInternal = idInternal;
    }

    public String getName() {
        return name;
    }

    public University name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        University university = (University) o;
        if (university.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, university.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "University{" +
            "idPartyRole=" + this.idPartyRole +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            '}';
    }
}
