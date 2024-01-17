package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity( name = "Person" )
public class Person {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@ManyToOne( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
	protected Account account;

	public Person() {
	}

	public Person(final Account parent) {
		this.account = parent;
	}
}
