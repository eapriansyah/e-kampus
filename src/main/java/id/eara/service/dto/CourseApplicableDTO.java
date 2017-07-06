package id.eara.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the CourseApplicable entity.
 * atiila consulting
 */

public class CourseApplicableDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idApplCourse;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateThru;

    private UUID prodyId;

    private String prodyName;

    private UUID courseId;

    private String courseDescription;

    private Long periodTypeId;

    private String periodTypeDescription;

    public UUID getIdApplCourse() {
        return this.idApplCourse;
    }

    public void setIdApplCourse(UUID id) {
        this.idApplCourse = id;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public UUID getProdyId() {
        return prodyId;
    }

    public void setProdyId(UUID programStudyId) {
        this.prodyId = programStudyId;
    }

    public String getProdyName() {
        return prodyName;
    }

    public void setProdyName(String programStudyName) {
        this.prodyName = programStudyName;
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

    public Long getPeriodTypeId() {
        return periodTypeId;
    }

    public void setPeriodTypeId(Long periodTypeId) {
        this.periodTypeId = periodTypeId;
    }

    public String getPeriodTypeDescription() {
        return periodTypeDescription;
    }

    public void setPeriodTypeDescription(String periodTypeDescription) {
        this.periodTypeDescription = periodTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseApplicableDTO courseApplicableDTO = (CourseApplicableDTO) o;
        if(courseApplicableDTO.getIdApplCourse() == null || getIdApplCourse() == null) {
            return false;
        }
        return Objects.equals(getIdApplCourse(), courseApplicableDTO.getIdApplCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdApplCourse());
    }

    @Override
    public String toString() {
        return "CourseApplicableDTO{" +
            "id=" + getIdApplCourse() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            "}";
    }
}
