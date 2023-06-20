package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity( name = "Entity1" )
public class Entity1 {
	@Id
	String id;

	@OneToOne( fetch = FetchType.LAZY, mappedBy = "entity1" )
	protected SubClass1 subClass1;

	@OneToOne( fetch = FetchType.LAZY, mappedBy = "entity1" )
	protected SubClass2 subClass2;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SubClass1 getSubClass1() {
		return subClass1;
	}

	public void setSubClass1(SubClass1 subClass1) {
		this.subClass1 = subClass1;
	}

	public SubClass2 getSubClass2() {
		return subClass2;
	}

	public void setSubClass2(SubClass2 subClass2) {
		this.subClass2 = subClass2;
	}
}
