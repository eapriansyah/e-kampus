package id.eara.service;

import id.eara.domain.ClassRoom;
import id.eara.repository.ClassRoomRepository;
import id.eara.repository.search.ClassRoomSearchRepository;
import id.eara.service.dto.ClassRoomDTO;
import id.eara.service.mapper.ClassRoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ClassRoom.
 * atiila consulting
 */

@Service
@Transactional
public class ClassRoomService {

    private final Logger log = LoggerFactory.getLogger(ClassRoomService.class);

    private final ClassRoomRepository classRoomRepository;

    private final ClassRoomMapper classRoomMapper;

    private final ClassRoomSearchRepository classRoomSearchRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository, ClassRoomMapper classRoomMapper, ClassRoomSearchRepository classRoomSearchRepository) {
        this.classRoomRepository = classRoomRepository;
        this.classRoomMapper = classRoomMapper;
        this.classRoomSearchRepository = classRoomSearchRepository;
    }

    /**
     * Save a classRoom.
     *
     * @param classRoomDTO the entity to save
     * @return the persisted entity
     */
    public ClassRoomDTO save(ClassRoomDTO classRoomDTO) {
        log.debug("Request to save ClassRoom : {}", classRoomDTO);
        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDTO);
        classRoom = classRoomRepository.save(classRoom);
        ClassRoomDTO result = classRoomMapper.toDto(classRoom);
        classRoomSearchRepository.save(classRoom);
        return result;
    }

    /**
     *  Get all the classRooms.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClassRoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassRooms");
        return classRoomRepository.findAll(pageable)
            .map(classRoomMapper::toDto);
    }

    /**
     *  Get one classRoom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ClassRoomDTO findOne(UUID id) {
        log.debug("Request to get ClassRoom : {}", id);
        ClassRoom classRoom = classRoomRepository.findOne(id);
        return classRoomMapper.toDto(classRoom);
    }

    /**
     *  Delete the  classRoom by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ClassRoom : {}", id);
        classRoomRepository.delete(id);
        classRoomSearchRepository.delete(id);
    }

    /**
     * Search for the classRoom corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClassRoomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ClassRooms for query {}", query);
        Page<ClassRoom> result = classRoomSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(classRoomMapper::toDto);
    }
}
