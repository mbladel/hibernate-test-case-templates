/**
 *  Ticket2Rock ist die Beispielanwendung des Buchs "EJB 3.1 professionell" (dpunkt).
 *  Es implementiert eine einfache Webanwendung zur Onlinebuchung von Tickets für
 *  Rockkonzerten. 
 *
 *  Copyright (C) 2006-2011
 *  Holisticon AG
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.hibernate.bugs.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 * Eine Ticketbestellung bezieht sich immer auf ein einzelnes Konzert, kann
 * aber mehrere Tickets umfassen.
 */

@Entity 
public class Ticketbestellung implements Serializable {
    private static final long serialVersionUID = 1L;

	private int id;

	private Konzert konzert;

	private Kunde kunde;

	private int anzahl;
	
	
	private int erwarteteBesucher;
	

	/**
	 * ID ist zugleich Primärschlüssel und (fachliche) Bestell-ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TicketbestellungGen")
	@TableGenerator(initialValue = 2, name = "TicketbestellungGen", table = "TableSequences",
                pkColumnName = "SEQID", pkColumnValue = "Ticketbestellung",
                valueColumnName = "NextValue")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	public Konzert getKonzert() {
		return konzert;
	}

	public void setKonzert(Konzert konzert) {
		this.konzert = konzert;
	}

	@ManyToOne
	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
	@Transient
	public int getErwarteteBesucher() {
		return erwarteteBesucher;
	}
	
	public void setErwarteteBesucher(int besucher) {
		this.erwarteteBesucher = besucher;		
	}
}
