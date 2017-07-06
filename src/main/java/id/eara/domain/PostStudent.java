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
 * Class definition for Entity PostStudent.
 */

@Entity
@Table(name = "post_student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "poststudent")
public class PostStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "idparrol", columnDefinition = "BINARY(16)")
    private UUID idPartyRole;

    @Column(name = "idpoststudent")
    private String idpoststudent;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;

    public UUID getIdPartyRole() {
        return this.idPartyRole;
    }

    public void setIdPartyRole(UUID id) {
        this.idPartyRole = id;
    }


    public String getIdpoststudent() {
        return idpoststudent;
    }

    public PostStudent idpoststudent(String idpoststudent) {
        this.idpoststudent = idpoststudent;
        return this;
    }

    public void setIdpoststudent(String idpoststudent) {
        this.idpoststudent = idpoststudent;
    }

    public String getName() {
        return name;
    }

    public PostStudent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public PostStudent status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostStudent postStudent = (PostStudent) o;
        if (postStudent.idPartyRole == null || this.idPartyRole == null) {
            return false;
        }
        return Objects.equals(this.idPartyRole, postStudent.idPartyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.idPartyRole);
    }

    @Override
    public String toString() {
        return "PostStudent{" +
            "idPartyRole=" + this.idPartyRole +
            ", idpoststudent='" + getIdpoststudent() + "'" +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            '}';
    }
}
