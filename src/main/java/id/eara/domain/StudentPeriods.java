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
 * Class definition for Entity StudentPeriods.
 */

@Entity
@Table(name = "student_periods")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "studentperiods")
public class StudentPeriods implements Serializable {

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

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name="idstudent", referencedColumnName="idparrol")
    private Student student;

    @ManyToOne
    @JoinColumn(name="idperiod", referencedColumnName="idperiod")
    private AcademicPeriods semester;

    public UUID getIdStudentPeriod() {
        return this.idStudentPeriod;
    }

    public void setIdStudentPeriod(UUID id) {
        this.idStudentPeriod = id;
    }


    public Integer getSeqnum() {
        return seqnum;
    }

    public StudentPeriods seqnum(Integer seqnum) {
        this.seqnum = seqnum;
        return this;
    }

    public void setSeqnum(Integer seqnum) {
        this.seqnum = seqnum;
    }

    public Integer getCredit() {
        return credit;
    }

    public StudentPeriods credit(Integer credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getTotalCredit() {
        return totalCredit;
    }

    public StudentPeriods totalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
        return this;
    }

    public void setTotalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public StudentPeriods totalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
        return this;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public StudentPeriods currentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
        return this;
    }

    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Integer getStatus() {
        return status;
    }

    public StudentPeriods status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public StudentPeriods student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AcademicPeriods getSemester() {
        return semester;
    }

    public StudentPeriods semester(AcademicPeriods academicPeriods) {
        this.semester = academicPeriods;
        return this;
    }

    public void setSemester(AcademicPeriods academicPeriods) {
        this.semester = academicPeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentPeriods studentPeriods = (StudentPeriods) o;
        if (studentPeriods.idStudentPeriod == null || this.idStudentPeriod == null) {
            return false;
        }
        return Objects.equals(this.idStudentPeriod, studentPeriods.idStudentPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idStudentPeriod);
    }

    @Override
    public String toString() {
        return "StudentPeriods{" +
            "idStudentPeriod=" + this.idStudentPeriod +
            ", seqnum='" + getSeqnum() + "'" +
            ", credit='" + getCredit() + "'" +
            ", totalCredit='" + getTotalCredit() + "'" +
            ", totalPoint='" + getTotalPoint() + "'" +
            ", currentPoint='" + getCurrentPoint() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
