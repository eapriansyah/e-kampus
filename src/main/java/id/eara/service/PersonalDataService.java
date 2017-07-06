package id.eara.service;

import id.eara.domain.PersonalData;
import id.eara.repository.PersonalDataRepository;
import id.eara.repository.search.PersonalDataSearchRepository;
import id.eara.service.dto.PersonalDataDTO;
import id.eara.service.mapper.PersonalDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing PersonalData.
 * atiila consulting
 */

@Service
@Transactional
public class PersonalDataService {

    private final Logger log = LoggerFactory.getLogger(PersonalDataService.class);

    private final PersonalDataRepository personalDataRepository;

    private final PersonalDataMapper personalDataMapper;

    private final PersonalDataSearchRepository personalDataSearchRepository;

    public PersonalDataService(PersonalDataRepository personalDataRepository, PersonalDataMapper personalDataMapper, PersonalDataSearchRepository personalDataSearchRepository) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
        this.personalDataSearchRepository = personalDataSearchRepository;
    }

    /**
     * Save a personalData.
     *
     * @param personalDataDTO the entity to save
     * @return the persisted entity
     */
    public PersonalDataDTO save(PersonalDataDTO personalDataDTO) {
        log.debug("Request to save PersonalData : {}", personalDataDTO);
        PersonalData personalData = personalDataMapper.toEntity(personalDataDTO);
        personalData = personalDataRepository.save(personalData);
        PersonalDataDTO result = personalDataMapper.toDto(personalData);
        personalDataSearchRepository.save(personalData);
        return result;
    }

    /**
     *  Get all the personalData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonalDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalData");
        return personalDataRepository.findAll(pageable)
            .map(personalDataMapper::toDto);
    }

    /**
     *  Get one personalData by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PersonalDataDTO findOne(UUID id) {
        log.debug("Request to get PersonalData : {}", id);
        PersonalData personalData = personalDataRepository.findOne(id);
        return personalDataMapper.toDto(personalData);
    }

    /**
     *  Delete the  personalData by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete PersonalData : {}", id);
        personalDataRepository.delete(id);
        personalDataSearchRepository.delete(id);
    }

    /**
     * Search for the personalData corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PersonalDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PersonalData for query {}", query);
        Page<PersonalData> result = personalDataSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(personalDataMapper::toDto);
    }
}
