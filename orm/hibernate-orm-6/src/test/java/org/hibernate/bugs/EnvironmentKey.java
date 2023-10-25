package org.hibernate.bugs;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
public class EnvironmentKey implements Serializable {
	@Column(length = 63)
	String name;

	public EnvironmentKey() {
	}

	public EnvironmentKey(String name) {
		this.name = name;
	}
}
