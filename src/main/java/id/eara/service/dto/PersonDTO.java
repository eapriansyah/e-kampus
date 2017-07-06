package id.eara.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Person entity.
 * atiila consulting
 */

public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idParty;

    private String firstName;

    private String lastName;

    private String pob;

    private String bloodType;

    private String gender;

    private LocalDate dob;

    public UUID getIdParty() {
        return this.idParty;
    }

    public void setIdParty(UUID id) {
        this.idParty = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if(personDTO.getIdParty() == null || getIdParty() == null) {
            return false;
        }
        return Objects.equals(getIdParty(), personDTO.getIdParty());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdParty());
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getIdParty() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", pob='" + getPob() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", gender='" + getGender() + "'" +
            ", dob='" + getDob() + "'" +
            "}";
    }
}
