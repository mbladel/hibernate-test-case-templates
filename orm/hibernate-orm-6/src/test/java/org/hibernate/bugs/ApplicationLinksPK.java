package org.hibernate.bugs;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ApplicationLinksPK implements Serializable {
    @Column(name = "AppId")
    private int appId;

    @Column(name = "Id")
    private int id;

    // Getters and setters, equals, and hashCode omitted for brevity
}
