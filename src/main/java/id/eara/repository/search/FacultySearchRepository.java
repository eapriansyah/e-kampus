package id.eara.repository.search;

import id.eara.domain.Faculty;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Faculty entity.
 */
public interface FacultySearchRepository extends ElasticsearchRepository<Faculty, UUID> {
}
