package id.eara.repository.search;

import id.eara.domain.HostDataSource;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HostDataSource entity.
 */
public interface HostDataSourceSearchRepository extends ElasticsearchRepository<HostDataSource, UUID> {
}
