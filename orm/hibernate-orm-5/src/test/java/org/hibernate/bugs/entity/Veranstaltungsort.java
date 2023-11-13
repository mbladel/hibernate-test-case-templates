/**
 * Ticket2Rock ist die Beispielanwendung des Buchs "EJB 3.1 professionell"
 * (dpunkt). Es implementiert eine einfache Webanwendung zur Onlinebuchung von
 * Tickets für Rockkonzerten.
 *
 * Copyright (C) 2006-2011 Holisticon AG
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.hibernate.bugs.entity;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.bugs.entity.embedded.GeoKoordinaten;

/**
 * An einen Veranstaltungsort finden kommt es zur Durchführung eines oder
 * mehrerer Konzerte. Ein Veranstaltungsort besitzt eine Adresse und eine
 * Zuschauerkapazität.
 */
@Entity 
public class Veranstaltungsort implements Serializable {

    private static final long serialVersionUID = 5842719774249641276L;

    private int id;

    private String name;

    private String adresse;

    private int kapazitaet;

    private GeoKoordinaten koordinaten;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    @Embedded
	//@AttributeOverrides( {
    //		@AttributeOverride(name = "breite", column = @Column(name = "GEO_BREITE")),
    //		@AttributeOverride(name = "laenge", column = @Column(name = "GEO_LAENGE")) })
    public GeoKoordinaten getKoordinaten() {
        return koordinaten;
    }

    public void setKoordinaten(GeoKoordinaten koordinaten) {
        this.koordinaten = koordinaten;
    }
}
