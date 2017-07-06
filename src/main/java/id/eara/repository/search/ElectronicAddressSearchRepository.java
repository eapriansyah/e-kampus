package id.eara.repository.search;

import id.eara.domain.ElectronicAddress;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ElectronicAddress entity.
 */
public interface ElectronicAddressSearchRepository extends ElasticsearchRepository<ElectronicAddress, UUID> {
}
