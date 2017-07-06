package id.eara.repository;

import id.eara.domain.TelecomunicationNumber;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TelecomunicationNumber entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface TelecomunicationNumberRepository extends JpaRepository<TelecomunicationNumber,UUID> {


}
