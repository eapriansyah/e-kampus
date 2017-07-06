package id.eara.service;

import id.eara.domain.StudentPeriodData;
import id.eara.repository.StudentPeriodDataRepository;
import id.eara.repository.search.StudentPeriodDataSearchRepository;
import id.eara.service.dto.StudentPeriodDataDTO;
import id.eara.service.mapper.StudentPeriodDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing StudentPeriodData.
 * atiila consulting
 */

@Service
@Transactional
public class StudentPeriodDataService {

    private final Logger log = LoggerFactory.getLogger(StudentPeriodDataService.class);

    private final StudentPeriodDataRepository studentPeriodDataRepository;

    private final StudentPeriodDataMapper studentPeriodDataMapper;

    private final StudentPeriodDataSearchRepository studentPeriodDataSearchRepository;

    public StudentPeriodDataService(StudentPeriodDataRepository studentPeriodDataRepository, StudentPeriodDataMapper studentPeriodDataMapper, StudentPeriodDataSearchRepository studentPeriodDataSearchRepository) {
        this.studentPeriodDataRepository = studentPeriodDataRepository;
        this.studentPeriodDataMapper = studentPeriodDataMapper;
        this.studentPeriodDataSearchRepository = studentPeriodDataSearchRepository;
    }

    /**
     * Save a studentPeriodData.
     *
     * @param studentPeriodDataDTO the entity to save
     * @return the persisted entity
     */
    public StudentPeriodDataDTO save(StudentPeriodDataDTO studentPeriodDataDTO) {
        log.debug("Request to save StudentPeriodData : {}", studentPeriodDataDTO);
        StudentPeriodData studentPeriodData = studentPeriodDataMapper.toEntity(studentPeriodDataDTO);
        studentPeriodData = studentPeriodDataRepository.save(studentPeriodData);
        StudentPeriodDataDTO result = studentPeriodDataMapper.toDto(studentPeriodData);
        studentPeriodDataSearchRepository.save(studentPeriodData);
        return result;
    }

    /**
     *  Get all the studentPeriodData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentPeriodDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentPeriodData");
        return studentPeriodDataRepository.findAll(pageable)
            .map(studentPeriodDataMapper::toDto);
    }

    /**
     *  Get one studentPeriodData by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudentPeriodDataDTO findOne(UUID id) {
        log.debug("Request to get StudentPeriodData : {}", id);
        StudentPeriodData studentPeriodData = studentPeriodDataRepository.findOne(id);
        return studentPeriodDataMapper.toDto(studentPeriodData);
    }

    /**
     *  Delete the  studentPeriodData by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete StudentPeriodData : {}", id);
        studentPeriodDataRepository.delete(id);
        studentPeriodDataSearchRepository.delete(id);
    }

    /**
     * Search for the studentPeriodData corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentPeriodDataDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudentPeriodData for query {}", query);
        Page<StudentPeriodData> result = studentPeriodDataSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentPeriodDataMapper::toDto);
    }
}
