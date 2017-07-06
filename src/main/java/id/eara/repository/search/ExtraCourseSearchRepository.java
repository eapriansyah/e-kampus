package id.eara.repository.search;

import id.eara.domain.ExtraCourse;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ExtraCourse entity.
 */
public interface ExtraCourseSearchRepository extends ElasticsearchRepository<ExtraCourse, UUID> {
}
