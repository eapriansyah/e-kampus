package id.eara.service;

import id.eara.domain.ProgramStudy;
import id.eara.repository.ProgramStudyRepository;
import id.eara.repository.search.ProgramStudySearchRepository;
import id.eara.service.dto.ProgramStudyDTO;
import id.eara.service.mapper.ProgramStudyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ProgramStudy.
 * atiila consulting
 */

@Service
@Transactional
public class ProgramStudyService {

    private final Logger log = LoggerFactory.getLogger(ProgramStudyService.class);

    private final ProgramStudyRepository programStudyRepository;

    private final ProgramStudyMapper programStudyMapper;

    private final ProgramStudySearchRepository programStudySearchRepository;

    public ProgramStudyService(ProgramStudyRepository programStudyRepository, ProgramStudyMapper programStudyMapper, ProgramStudySearchRepository programStudySearchRepository) {
        this.programStudyRepository = programStudyRepository;
        this.programStudyMapper = programStudyMapper;
        this.programStudySearchRepository = programStudySearchRepository;
    }

    /**
     * Save a programStudy.
     *
     * @param programStudyDTO the entity to save
     * @return the persisted entity
     */
    public ProgramStudyDTO save(ProgramStudyDTO programStudyDTO) {
        log.debug("Request to save ProgramStudy : {}", programStudyDTO);
        ProgramStudy programStudy = programStudyMapper.toEntity(programStudyDTO);
        programStudy = programStudyRepository.save(programStudy);
        ProgramStudyDTO result = programStudyMapper.toDto(programStudy);
        programStudySearchRepository.save(programStudy);
        return result;
    }

    /**
     *  Get all the programStudies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProgramStudyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProgramStudies");
        return programStudyRepository.findAll(pageable)
            .map(programStudyMapper::toDto);
    }

    /**
     *  Get one programStudy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProgramStudyDTO findOne(UUID id) {
        log.debug("Request to get ProgramStudy : {}", id);
        ProgramStudy programStudy = programStudyRepository.findOne(id);
        return programStudyMapper.toDto(programStudy);
    }

    /**
     *  Delete the  programStudy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ProgramStudy : {}", id);
        programStudyRepository.delete(id);
        programStudySearchRepository.delete(id);
    }

    /**
     * Search for the programStudy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProgramStudyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProgramStudies for query {}", query);
        Page<ProgramStudy> result = programStudySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(programStudyMapper::toDto);
    }
}
