package id.eara.repository.search;

import id.eara.domain.Internal;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Internal entity.
 */
public interface InternalSearchRepository extends ElasticsearchRepository<Internal, UUID> {
}
