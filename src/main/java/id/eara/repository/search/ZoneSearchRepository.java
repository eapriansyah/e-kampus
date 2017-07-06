package id.eara.repository.search;

import id.eara.domain.Zone;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Zone entity.
 */
public interface ZoneSearchRepository extends ElasticsearchRepository<Zone, UUID> {
}
