package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CS_USER_GROUP")
public class UserGroup extends GenericObject<Long> {

	private Long id;
	private String name;

	@Override
	@Id
	@SequenceGenerator(name = "CS_SEQ", sequenceName = "CS_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CS_SEQ")
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
