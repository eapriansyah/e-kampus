package id.eara.repository;

import id.eara.domain.PurposeType;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PurposeType entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PurposeTypeRepository extends JpaRepository<PurposeType,Long> {


}
