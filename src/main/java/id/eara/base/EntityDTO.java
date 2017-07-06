package id.eara.base;

import java.io.Serializable;

public abstract class EntityDTO<T, ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EntityDTO() {
	}

	public EntityDTO(T o) {
		super();
	}

    public void assign(T o) {
    	setDefaultValue();
    };

    public abstract ID getId();

    public void setId(ID value) {};

    protected void setDefaultValue() {
    }

}
