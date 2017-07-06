package id.eara.repository.search;

import id.eara.domain.StudentPeriodData;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentPeriodData entity.
 */
public interface StudentPeriodDataSearchRepository extends ElasticsearchRepository<StudentPeriodData, UUID> {
}
