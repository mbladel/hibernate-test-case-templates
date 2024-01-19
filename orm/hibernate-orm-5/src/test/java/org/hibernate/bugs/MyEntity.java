package org.hibernate.bugs;

import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
@Cacheable
@Entity
@Table( name = "MYENTITY" )
public class MyEntity {

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "seq_myentity" )
	@Column( name = "ID" )
	private Long id;

	@ManyToOne
	@NotFound( action = NotFoundAction.IGNORE )
	@JoinColumn( name = "PRIMARY_ASSOCIATED_ENTITY_ID" )
	private MyAssociatedEntity primary;

	@ManyToOne
	@JoinColumn( name = "SECONDARY_ASSOCIATED_ENTITY_ID" )
	private MyAssociatedEntity secondary;

	@Override
	public boolean equals(final Object o) {
		return o == this || o instanceof MyEntity && Objects.equals( this.id, ( (MyEntity) o ).id );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( id );
	}

}
