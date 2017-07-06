package id.eara.repository;

import id.eara.domain.Person;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Person entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person,UUID> {


}
