package id.eara.repository.search;

import id.eara.domain.ReligionType;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReligionType entity.
 */
public interface ReligionTypeSearchRepository extends ElasticsearchRepository<ReligionType, Long> {
}
