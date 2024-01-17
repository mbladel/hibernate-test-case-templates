package org.hibernate.bugs;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity( name = "Person" )
public class Person {
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE )
	private Long id;

	@ManyToOne( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
	protected Account account;

	public Person() {
	}

	public Person(final Account parent) {
		this.account = parent;
	}
}
