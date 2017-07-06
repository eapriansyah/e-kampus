package id.eara.repository.search;

import id.eara.domain.RegularCourse;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RegularCourse entity.
 */
public interface RegularCourseSearchRepository extends ElasticsearchRepository<RegularCourse, UUID> {
}
