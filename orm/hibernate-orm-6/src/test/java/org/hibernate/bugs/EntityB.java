package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;

/**
 * @author Marco Belladelli
 */
@Entity( name = "EntityB" )
public class EntityB {
	@Id
	public Long id;

//	@Version
//	public Long version;

	public String name;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn
	public EntityA entityA;
}