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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Ein Song ist ein Musikstück, das von einem Interpreten (Musiker oder Band)
 * veröffentlicht wird. Ein Song hat einen Titel. Ferner lässt sich ein Song
 * einem Musikgenre zuordnen. Ein Song kann Bestandteil eines Albums sein.
 */
@Entity 
public class Song implements Serializable {

    private static final long serialVersionUID = -2533005104367435200L;

    private int id;

    private String titel;

    private Interpret interpret;

    private Musikgenre genre;

    private Album album;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    @ManyToOne
    public Interpret getInterpret() {
        return interpret;
    }

    public void setInterpret(Interpret interpret) {
        this.interpret = interpret;
    }

    @ManyToOne
    public Musikgenre getGenre() {
        return genre;
    }

    public void setGenre(Musikgenre genre) {
        this.genre = genre;
    }

    @ManyToOne
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
