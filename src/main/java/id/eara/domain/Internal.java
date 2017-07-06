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
 * Class definition for Entity Internal.
 */

@Entity
@Table(name = "internal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "internal")
public class Internal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @Column(name = "id_internal")
    private String idInternal;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "id_role_type")
    private Integer idRoleType;

    @Column(name = "id_status_type")
    private Integer idStatusType;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdInternal() {
        return idInternal;
    }

    public Internal idInternal(String idInternal) {
        this.idInternal = idInternal;
        return this;
    }

    public void setIdInternal(String idInternal) {
        this.idInternal = idInternal;
    }

    public String getName() {
        return name;
    }

    public Internal name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Internal description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdRoleType() {
        return idRoleType;
    }

    public Internal idRoleType(Integer idRoleType) {
        this.idRoleType = idRoleType;
        return this;
    }

    public void setIdRoleType(Integer idRoleType) {
        this.idRoleType = idRoleType;
    }

    public Integer getIdStatusType() {
        return idStatusType;
    }

    public Internal idStatusType(Integer idStatusType) {
        this.idStatusType = idStatusType;
        return this;
    }

    public void setIdStatusType(Integer idStatusType) {
        this.idStatusType = idStatusType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Internal internal = (Internal) o;
        if (internal.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, internal.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "Internal{" +
            "idPartyRole=" + this.idPartyRole +
            ", idInternal='" + getIdInternal() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idRoleType='" + getIdRoleType() + "'" +
            ", idStatusType='" + getIdStatusType() + "'" +
            '}';
    }
}
