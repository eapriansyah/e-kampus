package id.eara.repository.search;

import id.eara.domain.ContactMechanism;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContactMechanism entity.
 */
public interface ContactMechanismSearchRepository extends ElasticsearchRepository<ContactMechanism, UUID> {
}
