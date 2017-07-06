package id.eara.repository.search;

import id.eara.domain.StudentEvent;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentEvent entity.
 */
public interface StudentEventSearchRepository extends ElasticsearchRepository<StudentEvent, Long> {
}
