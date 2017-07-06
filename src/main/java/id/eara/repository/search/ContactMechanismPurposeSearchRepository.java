package id.eara.repository.search;

import id.eara.domain.ContactMechanismPurpose;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContactMechanismPurpose entity.
 */
public interface ContactMechanismPurposeSearchRepository extends ElasticsearchRepository<ContactMechanismPurpose, UUID> {
}
