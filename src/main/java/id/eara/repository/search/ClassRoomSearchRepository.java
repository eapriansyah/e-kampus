package id.eara.repository.search;

import id.eara.domain.ClassRoom;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ClassRoom entity.
 */
public interface ClassRoomSearchRepository extends ElasticsearchRepository<ClassRoom, UUID> {
}
