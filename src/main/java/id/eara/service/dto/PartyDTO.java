package id.eara.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;



/**
 * A DTO for the Party entity.
 * atiila consulting
 */

public class PartyDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private UUID idParty;

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

        PartyDTO partyDTO = (PartyDTO) o;
        if(partyDTO.getIdParty() == null || getIdParty() == null) {
            return false;
        }
        return Objects.equals(getIdParty(), partyDTO.getIdParty());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdParty());
    }

    @Override
    public String toString() {
        return "PartyDTO{" +
            "id=" + getIdParty() +
            ", name='" + getName() + "'" +
            "}";
    }
}
