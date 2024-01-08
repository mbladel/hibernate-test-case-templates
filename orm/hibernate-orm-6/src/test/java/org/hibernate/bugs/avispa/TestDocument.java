package org.hibernate.bugs.avispa;

import jakarta.persistence.Entity;

/**
 * @author Rafał Hiszpański
 */
@Entity
public class TestDocument extends Document {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
