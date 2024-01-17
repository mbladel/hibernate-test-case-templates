package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity( name = "Account" )
public class Account {
	@Id
	private long id;

	@OneToMany( mappedBy = "account"/*, cascade = { CascadeType.PERSIST, CascadeType.MERGE }*/ )
	private Set<Person> children = new HashSet<>();

	public void addChild(final Person child) {
		children.add( child );
	}
}
