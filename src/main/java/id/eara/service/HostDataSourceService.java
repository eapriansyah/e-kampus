package id.eara.service;

import id.eara.domain.HostDataSource;
import id.eara.repository.HostDataSourceRepository;
import id.eara.repository.search.HostDataSourceSearchRepository;
import id.eara.service.dto.HostDataSourceDTO;
import id.eara.service.mapper.HostDataSourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing HostDataSource.
 * atiila consulting
 */

@Service
@Transactional
public class HostDataSourceService {

    private final Logger log = LoggerFactory.getLogger(HostDataSourceService.class);

    private final HostDataSourceRepository hostDataSourceRepository;

    private final HostDataSourceMapper hostDataSourceMapper;

    private final HostDataSourceSearchRepository hostDataSourceSearchRepository;

    public HostDataSourceService(HostDataSourceRepository hostDataSourceRepository, HostDataSourceMapper hostDataSourceMapper, HostDataSourceSearchRepository hostDataSourceSearchRepository) {
        this.hostDataSourceRepository = hostDataSourceRepository;
        this.hostDataSourceMapper = hostDataSourceMapper;
        this.hostDataSourceSearchRepository = hostDataSourceSearchRepository;
    }

    /**
     * Save a hostDataSource.
     *
     * @param hostDataSourceDTO the entity to save
     * @return the persisted entity
     */
    public HostDataSourceDTO save(HostDataSourceDTO hostDataSourceDTO) {
        log.debug("Request to save HostDataSource : {}", hostDataSourceDTO);
        HostDataSource hostDataSource = hostDataSourceMapper.toEntity(hostDataSourceDTO);
        hostDataSource = hostDataSourceRepository.save(hostDataSource);
        HostDataSourceDTO result = hostDataSourceMapper.toDto(hostDataSource);
        hostDataSourceSearchRepository.save(hostDataSource);
        return result;
    }

    /**
     *  Get all the hostDataSources.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HostDataSourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HostDataSources");
        return hostDataSourceRepository.findAll(pageable)
            .map(hostDataSourceMapper::toDto);
    }

    /**
     *  Get one hostDataSource by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public HostDataSourceDTO findOne(UUID id) {
        log.debug("Request to get HostDataSource : {}", id);
        HostDataSource hostDataSource = hostDataSourceRepository.findOne(id);
        return hostDataSourceMapper.toDto(hostDataSource);
    }

    /**
     *  Delete the  hostDataSource by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete HostDataSource : {}", id);
        hostDataSourceRepository.delete(id);
        hostDataSourceSearchRepository.delete(id);
    }

    /**
     * Search for the hostDataSource corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HostDataSourceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HostDataSources for query {}", query);
        Page<HostDataSource> result = hostDataSourceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(hostDataSourceMapper::toDto);
    }
}
