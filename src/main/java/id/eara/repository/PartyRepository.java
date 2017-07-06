package id.eara.repository;

import id.eara.domain.Party;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Party entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PartyRepository extends JpaRepository<Party,UUID> {


}
