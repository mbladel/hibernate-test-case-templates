package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class EntityA {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;


    @OneToMany(mappedBy = "entityA", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<AbstractEntityB> properties = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AbstractEntityB> getProperties() {
        return properties;
    }

    public void setProperties(Set<AbstractEntityB> properties) {
        this.properties = properties;
    }
}
