package id.eara.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the CourseLecture entity.
 * atiila consulting
 */

public class CourseLectureDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idCourseLecture;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateThru;

    private UUID lectureId;

    private String lectureName;

    private UUID courseId;

    private String courseDescription;

    public UUID getIdCourseLecture() {
        return this.idCourseLecture;
    }

    public void setIdCourseLecture(UUID id) {
        this.idCourseLecture = id;
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

    public UUID getLectureId() {
        return lectureId;
    }

    public void setLectureId(UUID lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseLectureDTO courseLectureDTO = (CourseLectureDTO) o;
        if(courseLectureDTO.getIdCourseLecture() == null || getIdCourseLecture() == null) {
            return false;
        }
        return Objects.equals(getIdCourseLecture(), courseLectureDTO.getIdCourseLecture());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCourseLecture());
    }

    @Override
    public String toString() {
        return "CourseLectureDTO{" +
            "id=" + getIdCourseLecture() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            "}";
    }
}
