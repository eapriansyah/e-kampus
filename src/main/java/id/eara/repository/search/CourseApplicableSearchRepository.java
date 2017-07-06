package id.eara.repository.search;

import id.eara.domain.CourseApplicable;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourseApplicable entity.
 */
public interface CourseApplicableSearchRepository extends ElasticsearchRepository<CourseApplicable, UUID> {
}
