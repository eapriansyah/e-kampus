package id.eara.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.springframework.data.elasticsearch.annotations.Document;


import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

import javax.persistence.*;
import java.io.Serializable;import java.util.Objects;

/**
 * atiila consulting
 * Class definition for Entity Organization.
 */

@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparty", columnDefinition = "BINARY(16)")
    private UUID idParty;

    @Column(name = "name")
    private String name;

    public UUID getIdParty() {
        return this.idParty;
    }

    public void setIdParty(UUID id) {
        this.idParty = id;
    }


    public String getName() {
        return name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization organization = (Organization) o;
        if (organization.idParty == null || this.idParty == null) {
            return false;
        }
        return Objects.equals(this.idParty, organization.idParty);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idParty);
    }

    @Override
    public String toString() {
        return "Organization{" +
            "idParty=" + this.idParty +
            ", name='" + getName() + "'" +
            '}';
    }
}
