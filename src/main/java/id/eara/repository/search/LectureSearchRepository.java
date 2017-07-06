package id.eara.repository.search;

import id.eara.domain.Lecture;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Lecture entity.
 */
public interface LectureSearchRepository extends ElasticsearchRepository<Lecture, UUID> {
}
