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
 * Class definition for Entity Person.
 */

@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparty", columnDefinition = "BINARY(16)")
    private UUID idParty;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pob")
    private String pob;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
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

    public Person firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Person lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPob() {
        return pob;
    }

    public Person pob(String pob) {
        this.pob = pob;
        return this;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Person bloodType(String bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public Person gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Person dob(LocalDate dob) {
        this.dob = dob;
        return this;
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
        Person person = (Person) o;
        if (person.idParty == null || this.idParty == null) {
            return false;
        }
        return Objects.equals(this.idParty, person.idParty);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idParty);
    }

    @Override
    public String toString() {
        return "Person{" +
            "idParty=" + this.idParty +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", pob='" + getPob() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", gender='" + getGender() + "'" +
            ", dob='" + getDob() + "'" +
            '}';
    }
}
