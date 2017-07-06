package id.eara.repository.search;

import id.eara.domain.PreStudent;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PreStudent entity.
 */
public interface PreStudentSearchRepository extends ElasticsearchRepository<PreStudent, UUID> {
}
