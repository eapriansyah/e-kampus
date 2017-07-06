package id.eara.service;

import id.eara.domain.Lecture;
import id.eara.repository.LectureRepository;
import id.eara.repository.search.LectureSearchRepository;
import id.eara.service.dto.LectureDTO;
import id.eara.service.mapper.LectureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Lecture.
 * atiila consulting
 */

@Service
@Transactional
public class LectureService {

    private final Logger log = LoggerFactory.getLogger(LectureService.class);

    private final LectureRepository lectureRepository;

    private final LectureMapper lectureMapper;

    private final LectureSearchRepository lectureSearchRepository;

    public LectureService(LectureRepository lectureRepository, LectureMapper lectureMapper, LectureSearchRepository lectureSearchRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
        this.lectureSearchRepository = lectureSearchRepository;
    }

    /**
     * Save a lecture.
     *
     * @param lectureDTO the entity to save
     * @return the persisted entity
     */
    public LectureDTO save(LectureDTO lectureDTO) {
        log.debug("Request to save Lecture : {}", lectureDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO);
        lecture = lectureRepository.save(lecture);
        LectureDTO result = lectureMapper.toDto(lecture);
        lectureSearchRepository.save(lecture);
        return result;
    }

    /**
     *  Get all the lectures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LectureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lectures");
        return lectureRepository.findAll(pageable)
            .map(lectureMapper::toDto);
    }

    /**
     *  Get one lecture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LectureDTO findOne(UUID id) {
        log.debug("Request to get Lecture : {}", id);
        Lecture lecture = lectureRepository.findOne(id);
        return lectureMapper.toDto(lecture);
    }

    /**
     *  Delete the  lecture by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Lecture : {}", id);
        lectureRepository.delete(id);
        lectureSearchRepository.delete(id);
    }

    /**
     * Search for the lecture corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LectureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lectures for query {}", query);
        Page<Lecture> result = lectureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(lectureMapper::toDto);
    }
}
