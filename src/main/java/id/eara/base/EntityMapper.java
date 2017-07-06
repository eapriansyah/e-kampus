package id.eara.base;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class EntityMapper<ID extends Serializable, E, D extends EntityDTO<E, ID>, R extends JpaRepository<E, ID>> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Class<E> clazzE;
	protected Class<D> clazzD;

	protected R repo;
	protected Logger log = LoggerFactory.getLogger(EntityMapper.class);

    protected D newDTO(E e) {
    	try {
    		return clazzD.getDeclaredConstructor(clazzE).newInstance(e);
    	} catch (Exception ex) {
    		return null;
		}
    }

    @Transactional(propagation=Propagation.REQUIRED)
    protected E alternateSearchEntity(D d) {
    	return null;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    protected void prepareNewEntity(D d, E e) {
    }

    @Transactional(propagation=Propagation.REQUIRED)
    protected E getEntity(D d) {
    	try {
    		E r = null;

    		if (d.getId() != null) r = repo.findOne(d.getId());
    		else r = alternateSearchEntity(d);

    		if (r == null) {
    			r = clazzE.newInstance();
    			prepareNewEntity(d, r);
    		}

    		d.assign(r);
    		convertDTOToEntity(d, r);

    		return r;
    	} catch (Exception e) {
    		return null;
		}
    }


    @Transactional(propagation=Propagation.REQUIRED)
    protected void convertDTOToEntity(D d, E e) {
    }

    protected void postSaveDTO(D d, E e) {
    }

    protected void postDeleteDTO(D d, E e) {
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public D saveDTO(D d) {
    	E f = getEntity(d);
    	f = repo.save(f);
    	D n = newDTO(f);
    	postSaveDTO(n, f);
		return n;
    }

}
