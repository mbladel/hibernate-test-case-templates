package org.hibernate.bugs;

import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
@Cacheable
@Entity
@Table( name = "MYASSOCIATEDENTITY" )
public class MyAssociatedEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "seq_myassociatedentity" )
	@Column( name = "ID" )
	private Long id;

	@Override
	public boolean equals(final Object o) {
		return o == this || o instanceof MyAssociatedEntity && Objects.equals( this.id, ( (MyAssociatedEntity) o ).id );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( id );
	}

}
