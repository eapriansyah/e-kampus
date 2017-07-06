package id.eara.repository.search;

import id.eara.domain.WorkType;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkType entity.
 */
public interface WorkTypeSearchRepository extends ElasticsearchRepository<WorkType, Long> {
}
