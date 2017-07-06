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
 * Class definition for Entity StudentCourseScore.
 */

@Entity
@Table(name = "student_course_score")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studentcoursescore")
public class StudentCourseScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idstucousco", columnDefinition = "BINARY(16)")
    private UUID idStudentCourseScore;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "jhi_value")
    private Float value;

    @Column(name = "value_string")
    private String valueString;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name="idperiod", referencedColumnName="idperiod")
    private AcademicPeriods period;

    @ManyToOne
    @JoinColumn(name="idcourse", referencedColumnName="idcourse")
    private Course course;

    @ManyToOne
    @JoinColumn(name="idstudent", referencedColumnName="idparrol")
    private Student student;

    public UUID getIdStudentCourseScore() {
        return this.idStudentCourseScore;
    }

    public void setIdStudentCourseScore(UUID id) {
        this.idStudentCourseScore = id;
    }


    public Integer getCredit() {
        return credit;
    }

    public StudentCourseScore credit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Float getValue() {
        return value;
    }

    public StudentCourseScore value(Float value) {
        this.value = value;
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getValueString() {
        return valueString;
    }

    public StudentCourseScore valueString(String valueString) {
        this.valueString = valueString;
        return this;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Integer getStatus() {
        return status;
    }

    public StudentCourseScore status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AcademicPeriods getPeriod() {
        return period;
    }

    public StudentCourseScore period(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
        return this;
    }

    public void setPeriod(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
    }

    public Course getCourse() {
        return course;
    }

    public StudentCourseScore course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public StudentCourseScore student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentCourseScore studentCourseScore = (StudentCourseScore) o;
        if (studentCourseScore.idStudentCourseScore == null || this.idStudentCourseScore == null) {
            return false;
        }
        return Objects.equals(this.idStudentCourseScore, studentCourseScore.idStudentCourseScore);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudentCourseScore);
    }

    @Override
    public String toString() {
        return "StudentCourseScore{" +
            "idStudentCourseScore=" + this.idStudentCourseScore +
            ", credit='" + getCredit() + "'" +
            ", value='" + getValue() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
