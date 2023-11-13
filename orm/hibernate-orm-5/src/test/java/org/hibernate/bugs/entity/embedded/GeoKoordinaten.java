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
package org.hibernate.bugs.entity.embedded;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Eine Klasse zur Speicherung der Geo-Koordinaten eines geographischen Ortes.
 * Dieser Typ wird in der Persistent Entity Veranstaltungsort als eingebettetes
 * Objekt verwendet.
 */
@Embeddable
public class GeoKoordinaten implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private static String google_maps_base_url = "maps.google.de/maps?t=k&z=17";
    private float breite;
    private float laenge;

    private String google_maps_url = "";

@Column(name = "GEO_BREITE")
    public float getBreite() {
        return breite;
    }

    public void setBreite(float breite) {
        this.breite = breite;
        initGoogleMapsUrlString();
    }

@Column(name = "GEO_LAENGE")
    public float getLaenge() {
        return laenge;
    }

    public void setLaenge(float laenge) {
        this.laenge = laenge;
        initGoogleMapsUrlString();
    }

    @Transient
    public String getGoogleMapsUrlString() {
        return google_maps_url;
    }

    private void initGoogleMapsUrlString() {
        google_maps_url = google_maps_base_url + "&ll=" + this.breite + ","
                + this.laenge;
    }
}
