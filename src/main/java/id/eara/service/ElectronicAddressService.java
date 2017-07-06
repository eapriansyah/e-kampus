package id.eara.service;

import id.eara.domain.ElectronicAddress;
import id.eara.repository.ElectronicAddressRepository;
import id.eara.repository.search.ElectronicAddressSearchRepository;
import id.eara.service.dto.ElectronicAddressDTO;
import id.eara.service.mapper.ElectronicAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ElectronicAddress.
 * atiila consulting
 */

@Service
@Transactional
public class ElectronicAddressService {

    private final Logger log = LoggerFactory.getLogger(ElectronicAddressService.class);

    private final ElectronicAddressRepository electronicAddressRepository;

    private final ElectronicAddressMapper electronicAddressMapper;

    private final ElectronicAddressSearchRepository electronicAddressSearchRepository;

    public ElectronicAddressService(ElectronicAddressRepository electronicAddressRepository, ElectronicAddressMapper electronicAddressMapper, ElectronicAddressSearchRepository electronicAddressSearchRepository) {
        this.electronicAddressRepository = electronicAddressRepository;
        this.electronicAddressMapper = electronicAddressMapper;
        this.electronicAddressSearchRepository = electronicAddressSearchRepository;
    }

    /**
     * Save a electronicAddress.
     *
     * @param electronicAddressDTO the entity to save
     * @return the persisted entity
     */
    public ElectronicAddressDTO save(ElectronicAddressDTO electronicAddressDTO) {
        log.debug("Request to save ElectronicAddress : {}", electronicAddressDTO);
        ElectronicAddress electronicAddress = electronicAddressMapper.toEntity(electronicAddressDTO);
        electronicAddress = electronicAddressRepository.save(electronicAddress);
        ElectronicAddressDTO result = electronicAddressMapper.toDto(electronicAddress);
        electronicAddressSearchRepository.save(electronicAddress);
        return result;
    }

    /**
     *  Get all the electronicAddresses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectronicAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ElectronicAddresses");
        return electronicAddressRepository.findAll(pageable)
            .map(electronicAddressMapper::toDto);
    }

    /**
     *  Get one electronicAddress by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ElectronicAddressDTO findOne(UUID id) {
        log.debug("Request to get ElectronicAddress : {}", id);
        ElectronicAddress electronicAddress = electronicAddressRepository.findOne(id);
        return electronicAddressMapper.toDto(electronicAddress);
    }

    /**
     *  Delete the  electronicAddress by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ElectronicAddress : {}", id);
        electronicAddressRepository.delete(id);
        electronicAddressSearchRepository.delete(id);
    }

    /**
     * Search for the electronicAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ElectronicAddressDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ElectronicAddresses for query {}", query);
        Page<ElectronicAddress> result = electronicAddressSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(electronicAddressMapper::toDto);
    }
}
