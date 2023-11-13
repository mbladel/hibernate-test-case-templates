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
import javax.persistence.TableGenerator;

/**
 * Ein Musikgenre beschreibt eine bestimmte Musikrichtung.
 */
@Entity 
public class Musikgenre implements Serializable {

    private static final long serialVersionUID = -6275549486163454298L;

    private int id;

    private String name;

    @Id
    @TableGenerator(initialValue = 3, name = "GenreGen", table = "TableSequences",
            pkColumnName = "SEQID", pkColumnValue = "Genre",
            valueColumnName = "NextValue")
    @GeneratedValue(generator = "GenreGen")
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
}
