package id.eara.repository.search;

import id.eara.domain.StudyPath;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StudyPath entity.
 */
public interface StudyPathSearchRepository extends ElasticsearchRepository<StudyPath, Long> {
}
