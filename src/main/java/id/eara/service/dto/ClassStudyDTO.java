package id.eara.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the ClassStudy entity.
 * atiila consulting
 */

public class ClassStudyDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idClassStudy;

    private String description;

    private String refkey;

    private UUID courseId;

    private String courseDescription;

    private UUID prodyId;

    private String prodyName;

    private UUID periodId;

    private String periodDescription;

    private UUID lectureId;

    private String lectureName;

    private UUID secondaryLectureId;

    private String secondaryLectureName;

    private UUID thirdLectureId;

    private String thirdLectureName;

    public UUID getIdClassStudy() {
        return this.idClassStudy;
    }

    public void setIdClassStudy(UUID id) {
        this.idClassStudy = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefkey() {
        return refkey;
    }

    public void setRefkey(String refkey) {
        this.refkey = refkey;
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

    public UUID getSecondaryLectureId() {
        return secondaryLectureId;
    }

    public void setSecondaryLectureId(UUID lectureId) {
        this.secondaryLectureId = lectureId;
    }

    public String getSecondaryLectureName() {
        return secondaryLectureName;
    }

    public void setSecondaryLectureName(String lectureName) {
        this.secondaryLectureName = lectureName;
    }

    public UUID getThirdLectureId() {
        return thirdLectureId;
    }

    public void setThirdLectureId(UUID lectureId) {
        this.thirdLectureId = lectureId;
    }

    public String getThirdLectureName() {
        return thirdLectureName;
    }

    public void setThirdLectureName(String lectureName) {
        this.thirdLectureName = lectureName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassStudyDTO classStudyDTO = (ClassStudyDTO) o;
        if(classStudyDTO.getIdClassStudy() == null || getIdClassStudy() == null) {
            return false;
        }
        return Objects.equals(getIdClassStudy(), classStudyDTO.getIdClassStudy());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdClassStudy());
    }

    @Override
    public String toString() {
        return "ClassStudyDTO{" +
            "id=" + getIdClassStudy() +
            ", description='" + getDescription() + "'" +
            ", refkey='" + getRefkey() + "'" +
            "}";
    }
}
