package id.eara.service;

import id.eara.domain.StudentCourseScore;
import id.eara.repository.StudentCourseScoreRepository;
import id.eara.repository.search.StudentCourseScoreSearchRepository;
import id.eara.service.dto.StudentCourseScoreDTO;
import id.eara.service.mapper.StudentCourseScoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing StudentCourseScore.
 * atiila consulting
 */

@Service
@Transactional
public class StudentCourseScoreService {

    private final Logger log = LoggerFactory.getLogger(StudentCourseScoreService.class);

    private final StudentCourseScoreRepository studentCourseScoreRepository;

    private final StudentCourseScoreMapper studentCourseScoreMapper;

    private final StudentCourseScoreSearchRepository studentCourseScoreSearchRepository;

    public StudentCourseScoreService(StudentCourseScoreRepository studentCourseScoreRepository, StudentCourseScoreMapper studentCourseScoreMapper, StudentCourseScoreSearchRepository studentCourseScoreSearchRepository) {
        this.studentCourseScoreRepository = studentCourseScoreRepository;
        this.studentCourseScoreMapper = studentCourseScoreMapper;
        this.studentCourseScoreSearchRepository = studentCourseScoreSearchRepository;
    }

    /**
     * Save a studentCourseScore.
     *
     * @param studentCourseScoreDTO the entity to save
     * @return the persisted entity
     */
    public StudentCourseScoreDTO save(StudentCourseScoreDTO studentCourseScoreDTO) {
        log.debug("Request to save StudentCourseScore : {}", studentCourseScoreDTO);
        StudentCourseScore studentCourseScore = studentCourseScoreMapper.toEntity(studentCourseScoreDTO);
        studentCourseScore = studentCourseScoreRepository.save(studentCourseScore);
        StudentCourseScoreDTO result = studentCourseScoreMapper.toDto(studentCourseScore);
        studentCourseScoreSearchRepository.save(studentCourseScore);
        return result;
    }

    /**
     *  Get all the studentCourseScores.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentCourseScoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudentCourseScores");
        return studentCourseScoreRepository.findAll(pageable)
            .map(studentCourseScoreMapper::toDto);
    }

    /**
     *  Get one studentCourseScore by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudentCourseScoreDTO findOne(UUID id) {
        log.debug("Request to get StudentCourseScore : {}", id);
        StudentCourseScore studentCourseScore = studentCourseScoreRepository.findOne(id);
        return studentCourseScoreMapper.toDto(studentCourseScore);
    }

    /**
     *  Delete the  studentCourseScore by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete StudentCourseScore : {}", id);
        studentCourseScoreRepository.delete(id);
        studentCourseScoreSearchRepository.delete(id);
    }

    /**
     * Search for the studentCourseScore corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudentCourseScoreDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudentCourseScores for query {}", query);
        Page<StudentCourseScore> result = studentCourseScoreSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studentCourseScoreMapper::toDto);
    }
}
