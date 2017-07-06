package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity PersonalData.
 */

@Entity
@Table(name = "personal_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "personaldata")
public class PersonalData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idpersonaldata", columnDefinition = "BINARY(16)")
    private UUID idPersonalData;

    @Column(name = "nisn")
    private String nisn;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "father_dob")
    private LocalDate fatherDob;

    @Column(name = "mother_dob")
    private LocalDate motherDob;

    @Column(name = "has_paud")
    private Integer hasPaud;

    @Column(name = "has_tk")
    private Integer hasTk;

    @ManyToOne
    @JoinColumn(name="idparty", referencedColumnName="idparty")
    private Person person;

    @ManyToOne
    @JoinColumn(name="idmotherreligiontype", referencedColumnName="idreligiontype")
    private ReligionType motherReligion;

    @ManyToOne
    @JoinColumn(name="idfatherreligiontype", referencedColumnName="idreligiontype")
    private ReligionType fatherReligion;

    @ManyToOne
    @JoinColumn(name="idfathereducationtype", referencedColumnName="ideducationtype")
    private EducationType fatherEducation;

    @ManyToOne
    @JoinColumn(name="idmothereducationtype", referencedColumnName="ideducationtype")
    private EducationType motherEducation;

    @ManyToOne
    @JoinColumn(name="idfatherworktype", referencedColumnName="idworktype")
    private WorkType fatherWorkType;

    @ManyToOne
    @JoinColumn(name="idmotherworktype", referencedColumnName="idworktype")
    private WorkType motherWorkType;

    public UUID getIdPersonalData() {
        return this.idPersonalData;
    }

    public void setIdPersonalData(UUID id) {
        this.idPersonalData = id;
    }


    public String getNisn() {
        return nisn;
    }

    public PersonalData nisn(String nisn) {
        this.nisn = nisn;
        return this;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getMotherName() {
        return motherName;
    }

    public PersonalData motherName(String motherName) {
        this.motherName = motherName;
        return this;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public PersonalData fatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public LocalDate getFatherDob() {
        return fatherDob;
    }

    public PersonalData fatherDob(LocalDate fatherDob) {
        this.fatherDob = fatherDob;
        return this;
    }

    public void setFatherDob(LocalDate fatherDob) {
        this.fatherDob = fatherDob;
    }

    public LocalDate getMotherDob() {
        return motherDob;
    }

    public PersonalData motherDob(LocalDate motherDob) {
        this.motherDob = motherDob;
        return this;
    }

    public void setMotherDob(LocalDate motherDob) {
        this.motherDob = motherDob;
    }

    public Integer getHasPaud() {
        return hasPaud;
    }

    public PersonalData hasPaud(Integer hasPaud) {
        this.hasPaud = hasPaud;
        return this;
    }

    public void setHasPaud(Integer hasPaud) {
        this.hasPaud = hasPaud;
    }

    public Integer getHasTk() {
        return hasTk;
    }

    public PersonalData hasTk(Integer hasTk) {
        this.hasTk = hasTk;
        return this;
    }

    public void setHasTk(Integer hasTk) {
        this.hasTk = hasTk;
    }

    public Person getPerson() {
        return person;
    }

    public PersonalData person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ReligionType getMotherReligion() {
        return motherReligion;
    }

    public PersonalData motherReligion(ReligionType religionType) {
        this.motherReligion = religionType;
        return this;
    }

    public void setMotherReligion(ReligionType religionType) {
        this.motherReligion = religionType;
    }

    public ReligionType getFatherReligion() {
        return fatherReligion;
    }

    public PersonalData fatherReligion(ReligionType religionType) {
        this.fatherReligion = religionType;
        return this;
    }

    public void setFatherReligion(ReligionType religionType) {
        this.fatherReligion = religionType;
    }

    public EducationType getFatherEducation() {
        return fatherEducation;
    }

    public PersonalData fatherEducation(EducationType educationType) {
        this.fatherEducation = educationType;
        return this;
    }

    public void setFatherEducation(EducationType educationType) {
        this.fatherEducation = educationType;
    }

    public EducationType getMotherEducation() {
        return motherEducation;
    }

    public PersonalData motherEducation(EducationType educationType) {
        this.motherEducation = educationType;
        return this;
    }

    public void setMotherEducation(EducationType educationType) {
        this.motherEducation = educationType;
    }

    public WorkType getFatherWorkType() {
        return fatherWorkType;
    }

    public PersonalData fatherWorkType(WorkType workType) {
        this.fatherWorkType = workType;
        return this;
    }

    public void setFatherWorkType(WorkType workType) {
        this.fatherWorkType = workType;
    }

    public WorkType getMotherWorkType() {
        return motherWorkType;
    }

    public PersonalData motherWorkType(WorkType workType) {
        this.motherWorkType = workType;
        return this;
    }

    public void setMotherWorkType(WorkType workType) {
        this.motherWorkType = workType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonalData personalData = (PersonalData) o;
        if (personalData.idPersonalData == null || this.idPersonalData == null) {
            return false;
        }
        return Objects.equals(this.idPersonalData, personalData.idPersonalData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPersonalData);
    }

    @Override
    public String toString() {
        return "PersonalData{" +
            "idPersonalData=" + this.idPersonalData +
            ", nisn='" + getNisn() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", fatherDob='" + getFatherDob() + "'" +
            ", motherDob='" + getMotherDob() + "'" +
            ", hasPaud='" + getHasPaud() + "'" +
            ", hasTk='" + getHasTk() + "'" +
            '}';
    }
}
