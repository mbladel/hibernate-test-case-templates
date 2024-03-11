package org.hibernate.bugs.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Cat extends Animal {

	@OneToMany(mappedBy = "partOf")
	private List<Leg> legs = new LinkedList<>();

	public List<Leg> getLegs() {
		return this.legs;
	}

	public void addToLegs(Leg person) {
		legs.add(person);
		person.partOf = this;
	}

}
