package org.hibernate.bugs.avispa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Document {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
