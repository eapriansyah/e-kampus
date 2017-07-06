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
 * Class definition for Entity StudentPeriodData.
 */

@Entity
@Table(name = "student_period_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studentperioddata")
public class StudentPeriodData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idstuper", columnDefinition = "BINARY(16)")
    private UUID idStudentPeriod;

    @Column(name = "seqnum")
    private Integer seqnum;

    @Column(name = "credit")
    private Integer credit;

    @Column(name = "total_credit")
    private Integer totalCredit;

    @Column(name = "total_point")
    private Integer totalPoint;

    @Column(name = "current_point")
    private Integer currentPoint;

    @ManyToOne
    @JoinColumn(name="idperiod", referencedColumnName="idperiod")
    private AcademicPeriods period;

    @ManyToOne
    @JoinColumn(name="idcourse", referencedColumnName="idcourse")
    private Course course;

    @ManyToOne
    @JoinColumn(name="idstudent", referencedColumnName="idparrol")
    private Student student;

    public UUID getIdStudentPeriod() {
        return this.idStudentPeriod;
    }

    public void setIdStudentPeriod(UUID id) {
        this.idStudentPeriod = id;
    }


    public Integer getSeqnum() {
        return seqnum;
    }

    public StudentPeriodData seqnum(Integer seqnum) {
        this.seqnum = seqnum;
        return this;
    }

    public void setSeqnum(Integer seqnum) {
        this.seqnum = seqnum;
    }

    public Integer getCredit() {
        return credit;
    }

    public StudentPeriodData credit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getTotalCredit() {
        return totalCredit;
    }

    public StudentPeriodData totalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
        return this;
    }

    public void setTotalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public StudentPeriodData totalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
        return this;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public StudentPeriodData currentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
        return this;
    }

    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    public AcademicPeriods getPeriod() {
        return period;
    }

    public StudentPeriodData period(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
        return this;
    }

    public void setPeriod(AcademicPeriods academicPeriods) {
        this.period = academicPeriods;
    }

    public Course getCourse() {
        return course;
    }

    public StudentPeriodData course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public StudentPeriodData student(Student student) {
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
        StudentPeriodData studentPeriodData = (StudentPeriodData) o;
        if (studentPeriodData.idStudentPeriod == null || this.idStudentPeriod == null) {
            return false;
        }
        return Objects.equals(this.idStudentPeriod, studentPeriodData.idStudentPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudentPeriod);
    }

    @Override
    public String toString() {
        return "StudentPeriodData{" +
            "idStudentPeriod=" + this.idStudentPeriod +
            ", seqnum='" + getSeqnum() + "'" +
            ", credit='" + getCredit() + "'" +
            ", totalCredit='" + getTotalCredit() + "'" +
            ", totalPoint='" + getTotalPoint() + "'" +
            ", currentPoint='" + getCurrentPoint() + "'" +
            '}';
    }
}
