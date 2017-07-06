package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the StudentCoursePeriod entity.
 * atiila consulting
 */

public class StudentCoursePeriodDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idStudentCoursePeriod;

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

    public UUID getIdStudentCoursePeriod() {
        return this.idStudentCoursePeriod;
    }

    public void setIdStudentCoursePeriod(UUID id) {
        this.idStudentCoursePeriod = id;
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

        StudentCoursePeriodDTO studentCoursePeriodDTO = (StudentCoursePeriodDTO) o;
        if(studentCoursePeriodDTO.getIdStudentCoursePeriod() == null || getIdStudentCoursePeriod() == null) {
            return false;
        }
        return Objects.equals(getIdStudentCoursePeriod(), studentCoursePeriodDTO.getIdStudentCoursePeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdStudentCoursePeriod());
    }

    @Override
    public String toString() {
        return "StudentCoursePeriodDTO{" +
            "id=" + getIdStudentCoursePeriod() +
            ", credit='" + getCredit() + "'" +
            ", value='" + getValue() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
