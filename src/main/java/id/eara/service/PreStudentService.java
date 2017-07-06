package id.eara.service;

import id.eara.domain.PreStudent;
import id.eara.repository.PreStudentRepository;
import id.eara.repository.search.PreStudentSearchRepository;
import id.eara.service.dto.PreStudentDTO;
import id.eara.service.mapper.PreStudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing PreStudent.
 * atiila consulting
 */

@Service
@Transactional
public class PreStudentService {

    private final Logger log = LoggerFactory.getLogger(PreStudentService.class);

    private final PreStudentRepository preStudentRepository;

    private final PreStudentMapper preStudentMapper;

    private final PreStudentSearchRepository preStudentSearchRepository;

    public PreStudentService(PreStudentRepository preStudentRepository, PreStudentMapper preStudentMapper, PreStudentSearchRepository preStudentSearchRepository) {
        this.preStudentRepository = preStudentRepository;
        this.preStudentMapper = preStudentMapper;
        this.preStudentSearchRepository = preStudentSearchRepository;
    }

    /**
     * Save a preStudent.
     *
     * @param preStudentDTO the entity to save
     * @return the persisted entity
     */
    public PreStudentDTO save(PreStudentDTO preStudentDTO) {
        log.debug("Request to save PreStudent : {}", preStudentDTO);
        PreStudent preStudent = preStudentMapper.toEntity(preStudentDTO);
        preStudent = preStudentRepository.save(preStudent);
        PreStudentDTO result = preStudentMapper.toDto(preStudent);
        preStudentSearchRepository.save(preStudent);
        return result;
    }

    /**
     *  Get all the preStudents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PreStudentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PreStudents");
        return preStudentRepository.findAll(pageable)
            .map(preStudentMapper::toDto);
    }

    /**
     *  Get one preStudent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PreStudentDTO findOne(UUID id) {
        log.debug("Request to get PreStudent : {}", id);
        PreStudent preStudent = preStudentRepository.findOne(id);
        return preStudentMapper.toDto(preStudent);
    }

    /**
     *  Delete the  preStudent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete PreStudent : {}", id);
        preStudentRepository.delete(id);
        preStudentSearchRepository.delete(id);
    }

    /**
     * Search for the preStudent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PreStudentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PreStudents for query {}", query);
        Page<PreStudent> result = preStudentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(preStudentMapper::toDto);
    }
}
