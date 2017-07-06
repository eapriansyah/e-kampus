package id.eara.repository.search;

import id.eara.domain.StudentCoursePeriod;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentCoursePeriod entity.
 */
public interface StudentCoursePeriodSearchRepository extends ElasticsearchRepository<StudentCoursePeriod, UUID> {
}
