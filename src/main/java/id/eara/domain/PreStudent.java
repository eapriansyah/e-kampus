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
 * Class definition for Entity PreStudent.
 */

@Entity
@Table(name = "pre_student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "prestudent")
public class PreStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @Column(name = "id_pre_student")
    private String idPreStudent;

    @Column(name = "name")
    private String name;

    @Column(name = "year_of")
    private Integer yearOf;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name="idprogramstudy", referencedColumnName="idparrol")
    private ProgramStudy prody;

    @ManyToOne
    @JoinColumn(name="idstupat", referencedColumnName="idstupat")
    private StudyPath studyPath;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdPreStudent() {
        return idPreStudent;
    }

    public PreStudent idPreStudent(String idPreStudent) {
        this.idPreStudent = idPreStudent;
        return this;
    }

    public void setIdPreStudent(String idPreStudent) {
        this.idPreStudent = idPreStudent;
    }

    public String getName() {
        return name;
    }

    public PreStudent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearOf() {
        return yearOf;
    }

    public PreStudent yearOf(Integer yearOf) {
        this.yearOf = yearOf;
        return this;
    }

    public void setYearOf(Integer yearOf) {
        this.yearOf = yearOf;
    }

    public Integer getStatus() {
        return status;
    }

    public PreStudent status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ProgramStudy getPrody() {
        return prody;
    }

    public PreStudent prody(ProgramStudy programStudy) {
        this.prody = programStudy;
        return this;
    }

    public void setPrody(ProgramStudy programStudy) {
        this.prody = programStudy;
    }

    public StudyPath getStudyPath() {
        return studyPath;
    }

    public PreStudent studyPath(StudyPath studyPath) {
        this.studyPath = studyPath;
        return this;
    }

    public void setStudyPath(StudyPath studyPath) {
        this.studyPath = studyPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreStudent preStudent = (PreStudent) o;
        if (preStudent.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, preStudent.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "PreStudent{" +
            "idPartyRole=" + this.idPartyRole +
            ", idPreStudent='" + getIdPreStudent() + "'" +
            ", name='" + getName() + "'" +
            ", yearOf='" + getYearOf() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
