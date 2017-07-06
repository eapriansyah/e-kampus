package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


/**
 * A DTO for the PeriodType entity.
 * atiila consulting
 */

public class PeriodTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long idPeriodType;

    private String description;

    private Integer fromDay;

    private Integer fromMonth;

    private Integer thruDay;

    private Integer thruMonth;

    private Integer fromAddYear;

    private Integer thruAddYear;

    private Integer sequence;

    private Integer idParent;

    public Long getIdPeriodType() {
        return this.idPeriodType;
    }

    public void setIdPeriodType(Long id) {
        this.idPeriodType = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFromDay() {
        return fromDay;
    }

    public void setFromDay(Integer fromDay) {
        this.fromDay = fromDay;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    public Integer getThruDay() {
        return thruDay;
    }

    public void setThruDay(Integer thruDay) {
        this.thruDay = thruDay;
    }

    public Integer getThruMonth() {
        return thruMonth;
    }

    public void setThruMonth(Integer thruMonth) {
        this.thruMonth = thruMonth;
    }

    public Integer getFromAddYear() {
        return fromAddYear;
    }

    public void setFromAddYear(Integer fromAddYear) {
        this.fromAddYear = fromAddYear;
    }

    public Integer getThruAddYear() {
        return thruAddYear;
    }

    public void setThruAddYear(Integer thruAddYear) {
        this.thruAddYear = thruAddYear;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIdParent() {
        return idParent;
    }

    public void setIdParent(Integer idParent) {
        this.idParent = idParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PeriodTypeDTO periodTypeDTO = (PeriodTypeDTO) o;
        if(periodTypeDTO.getIdPeriodType() == null || getIdPeriodType() == null) {
            return false;
        }
        return Objects.equals(getIdPeriodType(), periodTypeDTO.getIdPeriodType());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPeriodType());
    }

    @Override
    public String toString() {
        return "PeriodTypeDTO{" +
            "id=" + getIdPeriodType() +
            ", description='" + getDescription() + "'" +
            ", fromDay='" + getFromDay() + "'" +
            ", fromMonth='" + getFromMonth() + "'" +
            ", thruDay='" + getThruDay() + "'" +
            ", thruMonth='" + getThruMonth() + "'" +
            ", fromAddYear='" + getFromAddYear() + "'" +
            ", thruAddYear='" + getThruAddYear() + "'" +
            ", sequence='" + getSequence() + "'" +
            ", idParent='" + getIdParent() + "'" +
            "}";
    }
}
