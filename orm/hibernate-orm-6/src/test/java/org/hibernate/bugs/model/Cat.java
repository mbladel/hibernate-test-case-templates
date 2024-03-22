package org.hibernate.bugs.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;

@Entity
public class Cat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = { "ownedCats_id", "owners_id" }))
	@ManyToMany
	private Set<Person> owners = new HashSet<Person>();

	public long getId() {
		return id;
	}

	public Set<Person> getOwners() {
		return this.owners;
	}

	public void addToOwners(Person person) {
		owners.add(person);
	}

}
