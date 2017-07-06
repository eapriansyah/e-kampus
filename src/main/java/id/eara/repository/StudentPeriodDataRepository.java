package id.eara.repository;

import id.eara.domain.StudentPeriodData;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudentPeriodData entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentPeriodDataRepository extends JpaRepository<StudentPeriodData,UUID> {


}
