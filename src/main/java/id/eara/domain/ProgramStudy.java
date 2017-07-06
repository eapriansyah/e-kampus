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
 * Class definition for Entity ProgramStudy.
 */

@Entity
@Table(name = "program_study")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "programstudy")
public class ProgramStudy implements Serializable {

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

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name="iddegree", referencedColumnName="iddegree")
    private Degree degree;

    @ManyToOne
    @JoinColumn(name="idfaculty", referencedColumnName="idparrol")
    private Faculty faculty;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdInternal() {
        return idInternal;
    }

    public ProgramStudy idInternal(String idInternal) {
        this.idInternal = idInternal;
        return this;
    }

    public void setIdInternal(String idInternal) {
        this.idInternal = idInternal;
    }

    public String getName() {
        return name;
    }

    public ProgramStudy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public ProgramStudy status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Degree getDegree() {
        return degree;
    }

    public ProgramStudy degree(Degree Degree) {
        this.degree = Degree;
        return this;
    }

    public void setDegree(Degree Degree) {
        this.degree = Degree;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public ProgramStudy faculty(Faculty Faculty) {
        this.faculty = Faculty;
        return this;
    }

    public void setFaculty(Faculty Faculty) {
        this.faculty = Faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramStudy programStudy = (ProgramStudy) o;
        if (programStudy.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, programStudy.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "ProgramStudy{" +
            "idPartyRole=" + this.idPartyRole +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
