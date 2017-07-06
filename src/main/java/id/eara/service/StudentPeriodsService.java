package id.eara.service;

import id.eara.domain.StudentPeriods;
import id.eara.repository.StudentPeriodsRepository;
import id.eara.repository.search.StudentPeriodsSearchRepository;
import id.eara.service.dto.StudentPeriodsDTO;
import id.eara.service.mapper.StudentPeriodsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing StudentPeriods.
 * atiila consulting
 */

@Service
@Transactional
public class StudentPeriodsService {

    private final Logger log = LoggerFactory.getLogger(StudentPeriodsService.class);

    private final StudentPeriodsRepository studentPeriodsRepository;

    private final StudentPeriodsMapper studentPeriodsMapper;

    private final StudentPeriodsSearchRepository studentPeriodsSearchRepository;

    public StudentPeriodsService(StudentPeriodsRepository studentPeriodsRepository, StudentPeriodsMapper studentPeriodsMapper, StudentPeriodsSearchRepository studentPeriodsSearchRepository) {
        this.studentPeriodsRepository = studentPeriodsRepository;
        this.studentPeriodsMapper = studentPeriodsMapper;
        this.studentPeriodsSearchRepository = studentPeriodsSearchRepository;
    }

    /**
     * Save a studentPeriods.
     *
     * @param studentPeriodsDTO the entity to save
     * @return the persisted entity
     */
    public StudentPeriodsDTO save(StudentPeriodsDTO studentPeriodsDTO) {
        log.debug("Request to save StudentPeriods : {}", studentPeriodsDTO);
        StudentPeriods studentPeriods = studentPeriodsMapper.toEntity(studentPeriodsDTO);
        studentPeriods = studentPeriodsRepository.save(studentPeriods);
        StudentPeriodsDTO result = studentPeriodsMapper.toDto(studentPeriods);
        studentPeriodsSearchRepository.save(studentPeriods);
        return result;
    }

    /**
     *  Get all the studentPeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentPeriodsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentPeriods");
        return studentPeriodsRepository.findAll(pageable)
            .map(studentPeriodsMapper::toDto);
    }

    /**
     *  Get one studentPeriods by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudentPeriodsDTO findOne(UUID id) {
        log.debug("Request to get StudentPeriods : {}", id);
        StudentPeriods studentPeriods = studentPeriodsRepository.findOne(id);
        return studentPeriodsMapper.toDto(studentPeriods);
    }

    /**
     *  Delete the  studentPeriods by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete StudentPeriods : {}", id);
        studentPeriodsRepository.delete(id);
        studentPeriodsSearchRepository.delete(id);
    }

    /**
     * Search for the studentPeriods corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentPeriodsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudentPeriods for query {}", query);
        Page<StudentPeriods> result = studentPeriodsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentPeriodsMapper::toDto);
    }
}
