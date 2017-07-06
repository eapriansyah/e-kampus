package id.eara.repository.search;

import id.eara.domain.ClassStudy;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ClassStudy entity.
 */
public interface ClassStudySearchRepository extends ElasticsearchRepository<ClassStudy, UUID> {
}
