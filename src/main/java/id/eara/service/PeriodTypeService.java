package id.eara.service;

import id.eara.domain.PeriodType;
import id.eara.repository.PeriodTypeRepository;
import id.eara.repository.search.PeriodTypeSearchRepository;
import id.eara.service.dto.PeriodTypeDTO;
import id.eara.service.mapper.PeriodTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing PeriodType.
 * atiila consulting
 */

@Service
@Transactional
public class PeriodTypeService {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeService.class);

    private final PeriodTypeRepository periodTypeRepository;

    private final PeriodTypeMapper periodTypeMapper;

    private final PeriodTypeSearchRepository periodTypeSearchRepository;

    public PeriodTypeService(PeriodTypeRepository periodTypeRepository, PeriodTypeMapper periodTypeMapper, PeriodTypeSearchRepository periodTypeSearchRepository) {
        this.periodTypeRepository = periodTypeRepository;
        this.periodTypeMapper = periodTypeMapper;
        this.periodTypeSearchRepository = periodTypeSearchRepository;
    }

    /**
     * Save a periodType.
     *
     * @param periodTypeDTO the entity to save
     * @return the persisted entity
     */
    public PeriodTypeDTO save(PeriodTypeDTO periodTypeDTO) {
        log.debug("Request to save PeriodType : {}", periodTypeDTO);
        PeriodType periodType = periodTypeMapper.toEntity(periodTypeDTO);
        periodType = periodTypeRepository.save(periodType);
        PeriodTypeDTO result = periodTypeMapper.toDto(periodType);
        periodTypeSearchRepository.save(periodType);
        return result;
    }

    /**
     *  Get all the periodTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PeriodTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodTypes");
        return periodTypeRepository.findAll(pageable)
            .map(periodTypeMapper::toDto);
    }

    /**
     *  Get one periodType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PeriodTypeDTO findOne(Long id) {
        log.debug("Request to get PeriodType : {}", id);
        PeriodType periodType = periodTypeRepository.findOne(id);
        return periodTypeMapper.toDto(periodType);
    }

    /**
     *  Delete the  periodType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PeriodType : {}", id);
        periodTypeRepository.delete(id);
        periodTypeSearchRepository.delete(id);
    }

    /**
     * Search for the periodType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PeriodTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PeriodTypes for query {}", query);
        Page<PeriodType> result = periodTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(periodTypeMapper::toDto);
    }
}
