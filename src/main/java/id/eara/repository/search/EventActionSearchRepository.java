package id.eara.repository.search;

import id.eara.domain.EventAction;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EventAction entity.
 */
public interface EventActionSearchRepository extends ElasticsearchRepository<EventAction, Long> {
}
