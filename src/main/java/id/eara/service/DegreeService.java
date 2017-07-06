package id.eara.service;

import id.eara.domain.Degree;
import id.eara.repository.DegreeRepository;
import id.eara.repository.search.DegreeSearchRepository;
import id.eara.service.dto.DegreeDTO;
import id.eara.service.mapper.DegreeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing Degree.
 * atiila consulting
 */

@Service
@Transactional
public class DegreeService {

    private final Logger log = LoggerFactory.getLogger(DegreeService.class);

    private final DegreeRepository degreeRepository;

    private final DegreeMapper degreeMapper;

    private final DegreeSearchRepository degreeSearchRepository;

    public DegreeService(DegreeRepository degreeRepository, DegreeMapper degreeMapper, DegreeSearchRepository degreeSearchRepository) {
        this.degreeRepository = degreeRepository;
        this.degreeMapper = degreeMapper;
        this.degreeSearchRepository = degreeSearchRepository;
    }

    /**
     * Save a degree.
     *
     * @param degreeDTO the entity to save
     * @return the persisted entity
     */
    public DegreeDTO save(DegreeDTO degreeDTO) {
        log.debug("Request to save Degree : {}", degreeDTO);
        Degree degree = degreeMapper.toEntity(degreeDTO);
        degree = degreeRepository.save(degree);
        DegreeDTO result = degreeMapper.toDto(degree);
        degreeSearchRepository.save(degree);
        return result;
    }

    /**
     *  Get all the degrees.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DegreeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Degrees");
        return degreeRepository.findAll(pageable)
            .map(degreeMapper::toDto);
    }

    /**
     *  Get one degree by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DegreeDTO findOne(Long id) {
        log.debug("Request to get Degree : {}", id);
        Degree degree = degreeRepository.findOne(id);
        return degreeMapper.toDto(degree);
    }

    /**
     *  Delete the  degree by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Degree : {}", id);
        degreeRepository.delete(id);
        degreeSearchRepository.delete(id);
    }

    /**
     * Search for the degree corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DegreeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Degrees for query {}", query);
        Page<Degree> result = degreeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(degreeMapper::toDto);
    }
}
