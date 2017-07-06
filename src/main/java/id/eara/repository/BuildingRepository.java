package id.eara.repository;

import id.eara.domain.Building;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Building entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface BuildingRepository extends JpaRepository<Building,UUID> {


}
