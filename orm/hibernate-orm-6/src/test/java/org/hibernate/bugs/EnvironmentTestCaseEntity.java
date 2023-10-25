package org.hibernate.bugs;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity(name = "EnvironmentTestCase")
public class EnvironmentTestCaseEntity {
	@EmbeddedId
	EnvironmentKey key;

	public EnvironmentTestCaseEntity() {
	}

	public EnvironmentTestCaseEntity(EnvironmentKey key) {
		this.key = key;
	}

	public EnvironmentKey getKey() {
		return key;
	}
}
