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
 * Class definition for Entity Lecture.
 */

@Entity
@Table(name = "lecture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "lecture")
public class Lecture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @Column(name = "id_lecture")
    private String idLecture;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdLecture() {
        return idLecture;
    }

    public Lecture idLecture(String idLecture) {
        this.idLecture = idLecture;
        return this;
    }

    public void setIdLecture(String idLecture) {
        this.idLecture = idLecture;
    }

    public String getName() {
        return name;
    }

    public Lecture name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public Lecture status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lecture lecture = (Lecture) o;
        if (lecture.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, lecture.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "Lecture{" +
            "idPartyRole=" + this.idPartyRole +
            ", idLecture='" + getIdLecture() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
