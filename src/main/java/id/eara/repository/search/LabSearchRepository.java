package id.eara.repository.search;

import id.eara.domain.Lab;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Lab entity.
 */
public interface LabSearchRepository extends ElasticsearchRepository<Lab, UUID> {
}
