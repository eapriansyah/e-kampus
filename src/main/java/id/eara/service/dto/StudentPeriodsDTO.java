package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the StudentPeriods entity.
 * atiila consulting
 */

public class StudentPeriodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idStudentPeriod;

    private Integer seqnum;

    private Integer credit;

    private Integer totalCredit;

    private Integer totalPoint;

    private Integer currentPoint;

    private Integer status;

    private UUID studentId;

    private String studentName;

    private UUID semesterId;

    private String semesterDescription;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public UUID getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(UUID academicPeriodsId) {
        this.semesterId = academicPeriodsId;
    }

    public String getSemesterDescription() {
        return semesterDescription;
    }

    public void setSemesterDescription(String academicPeriodsDescription) {
        this.semesterDescription = academicPeriodsDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudentPeriodsDTO studentPeriodsDTO = (StudentPeriodsDTO) o;
        if(studentPeriodsDTO.getIdStudentPeriod() == null || getIdStudentPeriod() == null) {
            return false;
        }
        return Objects.equals(getIdStudentPeriod(), studentPeriodsDTO.getIdStudentPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudentPeriod());
    }

    @Override
    public String toString() {
        return "StudentPeriodsDTO{" +
            "id=" + getIdStudentPeriod() +
            ", seqnum='" + getSeqnum() + "'" +
            ", credit='" + getCredit() + "'" +
            ", totalCredit='" + getTotalCredit() + "'" +
            ", totalPoint='" + getTotalPoint() + "'" +
            ", currentPoint='" + getCurrentPoint() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
