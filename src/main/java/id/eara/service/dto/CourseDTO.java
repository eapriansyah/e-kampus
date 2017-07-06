package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Course entity.
 * atiila consulting
 */

public class CourseDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idCourse;

    private String courseCode;

    private String description;

    private Integer credit;

    private UUID ownerId;

    private String ownerName;

    public UUID getIdCourse() {
        return this.idCourse;
    }

    public void setIdCourse(UUID id) {
        this.idCourse = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID internalId) {
        this.ownerId = internalId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String internalName) {
        this.ownerName = internalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseDTO courseDTO = (CourseDTO) o;
        if(courseDTO.getIdCourse() == null || getIdCourse() == null) {
            return false;
        }
        return Objects.equals(getIdCourse(), courseDTO.getIdCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdCourse());
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + getIdCourse() +
            ", courseCode='" + getCourseCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", credit='" + getCredit() + "'" +
            "}";
    }
}
