package org.hibernate.bugs;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity( name = "LateralEntity" )
@Table( name = "laterals" )
public class LateralEntity {

	@Id
	public UUID uuid;

	@Column( name = "id" )
	public Long id;

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "project_uuid", nullable = false, updatable = false )
	public Project project;
}
