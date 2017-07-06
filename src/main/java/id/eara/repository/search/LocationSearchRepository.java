package id.eara.repository.search;

import id.eara.domain.Location;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Location entity.
 */
public interface LocationSearchRepository extends ElasticsearchRepository<Location, UUID> {
}
