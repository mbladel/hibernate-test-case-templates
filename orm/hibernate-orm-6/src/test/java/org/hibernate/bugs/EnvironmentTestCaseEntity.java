package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity( name = "EnvironmentTestCase" )
public class EnvironmentTestCaseEntity {
	@Id
	@Column( name = "env_key_col" )
	private String key;

	public EnvironmentTestCaseEntity() {
	}

	public EnvironmentTestCaseEntity(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
