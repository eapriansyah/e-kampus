package id.eara.repository.search;

import id.eara.domain.StudentCourseScore;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudentCourseScore entity.
 */
public interface StudentCourseScoreSearchRepository extends ElasticsearchRepository<StudentCourseScore, UUID> {
}
