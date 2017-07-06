package id.eara.service;

import id.eara.domain.RegularCourse;
import id.eara.repository.RegularCourseRepository;
import id.eara.repository.search.RegularCourseSearchRepository;
import id.eara.service.dto.RegularCourseDTO;
import id.eara.service.mapper.RegularCourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing RegularCourse.
 * atiila consulting
 */

@Service
@Transactional
public class RegularCourseService {

    private final Logger log = LoggerFactory.getLogger(RegularCourseService.class);

    private final RegularCourseRepository regularCourseRepository;

    private final RegularCourseMapper regularCourseMapper;

    private final RegularCourseSearchRepository regularCourseSearchRepository;

    public RegularCourseService(RegularCourseRepository regularCourseRepository, RegularCourseMapper regularCourseMapper, RegularCourseSearchRepository regularCourseSearchRepository) {
        this.regularCourseRepository = regularCourseRepository;
        this.regularCourseMapper = regularCourseMapper;
        this.regularCourseSearchRepository = regularCourseSearchRepository;
    }

    /**
     * Save a regularCourse.
     *
     * @param regularCourseDTO the entity to save
     * @return the persisted entity
     */
    public RegularCourseDTO save(RegularCourseDTO regularCourseDTO) {
        log.debug("Request to save RegularCourse : {}", regularCourseDTO);
        RegularCourse regularCourse = regularCourseMapper.toEntity(regularCourseDTO);
        regularCourse = regularCourseRepository.save(regularCourse);
        RegularCourseDTO result = regularCourseMapper.toDto(regularCourse);
        regularCourseSearchRepository.save(regularCourse);
        return result;
    }

    /**
     *  Get all the regularCourses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegularCourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RegularCourses");
        return regularCourseRepository.findAll(pageable)
            .map(regularCourseMapper::toDto);
    }

    /**
     *  Get one regularCourse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public RegularCourseDTO findOne(UUID id) {
        log.debug("Request to get RegularCourse : {}", id);
        RegularCourse regularCourse = regularCourseRepository.findOne(id);
        return regularCourseMapper.toDto(regularCourse);
    }

    /**
     *  Delete the  regularCourse by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete RegularCourse : {}", id);
        regularCourseRepository.delete(id);
        regularCourseSearchRepository.delete(id);
    }

    /**
     * Search for the regularCourse corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RegularCourseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RegularCourses for query {}", query);
        Page<RegularCourse> result = regularCourseSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(regularCourseMapper::toDto);
    }
}
