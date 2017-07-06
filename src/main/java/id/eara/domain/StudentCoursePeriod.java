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
 * Class definition for Entity StudentCoursePeriod.
 */

@Entity
@Table(name = "student_course_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studentcourseperiod")
public class StudentCoursePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idstucouper", columnDefinition = "BINARY(16)")
    private UUID idStudentCoursePeriod;

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

    public UUID getIdStudentCoursePeriod() {
        return this.idStudentCoursePeriod;
    }

    public void setIdStudentCoursePeriod(UUID id) {
        this.idStudentCoursePeriod = id;
    }


    public Integer getCredit() {
        return credit;
    }

    public StudentCoursePeriod credit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Float getValue() {
        return value;
    }

    public StudentCoursePeriod value(Float value) {
        this.value = value;
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getValueString() {
        return valueString;
    }

    public StudentCoursePeriod valueString(String valueString) {
        this.valueString = valueString;
        return this;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Integer getStatus() {
        return status;
    }

    public StudentCoursePeriod status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AcademicPeriods getPeriod() {
        return period;
    }

    public StudentCoursePeriod period(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
        return this;
    }

    public void setPeriod(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
    }

    public Course getCourse() {
        return course;
    }

    public StudentCoursePeriod course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public StudentCoursePeriod student(Student student) {
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
        StudentCoursePeriod studentCoursePeriod = (StudentCoursePeriod) o;
        if (studentCoursePeriod.idStudentCoursePeriod == null || this.idStudentCoursePeriod == null) {
            return false;
        }
        return Objects.equals(this.idStudentCoursePeriod, studentCoursePeriod.idStudentCoursePeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudentCoursePeriod);
    }

    @Override
    public String toString() {
        return "StudentCoursePeriod{" +
            "idStudentCoursePeriod=" + this.idStudentCoursePeriod +
            ", credit='" + getCredit() + "'" +
            ", value='" + getValue() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
