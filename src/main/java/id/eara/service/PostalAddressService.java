package id.eara.service;

import id.eara.domain.PostalAddress;
import id.eara.repository.PostalAddressRepository;
import id.eara.repository.search.PostalAddressSearchRepository;
import id.eara.service.dto.PostalAddressDTO;
import id.eara.service.mapper.PostalAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing PostalAddress.
 * atiila consulting
 */

@Service
@Transactional
public class PostalAddressService {

    private final Logger log = LoggerFactory.getLogger(PostalAddressService.class);

    private final PostalAddressRepository postalAddressRepository;

    private final PostalAddressMapper postalAddressMapper;

    private final PostalAddressSearchRepository postalAddressSearchRepository;

    public PostalAddressService(PostalAddressRepository postalAddressRepository, PostalAddressMapper postalAddressMapper, PostalAddressSearchRepository postalAddressSearchRepository) {
        this.postalAddressRepository = postalAddressRepository;
        this.postalAddressMapper = postalAddressMapper;
        this.postalAddressSearchRepository = postalAddressSearchRepository;
    }

    /**
     * Save a postalAddress.
     *
     * @param postalAddressDTO the entity to save
     * @return the persisted entity
     */
    public PostalAddressDTO save(PostalAddressDTO postalAddressDTO) {
        log.debug("Request to save PostalAddress : {}", postalAddressDTO);
        PostalAddress postalAddress = postalAddressMapper.toEntity(postalAddressDTO);
        postalAddress = postalAddressRepository.save(postalAddress);
        PostalAddressDTO result = postalAddressMapper.toDto(postalAddress);
        postalAddressSearchRepository.save(postalAddress);
        return result;
    }

    /**
     *  Get all the postalAddresses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostalAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostalAddresses");
        return postalAddressRepository.findAll(pageable)
            .map(postalAddressMapper::toDto);
    }

    /**
     *  Get one postalAddress by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PostalAddressDTO findOne(UUID id) {
        log.debug("Request to get PostalAddress : {}", id);
        PostalAddress postalAddress = postalAddressRepository.findOne(id);
        return postalAddressMapper.toDto(postalAddress);
    }

    /**
     *  Delete the  postalAddress by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete PostalAddress : {}", id);
        postalAddressRepository.delete(id);
        postalAddressSearchRepository.delete(id);
    }

    /**
     * Search for the postalAddress corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostalAddressDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostalAddresses for query {}", query);
        Page<PostalAddress> result = postalAddressSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(postalAddressMapper::toDto);
    }
}
