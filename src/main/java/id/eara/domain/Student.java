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
 * Class definition for Entity Student.
 */

@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @NotNull
    @Column(name = "nim", nullable = false)
    private String nim;

    @Column(name = "name")
    private String name;

    @Column(name = "classof")
    private Integer classof;

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


    public String getNim() {
        return nim;
    }

    public Student nim(String nim) {
        this.nim = nim;
        return this;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public Student name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClassof() {
        return classof;
    }

    public Student classof(Integer classof) {
        this.classof = classof;
        return this;
    }

    public void setClassof(Integer classof) {
        this.classof = classof;
    }

    public Integer getStatus() {
        return status;
    }

    public Student status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ProgramStudy getPrody() {
        return prody;
    }

    public Student prody(ProgramStudy ProgramStudy) {
        this.prody = ProgramStudy;
        return this;
    }

    public void setPrody(ProgramStudy ProgramStudy) {
        this.prody = ProgramStudy;
    }

    public StudyPath getStudyPath() {
        return studyPath;
    }

    public Student studyPath(StudyPath StudyPath) {
        this.studyPath = StudyPath;
        return this;
    }

    public void setStudyPath(StudyPath StudyPath) {
        this.studyPath = StudyPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if (student.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, student.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "Student{" +
            "idPartyRole=" + this.idPartyRole +
            ", nim='" + getNim() + "'" +
            ", name='" + getName() + "'" +
            ", classof='" + getClassof() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
