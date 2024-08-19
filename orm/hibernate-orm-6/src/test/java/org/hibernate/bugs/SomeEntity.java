package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SomeEntity {
    private int id;
    private String name;

    @Id
    @Column(name = "id")
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "entity_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
