/*
 * Test
 *
 * Copyright (c) b+m Informatik AG, Melsdorf
 *
 * Alle Daten sind urheberrechtlich geschuetzt. Es sind Erzeugnisse der b+m Informatik AG,
 * Melsdorf, Deutschland. Jede unberechtigte Nutzung wird geahndet. Eine Berechtigung
 * zur Nutzung kann nur durch die b+m Informatik AG in Schriftform erfolgen. Sofern diese
 * Daten ohne schriftliche Uebertragung spezifizierter Nutzungsrechte bereitgestellt werden,
 * dienen sie lediglich der Information.
 * Sie sind verpflichtet, Urheberrechtshinweise nicht zu entfernen und solche
 * Hinweise originalgetreu auf alle von Ihnen rechtmaessig angelegten Kopien
 * beziehungsweise Datentraeger zu uebertragen.
 */
package org.hibernate.bugs;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/**
 * User entity-implementation
 */
@Entity(name = "UserImpl")
@IdClass(UserPKImpl.class)
public class UserImpl {
	/**
	 * Primary key of the entity
	 */
	@Id
	@Column(name = "ID")
	private String Id;


	//Only for query language support
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "Id", column = @Column(name = "ID", updatable = false, insertable = false))
	})
	private UserPKImpl PK;

	@Column(name = "USERNAME")
	private String ivUsername;

}
