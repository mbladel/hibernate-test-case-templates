package org.hibernate.bugs;

import java.io.Serializable;

public abstract class GenericObject<ID extends Serializable> {

	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (obj.getClass() == getClass()) {
			GenericObject<?> other = (GenericObject<?>) obj;
			return !(id == null || other.id == null) && id.equals(other.id);
		}

		return false;
	}
}
