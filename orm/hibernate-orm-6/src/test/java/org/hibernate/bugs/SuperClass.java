package org.hibernate.bugs;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class SuperClass implements Serializable {
    @Id
    public String id;

    @ManyToOne
    protected Entity1 entity1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Entity1 getEntity1() {
        return entity1;
    }

    public void setEntity1(Entity1 entity1) {
        this.entity1 = entity1;
    }
}
