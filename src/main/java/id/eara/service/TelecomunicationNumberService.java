package id.eara.service;

import id.eara.domain.TelecomunicationNumber;
import id.eara.repository.TelecomunicationNumberRepository;
import id.eara.repository.search.TelecomunicationNumberSearchRepository;
import id.eara.service.dto.TelecomunicationNumberDTO;
import id.eara.service.mapper.TelecomunicationNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing TelecomunicationNumber.
 * atiila consulting
 */

@Service
@Transactional
public class TelecomunicationNumberService {

    private final Logger log = LoggerFactory.getLogger(TelecomunicationNumberService.class);

    private final TelecomunicationNumberRepository telecomunicationNumberRepository;

    private final TelecomunicationNumberMapper telecomunicationNumberMapper;

    private final TelecomunicationNumberSearchRepository telecomunicationNumberSearchRepository;

    public TelecomunicationNumberService(TelecomunicationNumberRepository telecomunicationNumberRepository, TelecomunicationNumberMapper telecomunicationNumberMapper, TelecomunicationNumberSearchRepository telecomunicationNumberSearchRepository) {
        this.telecomunicationNumberRepository = telecomunicationNumberRepository;
        this.telecomunicationNumberMapper = telecomunicationNumberMapper;
        this.telecomunicationNumberSearchRepository = telecomunicationNumberSearchRepository;
    }

    /**
     * Save a telecomunicationNumber.
     *
     * @param telecomunicationNumberDTO the entity to save
     * @return the persisted entity
     */
    public TelecomunicationNumberDTO save(TelecomunicationNumberDTO telecomunicationNumberDTO) {
        log.debug("Request to save TelecomunicationNumber : {}", telecomunicationNumberDTO);
        TelecomunicationNumber telecomunicationNumber = telecomunicationNumberMapper.toEntity(telecomunicationNumberDTO);
        telecomunicationNumber = telecomunicationNumberRepository.save(telecomunicationNumber);
        TelecomunicationNumberDTO result = telecomunicationNumberMapper.toDto(telecomunicationNumber);
        telecomunicationNumberSearchRepository.save(telecomunicationNumber);
        return result;
    }

    /**
     *  Get all the telecomunicationNumbers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TelecomunicationNumberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TelecomunicationNumbers");
        return telecomunicationNumberRepository.findAll(pageable)
            .map(telecomunicationNumberMapper::toDto);
    }

    /**
     *  Get one telecomunicationNumber by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TelecomunicationNumberDTO findOne(UUID id) {
        log.debug("Request to get TelecomunicationNumber : {}", id);
        TelecomunicationNumber telecomunicationNumber = telecomunicationNumberRepository.findOne(id);
        return telecomunicationNumberMapper.toDto(telecomunicationNumber);
    }

    /**
     *  Delete the  telecomunicationNumber by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete TelecomunicationNumber : {}", id);
        telecomunicationNumberRepository.delete(id);
        telecomunicationNumberSearchRepository.delete(id);
    }

    /**
     * Search for the telecomunicationNumber corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TelecomunicationNumberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TelecomunicationNumbers for query {}", query);
        Page<TelecomunicationNumber> result = telecomunicationNumberSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(telecomunicationNumberMapper::toDto);
    }
}
