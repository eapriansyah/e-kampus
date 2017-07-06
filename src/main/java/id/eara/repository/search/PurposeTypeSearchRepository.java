package id.eara.repository.search;

import id.eara.domain.PurposeType;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurposeType entity.
 */
public interface PurposeTypeSearchRepository extends ElasticsearchRepository<PurposeType, Long> {
}
