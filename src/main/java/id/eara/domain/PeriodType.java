package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity PeriodType.
 */

@Entity
@Table(name = "period_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "periodtype")
public class PeriodType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idperiodtype")
    private Long idPeriodType;

    @Column(name = "description")
    private String description;

    @Column(name = "from_day")
    private Integer fromDay;

    @Column(name = "from_month")
    private Integer fromMonth;

    @Column(name = "thru_day")
    private Integer thruDay;

    @Column(name = "thru_month")
    private Integer thruMonth;

    @Column(name = "from_add_year")
    private Integer fromAddYear;

    @Column(name = "thru_add_year")
    private Integer thruAddYear;

    @Column(name = "jhi_sequence")
    private Integer sequence;

    @Column(name = "id_parent")
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

    public PeriodType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFromDay() {
        return fromDay;
    }

    public PeriodType fromDay(Integer fromDay) {
        this.fromDay = fromDay;
        return this;
    }

    public void setFromDay(Integer fromDay) {
        this.fromDay = fromDay;
    }

    public Integer getFromMonth() {
        return fromMonth;
    }

    public PeriodType fromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
        return this;
    }

    public void setFromMonth(Integer fromMonth) {
        this.fromMonth = fromMonth;
    }

    public Integer getThruDay() {
        return thruDay;
    }

    public PeriodType thruDay(Integer thruDay) {
        this.thruDay = thruDay;
        return this;
    }

    public void setThruDay(Integer thruDay) {
        this.thruDay = thruDay;
    }

    public Integer getThruMonth() {
        return thruMonth;
    }

    public PeriodType thruMonth(Integer thruMonth) {
        this.thruMonth = thruMonth;
        return this;
    }

    public void setThruMonth(Integer thruMonth) {
        this.thruMonth = thruMonth;
    }

    public Integer getFromAddYear() {
        return fromAddYear;
    }

    public PeriodType fromAddYear(Integer fromAddYear) {
        this.fromAddYear = fromAddYear;
        return this;
    }

    public void setFromAddYear(Integer fromAddYear) {
        this.fromAddYear = fromAddYear;
    }

    public Integer getThruAddYear() {
        return thruAddYear;
    }

    public PeriodType thruAddYear(Integer thruAddYear) {
        this.thruAddYear = thruAddYear;
        return this;
    }

    public void setThruAddYear(Integer thruAddYear) {
        this.thruAddYear = thruAddYear;
    }

    public Integer getSequence() {
        return sequence;
    }

    public PeriodType sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIdParent() {
        return idParent;
    }

    public PeriodType idParent(Integer idParent) {
        this.idParent = idParent;
        return this;
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
        PeriodType periodType = (PeriodType) o;
        if (periodType.idPeriodType == null || this.idPeriodType == null) {
            return false;
        }
        return Objects.equals(this.idPeriodType, periodType.idPeriodType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPeriodType);
    }

    @Override
    public String toString() {
        return "PeriodType{" +
            "idPeriodType=" + this.idPeriodType +
            ", description='" + getDescription() + "'" +
            ", fromDay='" + getFromDay() + "'" +
            ", fromMonth='" + getFromMonth() + "'" +
            ", thruDay='" + getThruDay() + "'" +
            ", thruMonth='" + getThruMonth() + "'" +
            ", fromAddYear='" + getFromAddYear() + "'" +
            ", thruAddYear='" + getThruAddYear() + "'" +
            ", sequence='" + getSequence() + "'" +
            ", idParent='" + getIdParent() + "'" +
            '}';
    }
}
