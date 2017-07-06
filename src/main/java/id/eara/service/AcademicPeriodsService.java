package id.eara.service;

import id.eara.domain.AcademicPeriods;
import id.eara.repository.AcademicPeriodsRepository;
import id.eara.repository.search.AcademicPeriodsSearchRepository;
import id.eara.service.dto.AcademicPeriodsDTO;
import id.eara.service.mapper.AcademicPeriodsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing AcademicPeriods.
 * atiila consulting
 */

@Service
@Transactional
public class AcademicPeriodsService {

    private final Logger log = LoggerFactory.getLogger(AcademicPeriodsService.class);

    private final AcademicPeriodsRepository academicPeriodsRepository;

    private final AcademicPeriodsMapper academicPeriodsMapper;

    private final AcademicPeriodsSearchRepository academicPeriodsSearchRepository;

    public AcademicPeriodsService(AcademicPeriodsRepository academicPeriodsRepository, AcademicPeriodsMapper academicPeriodsMapper, AcademicPeriodsSearchRepository academicPeriodsSearchRepository) {
        this.academicPeriodsRepository = academicPeriodsRepository;
        this.academicPeriodsMapper = academicPeriodsMapper;
        this.academicPeriodsSearchRepository = academicPeriodsSearchRepository;
    }

    /**
     * Save a academicPeriods.
     *
     * @param academicPeriodsDTO the entity to save
     * @return the persisted entity
     */
    public AcademicPeriodsDTO save(AcademicPeriodsDTO academicPeriodsDTO) {
        log.debug("Request to save AcademicPeriods : {}", academicPeriodsDTO);
        AcademicPeriods academicPeriods = academicPeriodsMapper.toEntity(academicPeriodsDTO);
        academicPeriods = academicPeriodsRepository.save(academicPeriods);
        AcademicPeriodsDTO result = academicPeriodsMapper.toDto(academicPeriods);
        academicPeriodsSearchRepository.save(academicPeriods);
        return result;
    }

    /**
     *  Get all the academicPeriods.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AcademicPeriodsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcademicPeriods");
        return academicPeriodsRepository.findAll(pageable)
            .map(academicPeriodsMapper::toDto);
    }

    /**
     *  Get one academicPeriods by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AcademicPeriodsDTO findOne(UUID id) {
        log.debug("Request to get AcademicPeriods : {}", id);
        AcademicPeriods academicPeriods = academicPeriodsRepository.findOne(id);
        return academicPeriodsMapper.toDto(academicPeriods);
    }

    /**
     *  Delete the  academicPeriods by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete AcademicPeriods : {}", id);
        academicPeriodsRepository.delete(id);
        academicPeriodsSearchRepository.delete(id);
    }

    /**
     * Search for the academicPeriods corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AcademicPeriodsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AcademicPeriods for query {}", query);
        Page<AcademicPeriods> result = academicPeriodsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(academicPeriodsMapper::toDto);
    }
}
