package id.eara.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the PersonalData entity.
 * atiila consulting
 */

public class PersonalDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idPersonalData;

    private String nisn;

    private String motherName;

    private String fatherName;

    private LocalDate fatherDob;

    private LocalDate motherDob;

    private Integer hasPaud;

    private Integer hasTk;

    private UUID personId;

    private String personFirstName;

    private Long motherReligionId;

    private String motherReligionDescription;

    private Long fatherReligionId;

    private String fatherReligionDescription;

    private Long fatherEducationId;

    private String fatherEducationDescription;

    private Long motherEducationId;

    private String motherEducationDescription;

    private Long fatherWorkTypeId;

    private String fatherWorkTypeDescription;

    private Long motherWorkTypeId;

    private String motherWorkTypeDescription;

    public UUID getIdPersonalData() {
        return this.idPersonalData;
    }

    public void setIdPersonalData(UUID id) {
        this.idPersonalData = id;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public LocalDate getFatherDob() {
        return fatherDob;
    }

    public void setFatherDob(LocalDate fatherDob) {
        this.fatherDob = fatherDob;
    }

    public LocalDate getMotherDob() {
        return motherDob;
    }

    public void setMotherDob(LocalDate motherDob) {
        this.motherDob = motherDob;
    }

    public Integer getHasPaud() {
        return hasPaud;
    }

    public void setHasPaud(Integer hasPaud) {
        this.hasPaud = hasPaud;
    }

    public Integer getHasTk() {
        return hasTk;
    }

    public void setHasTk(Integer hasTk) {
        this.hasTk = hasTk;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public Long getMotherReligionId() {
        return motherReligionId;
    }

    public void setMotherReligionId(Long religionTypeId) {
        this.motherReligionId = religionTypeId;
    }

    public String getMotherReligionDescription() {
        return motherReligionDescription;
    }

    public void setMotherReligionDescription(String religionTypeDescription) {
        this.motherReligionDescription = religionTypeDescription;
    }

    public Long getFatherReligionId() {
        return fatherReligionId;
    }

    public void setFatherReligionId(Long religionTypeId) {
        this.fatherReligionId = religionTypeId;
    }

    public String getFatherReligionDescription() {
        return fatherReligionDescription;
    }

    public void setFatherReligionDescription(String religionTypeDescription) {
        this.fatherReligionDescription = religionTypeDescription;
    }

    public Long getFatherEducationId() {
        return fatherEducationId;
    }

    public void setFatherEducationId(Long educationTypeId) {
        this.fatherEducationId = educationTypeId;
    }

    public String getFatherEducationDescription() {
        return fatherEducationDescription;
    }

    public void setFatherEducationDescription(String educationTypeDescription) {
        this.fatherEducationDescription = educationTypeDescription;
    }

    public Long getMotherEducationId() {
        return motherEducationId;
    }

    public void setMotherEducationId(Long educationTypeId) {
        this.motherEducationId = educationTypeId;
    }

    public String getMotherEducationDescription() {
        return motherEducationDescription;
    }

    public void setMotherEducationDescription(String educationTypeDescription) {
        this.motherEducationDescription = educationTypeDescription;
    }

    public Long getFatherWorkTypeId() {
        return fatherWorkTypeId;
    }

    public void setFatherWorkTypeId(Long workTypeId) {
        this.fatherWorkTypeId = workTypeId;
    }

    public String getFatherWorkTypeDescription() {
        return fatherWorkTypeDescription;
    }

    public void setFatherWorkTypeDescription(String workTypeDescription) {
        this.fatherWorkTypeDescription = workTypeDescription;
    }

    public Long getMotherWorkTypeId() {
        return motherWorkTypeId;
    }

    public void setMotherWorkTypeId(Long workTypeId) {
        this.motherWorkTypeId = workTypeId;
    }

    public String getMotherWorkTypeDescription() {
        return motherWorkTypeDescription;
    }

    public void setMotherWorkTypeDescription(String workTypeDescription) {
        this.motherWorkTypeDescription = workTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonalDataDTO personalDataDTO = (PersonalDataDTO) o;
        if(personalDataDTO.getIdPersonalData() == null || getIdPersonalData() == null) {
            return false;
        }
        return Objects.equals(getIdPersonalData(), personalDataDTO.getIdPersonalData());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdPersonalData());
    }

    @Override
    public String toString() {
        return "PersonalDataDTO{" +
            "id=" + getIdPersonalData() +
            ", nisn='" + getNisn() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", fatherDob='" + getFatherDob() + "'" +
            ", motherDob='" + getMotherDob() + "'" +
            ", hasPaud='" + getHasPaud() + "'" +
            ", hasTk='" + getHasTk() + "'" +
            "}";
    }
}
