package org.hibernate.bugs;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity(name = "InfraOperationTestCase")
public class InfraOperationTestCaseEntity {
	@Id
	String id;

	@Enumerated(EnumType.STRING)
	InfraOperationStatus status;

	LocalDateTime createdTimestamp;

	@ManyToOne
	EnvironmentTestCaseEntity environment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InfraOperationStatus getStatus() {
		return status;
	}

	public void setStatus(InfraOperationStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public EnvironmentTestCaseEntity getEnvironment() {
		return environment;
	}

	public void setEnvironment(EnvironmentTestCaseEntity environment) {
		this.environment = environment;
	}
}
