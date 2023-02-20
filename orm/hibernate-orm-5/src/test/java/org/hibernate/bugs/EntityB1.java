package org.hibernate.bugs;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("DISC1")
public class EntityB1 extends AbstractEntityB {

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch= FetchType.EAGER)
    private Set<AbstractEntityB> children;

    public Set<AbstractEntityB> getChildren() {
        return children;
    }

    public void setChildren(Set<AbstractEntityB> children) {
        this.children = children;
    }
}
