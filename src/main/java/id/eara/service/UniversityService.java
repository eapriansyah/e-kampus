package id.eara.service;

import id.eara.domain.University;
import id.eara.repository.UniversityRepository;
import id.eara.repository.search.UniversitySearchRepository;
import id.eara.service.dto.UniversityDTO;
import id.eara.service.mapper.UniversityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing University.
 * atiila consulting
 */

@Service
@Transactional
public class UniversityService {

    private final Logger log = LoggerFactory.getLogger(UniversityService.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    private final UniversitySearchRepository universitySearchRepository;

    public UniversityService(UniversityRepository universityRepository, UniversityMapper universityMapper, UniversitySearchRepository universitySearchRepository) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
        this.universitySearchRepository = universitySearchRepository;
    }

    /**
     * Save a university.
     *
     * @param universityDTO the entity to save
     * @return the persisted entity
     */
    public UniversityDTO save(UniversityDTO universityDTO) {
        log.debug("Request to save University : {}", universityDTO);
        University university = universityMapper.toEntity(universityDTO);
        university = universityRepository.save(university);
        UniversityDTO result = universityMapper.toDto(university);
        universitySearchRepository.save(university);
        return result;
    }

    /**
     *  Get all the universities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universities");
        return universityRepository.findAll(pageable)
            .map(universityMapper::toDto);
    }

    /**
     *  Get one university by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public UniversityDTO findOne(UUID id) {
        log.debug("Request to get University : {}", id);
        University university = universityRepository.findOne(id);
        return universityMapper.toDto(university);
    }

    /**
     *  Delete the  university by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete University : {}", id);
        universityRepository.delete(id);
        universitySearchRepository.delete(id);
    }

    /**
     * Search for the university corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UniversityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Universities for query {}", query);
        Page<University> result = universitySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(universityMapper::toDto);
    }
}
