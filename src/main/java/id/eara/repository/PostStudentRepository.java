package id.eara.repository;

import id.eara.domain.PostStudent;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PostStudent entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PostStudentRepository extends JpaRepository<PostStudent,UUID> {


}
