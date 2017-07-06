package id.eara.repository.search;

import id.eara.domain.PostalAddress;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PostalAddress entity.
 */
public interface PostalAddressSearchRepository extends ElasticsearchRepository<PostalAddress, UUID> {
}
