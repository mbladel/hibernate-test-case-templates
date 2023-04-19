package org.hibernate.bugs;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class EmbeddableKey<O,E> implements Serializable {

	protected O owner;
	protected E entity;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public O getOwner() {
		return owner;
	}

	public void setOwner(O owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EmbeddableKey)) return false;

		EmbeddableKey<O, E> thatKey = (EmbeddableKey<O, E>) o;

		return entity.equals(thatKey.entity) && owner.equals(thatKey.owner);
	}

	@Override
	public int hashCode() {
		int result = owner.hashCode();
		result = 31 * result + entity.hashCode();
		return result;
	}
}
