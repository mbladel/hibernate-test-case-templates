package org.hibernate.bugs;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity( name = "InfraOperation" )
public class InfraOperationTestCaseEntity {
	@Id
	private String id;

	@Enumerated( EnumType.STRING )
	private InfraOperationStatus status;

	private LocalDateTime createdTimestamp;

	@ManyToOne( cascade = CascadeType.PERSIST )
	private EnvironmentTestCaseEntity environment;

	public InfraOperationTestCaseEntity() {
	}

	public InfraOperationTestCaseEntity(
			String id,
			InfraOperationStatus status,
			LocalDateTime createdTimestamp,
			EnvironmentTestCaseEntity environment) {
		this.id = id;
		this.status = status;
		this.createdTimestamp = createdTimestamp;
		this.environment = environment;
	}

	public EnvironmentTestCaseEntity getEnvironment() {
		return environment;
	}
}
