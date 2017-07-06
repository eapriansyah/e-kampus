package id.eara.repository.search;

import id.eara.domain.Degree;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Degree entity.
 */
public interface DegreeSearchRepository extends ElasticsearchRepository<Degree, Long> {
}
