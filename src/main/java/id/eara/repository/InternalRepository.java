package id.eara.repository;

import id.eara.domain.Internal;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Internal entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface InternalRepository extends JpaRepository<Internal,UUID> {


}
