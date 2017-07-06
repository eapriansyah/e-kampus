package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity AcademicPeriods.
 */

@Entity
@Table(name = "academic_periods")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "academicperiods")
public class AcademicPeriods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idperiod", columnDefinition = "BINARY(16)")
    private UUID idPeriod;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "format_1")
    private String format1;

    @Column(name = "date_from")
    private ZonedDateTime dateFrom;

    @Column(name = "date_thru")
    private ZonedDateTime dateThru;

    @ManyToOne
    @JoinColumn(name="idperiodType", referencedColumnName="idperiodtype")
    private PeriodType periodType;

    public UUID getIdPeriod() {
        return this.idPeriod;
    }

    public void setIdPeriod(UUID id) {
        this.idPeriod = id;
    }


    public String getDescription() {
        return description;
    }

    public AcademicPeriods description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public AcademicPeriods year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFormat1() {
        return format1;
    }

    public AcademicPeriods format1(String format1) {
        this.format1 = format1;
        return this;
    }

    public void setFormat1(String format1) {
        this.format1 = format1;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public AcademicPeriods dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateThru() {
        return dateThru;
    }

    public AcademicPeriods dateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
        return this;
    }

    public void setDateThru(ZonedDateTime dateThru) {
        this.dateThru = dateThru;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public AcademicPeriods periodType(PeriodType periodType) {
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
        AcademicPeriods academicPeriods = (AcademicPeriods) o;
        if (academicPeriods.idPeriod == null || this.idPeriod == null) {
            return false;
        }
        return Objects.equals(this.idPeriod, academicPeriods.idPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPeriod);
    }

    @Override
    public String toString() {
        return "AcademicPeriods{" +
            "idPeriod=" + this.idPeriod +
            ", description='" + getDescription() + "'" +
            ", year='" + getYear() + "'" +
            ", format1='" + getFormat1() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            '}';
    }
}
