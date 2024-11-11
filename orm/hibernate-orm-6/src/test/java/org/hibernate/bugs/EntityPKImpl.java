/*
 * Copyright (c) b+m Informatik AG, Melsdorf <br /><br />
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

import jakarta.persistence.MappedSuperclass;

/**
 * Base implementation of the entity primary key
 *
 * @author son
 */
@MappedSuperclass
public abstract class EntityPKImpl {
	private String Id;
}
