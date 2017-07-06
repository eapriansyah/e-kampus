package id.eara.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the AcademicPeriods entity.
 * atiila consulting
 */

public class AcademicPeriodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPeriod;

    private String description;

    private Integer year;

    private String format1;

    private ZonedDateTime dateFrom;

    private ZonedDateTime dateThru;

    private Long periodTypeId;

    private String periodTypeDescription;

    public UUID getIdPeriod() {
        return this.idPeriod;
    }

    public void setIdPeriod(UUID id) {
        this.idPeriod = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFormat1() {
        return format1;
    }

    public void setFormat1(String format1) {
        this.format1 = format1;
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

        AcademicPeriodsDTO academicPeriodsDTO = (AcademicPeriodsDTO) o;
        if(academicPeriodsDTO.getIdPeriod() == null || getIdPeriod() == null) {
            return false;
        }
        return Objects.equals(getIdPeriod(), academicPeriodsDTO.getIdPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPeriod());
    }

    @Override
    public String toString() {
        return "AcademicPeriodsDTO{" +
            "id=" + getIdPeriod() +
            ", description='" + getDescription() + "'" +
            ", year='" + getYear() + "'" +
            ", format1='" + getFormat1() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateThru='" + getDateThru() + "'" +
            "}";
    }
}
