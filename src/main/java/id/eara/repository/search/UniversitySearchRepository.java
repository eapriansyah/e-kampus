package id.eara.repository.search;

import id.eara.domain.University;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the University entity.
 */
public interface UniversitySearchRepository extends ElasticsearchRepository<University, UUID> {
}
