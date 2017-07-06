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
 * Class definition for Entity ClassStudy.
 */

@Entity
@Table(name = "class_study")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "classstudy")
public class ClassStudy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idclastu", columnDefinition = "BINARY(16)")
    private UUID idClassStudy;

    @Column(name = "description")
    private String description;

    @Column(name = "refkey")
    private String refkey;

    @ManyToOne
    @JoinColumn(name="idcourse", referencedColumnName="idcourse")
    private Course course;

    @ManyToOne
    @JoinColumn(name="idprody", referencedColumnName="idparrol")
    private ProgramStudy prody;

    @ManyToOne
    @JoinColumn(name="idperiod", referencedColumnName="idperiod")
    private AcademicPeriods period;

    @ManyToOne
    @JoinColumn(name="idlecture", referencedColumnName="idparrol")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name="idsecondarylecture", referencedColumnName="idparrol")
    private Lecture secondaryLecture;

    @ManyToOne
    @JoinColumn(name="idthirdlecture", referencedColumnName="idparrol")
    private Lecture thirdLecture;

    public UUID getIdClassStudy() {
        return this.idClassStudy;
    }

    public void setIdClassStudy(UUID id) {
        this.idClassStudy = id;
    }


    public String getDescription() {
        return description;
    }

    public ClassStudy description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefkey() {
        return refkey;
    }

    public ClassStudy refkey(String refkey) {
        this.refkey = refkey;
        return this;
    }

    public void setRefkey(String refkey) {
        this.refkey = refkey;
    }

    public Course getCourse() {
        return course;
    }

    public ClassStudy course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ProgramStudy getPrody() {
        return prody;
    }

    public ClassStudy prody(ProgramStudy programStudy) {
        this.prody = programStudy;
        return this;
    }

    public void setPrody(ProgramStudy programStudy) {
        this.prody = programStudy;
    }

    public AcademicPeriods getPeriod() {
        return period;
    }

    public ClassStudy period(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
        return this;
    }

    public void setPeriod(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public ClassStudy lecture(Lecture lecture) {
        this.lecture = lecture;
        return this;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecture getSecondaryLecture() {
        return secondaryLecture;
    }

    public ClassStudy secondaryLecture(Lecture lecture) {
        this.secondaryLecture = lecture;
        return this;
    }

    public void setSecondaryLecture(Lecture lecture) {
        this.secondaryLecture = lecture;
    }

    public Lecture getThirdLecture() {
        return thirdLecture;
    }

    public ClassStudy thirdLecture(Lecture lecture) {
        this.thirdLecture = lecture;
        return this;
    }

    public void setThirdLecture(Lecture lecture) {
        this.thirdLecture = lecture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassStudy classStudy = (ClassStudy) o;
        if (classStudy.idClassStudy == null || this.idClassStudy == null) {
            return false;
        }
        return Objects.equals(this.idClassStudy, classStudy.idClassStudy);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idClassStudy);
    }

    @Override
    public String toString() {
        return "ClassStudy{" +
            "idClassStudy=" + this.idClassStudy +
            ", description='" + getDescription() + "'" +
            ", refkey='" + getRefkey() + "'" +
            '}';
    }
}
