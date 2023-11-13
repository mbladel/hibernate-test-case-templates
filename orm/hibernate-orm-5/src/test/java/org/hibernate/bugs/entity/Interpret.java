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
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

/**
 * Ein Interpret ist entweder ein Musiker oder eine Band. Er hat einen Namen,
 * interpretiert einen oder mehrere Songs, kann Konzerte geben und Alben
 * veröffentlichen.
 */

@Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 @DiscriminatorColumn(name = "TYP", discriminatorType = DiscriminatorType.CHAR)
 @DiscriminatorValue("I") 
public abstract class Interpret implements Serializable {
    private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;
	
	private Set<Album> alben;
	
	private List<Song> songs;
	
	private List<Konzert> konzerte;
	
	private List<Tournee> tourneen;

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="interpret")
	public List<Konzert> getKonzerte() {
		return konzerte;
	}

	
	public void setKonzerte(List<Konzert> konzerte) {
		this.konzerte = konzerte;
	}

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "interpret")
	public List<Song> getSongs() {
		return songs;	
	}
	
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="InterpretGen")
	@TableGenerator(initialValue =100, name = "InterpretGen", 
                table = "TableSequences", pkColumnName = "SEQID",
                pkColumnValue = "Interpret",valueColumnName = "NextValue" )
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="interpret")
	public Set<Album> getAlben() {
	      return alben;
	}

	public void setAlben(Set<Album> alben) {
		this.alben = alben;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="interpret")
	public List<Tournee> getTourneen() {
		return tourneen;
	}


	public void setTourneen(List<Tournee> tourneen) {
		this.tourneen = tourneen;
	}
	

}
