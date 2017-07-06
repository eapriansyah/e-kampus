package id.eara.repository.search;

import id.eara.domain.CourseLecture;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CourseLecture entity.
 */
public interface CourseLectureSearchRepository extends ElasticsearchRepository<CourseLecture, UUID> {
}
