package id.eara.service;

import id.eara.domain.Party;
import id.eara.repository.PartyRepository;
import id.eara.repository.search.PartySearchRepository;
import id.eara.service.dto.PartyDTO;
import id.eara.service.mapper.PartyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Party.
 * atiila consulting
 */

@Service
@Transactional
public class PartyService {

    private final Logger log = LoggerFactory.getLogger(PartyService.class);

    private final PartyRepository partyRepository;

    private final PartyMapper partyMapper;

    private final PartySearchRepository partySearchRepository;

    public PartyService(PartyRepository partyRepository, PartyMapper partyMapper, PartySearchRepository partySearchRepository) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.partySearchRepository = partySearchRepository;
    }

    /**
     * Save a party.
     *
     * @param partyDTO the entity to save
     * @return the persisted entity
     */
    public PartyDTO save(PartyDTO partyDTO) {
        log.debug("Request to save Party : {}", partyDTO);
        Party party = partyMapper.toEntity(partyDTO);
        party = partyRepository.save(party);
        PartyDTO result = partyMapper.toDto(party);
        partySearchRepository.save(party);
        return result;
    }

    /**
     *  Get all the parties.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PartyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parties");
        return partyRepository.findAll(pageable)
            .map(partyMapper::toDto);
    }

    /**
     *  Get one party by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PartyDTO findOne(UUID id) {
        log.debug("Request to get Party : {}", id);
        Party party = partyRepository.findOne(id);
        return partyMapper.toDto(party);
    }

    /**
     *  Delete the  party by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Party : {}", id);
        partyRepository.delete(id);
        partySearchRepository.delete(id);
    }

    /**
     * Search for the party corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PartyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Parties for query {}", query);
        Page<Party> result = partySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(partyMapper::toDto);
    }
}
