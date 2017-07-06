package id.eara.service;

import id.eara.domain.StudentCoursePeriod;
import id.eara.repository.StudentCoursePeriodRepository;
import id.eara.repository.search.StudentCoursePeriodSearchRepository;
import id.eara.service.dto.StudentCoursePeriodDTO;
import id.eara.service.mapper.StudentCoursePeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing StudentCoursePeriod.
 * atiila consulting
 */

@Service
@Transactional
public class StudentCoursePeriodService {

    private final Logger log = LoggerFactory.getLogger(StudentCoursePeriodService.class);

    private final StudentCoursePeriodRepository studentCoursePeriodRepository;

    private final StudentCoursePeriodMapper studentCoursePeriodMapper;

    private final StudentCoursePeriodSearchRepository studentCoursePeriodSearchRepository;

    public StudentCoursePeriodService(StudentCoursePeriodRepository studentCoursePeriodRepository, StudentCoursePeriodMapper studentCoursePeriodMapper, StudentCoursePeriodSearchRepository studentCoursePeriodSearchRepository) {
        this.studentCoursePeriodRepository = studentCoursePeriodRepository;
        this.studentCoursePeriodMapper = studentCoursePeriodMapper;
        this.studentCoursePeriodSearchRepository = studentCoursePeriodSearchRepository;
    }

    /**
     * Save a studentCoursePeriod.
     *
     * @param studentCoursePeriodDTO the entity to save
     * @return the persisted entity
     */
    public StudentCoursePeriodDTO save(StudentCoursePeriodDTO studentCoursePeriodDTO) {
        log.debug("Request to save StudentCoursePeriod : {}", studentCoursePeriodDTO);
        StudentCoursePeriod studentCoursePeriod = studentCoursePeriodMapper.toEntity(studentCoursePeriodDTO);
        studentCoursePeriod = studentCoursePeriodRepository.save(studentCoursePeriod);
        StudentCoursePeriodDTO result = studentCoursePeriodMapper.toDto(studentCoursePeriod);
        studentCoursePeriodSearchRepository.save(studentCoursePeriod);
        return result;
    }

    /**
     *  Get all the studentCoursePeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentCoursePeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentCoursePeriods");
        return studentCoursePeriodRepository.findAll(pageable)
            .map(studentCoursePeriodMapper::toDto);
    }

    /**
     *  Get one studentCoursePeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudentCoursePeriodDTO findOne(UUID id) {
        log.debug("Request to get StudentCoursePeriod : {}", id);
        StudentCoursePeriod studentCoursePeriod = studentCoursePeriodRepository.findOne(id);
        return studentCoursePeriodMapper.toDto(studentCoursePeriod);
    }

    /**
     *  Delete the  studentCoursePeriod by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete StudentCoursePeriod : {}", id);
        studentCoursePeriodRepository.delete(id);
        studentCoursePeriodSearchRepository.delete(id);
    }

    /**
     * Search for the studentCoursePeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentCoursePeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudentCoursePeriods for query {}", query);
        Page<StudentCoursePeriod> result = studentCoursePeriodSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentCoursePeriodMapper::toDto);
    }
}
