package id.eara.repository.search;

import id.eara.domain.StudentPeriods;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentPeriods entity.
 */
public interface StudentPeriodsSearchRepository extends ElasticsearchRepository<StudentPeriods, UUID> {
}
