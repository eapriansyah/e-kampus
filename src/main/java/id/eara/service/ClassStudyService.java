package id.eara.service;

import id.eara.domain.ClassStudy;
import id.eara.repository.ClassStudyRepository;
import id.eara.repository.search.ClassStudySearchRepository;
import id.eara.service.dto.ClassStudyDTO;
import id.eara.service.mapper.ClassStudyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ClassStudy.
 * atiila consulting
 */

@Service
@Transactional
public class ClassStudyService {

    private final Logger log = LoggerFactory.getLogger(ClassStudyService.class);

    private final ClassStudyRepository classStudyRepository;

    private final ClassStudyMapper classStudyMapper;

    private final ClassStudySearchRepository classStudySearchRepository;

    public ClassStudyService(ClassStudyRepository classStudyRepository, ClassStudyMapper classStudyMapper, ClassStudySearchRepository classStudySearchRepository) {
        this.classStudyRepository = classStudyRepository;
        this.classStudyMapper = classStudyMapper;
        this.classStudySearchRepository = classStudySearchRepository;
    }

    /**
     * Save a classStudy.
     *
     * @param classStudyDTO the entity to save
     * @return the persisted entity
     */
    public ClassStudyDTO save(ClassStudyDTO classStudyDTO) {
        log.debug("Request to save ClassStudy : {}", classStudyDTO);
        ClassStudy classStudy = classStudyMapper.toEntity(classStudyDTO);
        classStudy = classStudyRepository.save(classStudy);
        ClassStudyDTO result = classStudyMapper.toDto(classStudy);
        classStudySearchRepository.save(classStudy);
        return result;
    }

    /**
     *  Get all the classStudies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClassStudyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassStudies");
        return classStudyRepository.findAll(pageable)
            .map(classStudyMapper::toDto);
    }

    /**
     *  Get one classStudy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ClassStudyDTO findOne(UUID id) {
        log.debug("Request to get ClassStudy : {}", id);
        ClassStudy classStudy = classStudyRepository.findOne(id);
        return classStudyMapper.toDto(classStudy);
    }

    /**
     *  Delete the  classStudy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ClassStudy : {}", id);
        classStudyRepository.delete(id);
        classStudySearchRepository.delete(id);
    }

    /**
     * Search for the classStudy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClassStudyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClassStudies for query {}", query);
        Page<ClassStudy> result = classStudySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(classStudyMapper::toDto);
    }
}
