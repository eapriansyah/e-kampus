package id.eara.repository.search;

import id.eara.domain.PostStudent;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PostStudent entity.
 */
public interface PostStudentSearchRepository extends ElasticsearchRepository<PostStudent, UUID> {
}
