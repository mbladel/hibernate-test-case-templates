package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
