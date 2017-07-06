package id.eara.base;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EntityServiceImpl<ID extends Serializable, E, D extends EntityDTO<E, ID>, R extends JpaRepository<E, ID>> extends
		EntityMapper<ID, E, D, R> implements IEntityService<ID, E, D> {

	private static final long serialVersionUID = 1L;
	protected ElasticsearchRepository<D, ID> searchRepo;

    @Override
    public D save(D d) {
        D result = saveDTO(d);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> findAll(Pageable pageable) {
        Page<E> result = repo.findAll(pageable);
        return result.map(data -> newDTO(data));
    }

    @Override
    @Transactional(readOnly = true)
    public D findOne(ID id) {
        E e = repo.findOne(id);
        return e == null ? null : newDTO(e);
    }

    @Override
    public void delete(ID id) {
    	E e = repo.findOne(id);
    	if (e != null) {
    		D d = newDTO(e);
    		repo.delete(id);
        	postDeleteDTO(d, e);
    	}
    }

    @Override
    protected void postDeleteDTO(D d, E e) {
    	if (searchRepo != null) searchRepo.delete(d.getId());
    }

    @Override
    protected void postSaveDTO(D d, E e) {
    	if (searchRepo != null) searchRepo.save(d);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> search(String query, Pageable pageable) {
        Page<D> result = searchRepo.search(queryStringQuery(query), pageable);
        return result;
    }

}
