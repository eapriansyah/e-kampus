package id.eara.repository.search;

import id.eara.domain.PersonalData;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PersonalData entity.
 */
public interface PersonalDataSearchRepository extends ElasticsearchRepository<PersonalData, UUID> {
}
