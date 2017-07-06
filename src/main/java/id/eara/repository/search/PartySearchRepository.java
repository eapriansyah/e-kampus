package id.eara.repository.search;

import id.eara.domain.Party;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Party entity.
 */
public interface PartySearchRepository extends ElasticsearchRepository<Party, UUID> {
}
