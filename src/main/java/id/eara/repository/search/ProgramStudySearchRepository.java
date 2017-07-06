package id.eara.repository.search;

import id.eara.domain.ProgramStudy;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProgramStudy entity.
 */
public interface ProgramStudySearchRepository extends ElasticsearchRepository<ProgramStudy, UUID> {
}
