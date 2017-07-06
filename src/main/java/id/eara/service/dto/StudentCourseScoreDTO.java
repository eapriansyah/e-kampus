package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the StudentCourseScore entity.
 * atiila consulting
 */

public class StudentCourseScoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idStudentCourseScore;

    private Integer credit;

    private Float value;

    private String valueString;

    private Integer status;

    private UUID periodId;

    private String periodDescription;

    private UUID courseId;

    private String courseDescription;

    private UUID studentId;

    private String studentName;

    public UUID getIdStudentCourseScore() {
        return this.idStudentCourseScore;
    }

    public void setIdStudentCourseScore(UUID id) {
        this.idStudentCourseScore = id;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        StudentCourseScoreDTO studentCourseScoreDTO = (StudentCourseScoreDTO) o;
        if(studentCourseScoreDTO.getIdStudentCourseScore() == null || getIdStudentCourseScore() == null) {
            return false;
        }
        return Objects.equals(getIdStudentCourseScore(), studentCourseScoreDTO.getIdStudentCourseScore());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudentCourseScore());
    }

    @Override
    public String toString() {
        return "StudentCourseScoreDTO{" +
            "id=" + getIdStudentCourseScore() +
            ", credit='" + getCredit() + "'" +
            ", value='" + getValue() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
