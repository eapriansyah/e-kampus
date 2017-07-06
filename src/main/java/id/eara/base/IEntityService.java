package id.eara.base;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEntityService<ID extends Serializable, E, D extends EntityDTO<E, ID>> {

    D save(D d);

    Page<D> findAll(Pageable pageable);

    D findOne(ID id);

    void delete(ID id);

    Page<D> search(String query, Pageable pageable);
}
