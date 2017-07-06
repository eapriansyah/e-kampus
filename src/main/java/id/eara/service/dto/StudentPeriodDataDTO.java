package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the StudentPeriodData entity.
 * atiila consulting
 */

public class StudentPeriodDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idStudentPeriod;

    private Integer seqnum;

    private Integer credit;

    private Integer totalCredit;

    private Integer totalPoint;

    private Integer currentPoint;

    private UUID periodId;

    private String periodDescription;

    private UUID courseId;

    private String courseDescription;

    private UUID studentId;

    private String studentName;

    public UUID getIdStudentPeriod() {
        return this.idStudentPeriod;
    }

    public void setIdStudentPeriod(UUID id) {
        this.idStudentPeriod = id;
    }

    public Integer getSeqnum() {
        return seqnum;
    }

    public void setSeqnum(Integer seqnum) {
        this.seqnum = seqnum;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Integer totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    public UUID getPeriodId() {
        return periodId;
    }

    public void setPeriodId(UUID academicPeriodsId) {
        this.periodId = academicPeriodsId;
    }

    public String getPeriodDescription() {
        return periodDescription;
    }

    public void setPeriodDescription(String academicPeriodsDescription) {
        this.periodDescription = academicPeriodsDescription;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudentPeriodDataDTO studentPeriodDataDTO = (StudentPeriodDataDTO) o;
        if(studentPeriodDataDTO.getIdStudentPeriod() == null || getIdStudentPeriod() == null) {
            return false;
        }
        return Objects.equals(getIdStudentPeriod(), studentPeriodDataDTO.getIdStudentPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudentPeriod());
    }

    @Override
    public String toString() {
        return "StudentPeriodDataDTO{" +
            "id=" + getIdStudentPeriod() +
            ", seqnum='" + getSeqnum() + "'" +
            ", credit='" + getCredit() + "'" +
            ", totalCredit='" + getTotalCredit() + "'" +
            ", totalPoint='" + getTotalPoint() + "'" +
            ", currentPoint='" + getCurrentPoint() + "'" +
            "}";
    }
}
