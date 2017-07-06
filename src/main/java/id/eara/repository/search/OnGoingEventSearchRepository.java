package id.eara.repository.search;

import id.eara.domain.OnGoingEvent;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OnGoingEvent entity.
 */
public interface OnGoingEventSearchRepository extends ElasticsearchRepository<OnGoingEvent, UUID> {
}
