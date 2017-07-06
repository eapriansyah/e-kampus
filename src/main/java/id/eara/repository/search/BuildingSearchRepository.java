package id.eara.repository.search;

import id.eara.domain.Building;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Building entity.
 */
public interface BuildingSearchRepository extends ElasticsearchRepository<Building, UUID> {
}
