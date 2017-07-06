package id.eara.repository.search;

import id.eara.domain.TelecomunicationNumber;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TelecomunicationNumber entity.
 */
public interface TelecomunicationNumberSearchRepository extends ElasticsearchRepository<TelecomunicationNumber, UUID> {
}
