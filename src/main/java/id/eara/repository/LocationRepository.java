package id.eara.repository;

import id.eara.domain.Location;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Location entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location,UUID> {


}
