package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Marco Belladelli
 */
@Entity(name = "NullablePerson")
@Table(name = "NULLABLE_PERSON")
public class NullablePerson {
	@Id
	Long id;

	@Column
	String name;

	@Column
	Integer number;

	public NullablePerson() {
	}

	public NullablePerson(Long id, String name, Integer number) {
		this.id = id;
		this.name = name;
		this.number = number;
	}
}