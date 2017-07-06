package id.eara.repository.search;

import id.eara.domain.AcademicPeriods;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AcademicPeriods entity.
 */
public interface AcademicPeriodsSearchRepository extends ElasticsearchRepository<AcademicPeriods, UUID> {
}
