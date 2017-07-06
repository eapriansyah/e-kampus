package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity Faculty.
 */

@Entity
@Table(name = "faculty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "faculty")
public class Faculty implements Serializable {

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

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="iduniversity", referencedColumnName="idparrol")
    private University university;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdInternal() {
        return idInternal;
    }

    public Faculty idInternal(String idInternal) {
        this.idInternal = idInternal;
        return this;
    }

    public void setIdInternal(String idInternal) {
        this.idInternal = idInternal;
    }

    public String getName() {
        return name;
    }

    public Faculty name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public University getUniversity() {
        return university;
    }

    public Faculty university(University university) {
        this.university = university;
        return this;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Faculty faculty = (Faculty) o;
        if (faculty.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, faculty.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "Faculty{" +
            "idPartyRole=" + this.idPartyRole +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            '}';
    }
}
