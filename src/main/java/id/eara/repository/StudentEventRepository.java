package id.eara.repository;

import id.eara.domain.StudentEvent;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudentEvent entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentEventRepository extends JpaRepository<StudentEvent,Long> {


}
