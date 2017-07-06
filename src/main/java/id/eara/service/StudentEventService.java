package id.eara.service;

import id.eara.domain.StudentEvent;
import id.eara.repository.StudentEventRepository;
import id.eara.repository.search.StudentEventSearchRepository;
import id.eara.service.dto.StudentEventDTO;
import id.eara.service.mapper.StudentEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing StudentEvent.
 * atiila consulting
 */

@Service
@Transactional
public class StudentEventService {

    private final Logger log = LoggerFactory.getLogger(StudentEventService.class);

    private final StudentEventRepository studentEventRepository;

    private final StudentEventMapper studentEventMapper;

    private final StudentEventSearchRepository studentEventSearchRepository;

    public StudentEventService(StudentEventRepository studentEventRepository, StudentEventMapper studentEventMapper, StudentEventSearchRepository studentEventSearchRepository) {
        this.studentEventRepository = studentEventRepository;
        this.studentEventMapper = studentEventMapper;
        this.studentEventSearchRepository = studentEventSearchRepository;
    }

    /**
     * Save a studentEvent.
     *
     * @param studentEventDTO the entity to save
     * @return the persisted entity
     */
    public StudentEventDTO save(StudentEventDTO studentEventDTO) {
        log.debug("Request to save StudentEvent : {}", studentEventDTO);
        StudentEvent studentEvent = studentEventMapper.toEntity(studentEventDTO);
        studentEvent = studentEventRepository.save(studentEvent);
        StudentEventDTO result = studentEventMapper.toDto(studentEvent);
        studentEventSearchRepository.save(studentEvent);
        return result;
    }

    /**
     *  Get all the studentEvents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentEvents");
        return studentEventRepository.findAll(pageable)
            .map(studentEventMapper::toDto);
    }

    /**
     *  Get one studentEvent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudentEventDTO findOne(Long id) {
        log.debug("Request to get StudentEvent : {}", id);
        StudentEvent studentEvent = studentEventRepository.findOne(id);
        return studentEventMapper.toDto(studentEvent);
    }

    /**
     *  Delete the  studentEvent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentEvent : {}", id);
        studentEventRepository.delete(id);
        studentEventSearchRepository.delete(id);
    }

    /**
     * Search for the studentEvent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudentEvents for query {}", query);
        Page<StudentEvent> result = studentEventSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentEventMapper::toDto);
    }
}
