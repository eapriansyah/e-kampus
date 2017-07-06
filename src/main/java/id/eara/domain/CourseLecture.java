package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity CourseLecture.
 */

@Entity
@Table(name = "course_lecture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "courselecture")
public class CourseLecture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID idCourseLecture;

    @Column(name = "date_from")
    private ZonedDateTime dateFrom;

    @Column(name = "date_thru")
    private ZonedDateTime dateThru;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="idlecture", referencedColumnName="idparrol")
    private Lecture lecture;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="idcourse", referencedColumnName="idcourse")
    private Course course;

    public UUID getIdCourseLecture() {
        return this.idCourseLecture;
    }

    public void setIdCourseLecture(UUID id) {
        this.idCourseLecture = id;
    }


    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public CourseLecture dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public CourseLecture dateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
        return this;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public CourseLecture lecture(Lecture lecture) {
        this.lecture = lecture;
        return this;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Course getCourse() {
        return course;
    }

    public CourseLecture course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseLecture courseLecture = (CourseLecture) o;
        if (courseLecture.idCourseLecture == null || this.idCourseLecture == null) {
            return false;
        }
        return Objects.equals(this.idCourseLecture, courseLecture.idCourseLecture);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idCourseLecture);
    }

    @Override
    public String toString() {
        return "CourseLecture{" +
            "idCourseLecture=" + this.idCourseLecture +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            '}';
    }
}
