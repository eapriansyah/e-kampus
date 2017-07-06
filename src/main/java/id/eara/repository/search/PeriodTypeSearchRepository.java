package id.eara.repository.search;

import id.eara.domain.PeriodType;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PeriodType entity.
 */
public interface PeriodTypeSearchRepository extends ElasticsearchRepository<PeriodType, Long> {
}
