/*
 * Copyright (c) Krakfin
 * All rights reserved
 */
package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "public", name = "t_secondary")
public class Secondary implements Serializable {

    @Id
    private int id;
    private String entityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Secondary secondary = (Secondary) o;
        return id == secondary.id;
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}
