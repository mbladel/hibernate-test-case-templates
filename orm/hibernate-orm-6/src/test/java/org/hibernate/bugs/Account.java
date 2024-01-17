package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity( name = "Account" )
public class Account {
	@Id
	private long id;

	@OneToMany( mappedBy = "account", cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
	private Set<Person> children = new HashSet<>();

	public void addChild(final Person child) {
		children.add( child );
	}
}
