package id.eara.repository;

import id.eara.domain.ReligionType;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReligionType entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ReligionTypeRepository extends JpaRepository<ReligionType,Long> {


}
