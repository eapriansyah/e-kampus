package id.eara.repository.search;

import id.eara.domain.EducationType;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EducationType entity.
 */
public interface EducationTypeSearchRepository extends ElasticsearchRepository<EducationType, Long> {
}
