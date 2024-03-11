package org.hibernate.bugs.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discr", discriminatorType = DiscriminatorType.INTEGER)
public class Animal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	protected String name;

	public long getId() {
		return id;
	}

}
