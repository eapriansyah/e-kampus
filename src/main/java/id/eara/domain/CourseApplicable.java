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
 * Class definition for Entity CourseApplicable.
 */

@Entity
@Table(name = "course_applicable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "courseapplicable")
public class CourseApplicable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idapplcourse", columnDefinition = "BINARY(16)")
    private UUID idApplCourse;

    @Column(name = "date_from")
    private ZonedDateTime dateFrom;

    @Column(name = "date_thru")
    private ZonedDateTime dateThru;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="idprody", referencedColumnName="idparrol")
    private ProgramStudy prody;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="idcourse", referencedColumnName="idcourse")
    private Course course;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name="idperiodtype", referencedColumnName="idperiodtype")
    private PeriodType periodType;

    public UUID getIdApplCourse() {
        return this.idApplCourse;
    }

    public void setIdApplCourse(UUID id) {
        this.idApplCourse = id;
    }


    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public CourseApplicable dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public CourseApplicable dateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
        return this;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public ProgramStudy getPrody() {
        return prody;
    }

    public CourseApplicable prody(ProgramStudy programStudy) {
        this.prody = programStudy;
        return this;
    }

    public void setPrody(ProgramStudy programStudy) {
        this.prody = programStudy;
    }

    public Course getCourse() {
        return course;
    }

    public CourseApplicable course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public CourseApplicable periodType(PeriodType periodType) {
        this.periodType = periodType;
        return this;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseApplicable courseApplicable = (CourseApplicable) o;
        if (courseApplicable.idApplCourse == null || this.idApplCourse == null) {
            return false;
        }
        return Objects.equals(this.idApplCourse, courseApplicable.idApplCourse);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idApplCourse);
    }

    @Override
    public String toString() {
        return "CourseApplicable{" +
            "idApplCourse=" + this.idApplCourse +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            '}';
    }
}
