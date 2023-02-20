package org.hibernate.bugs;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("DISC2")
public class EntityB2 extends AbstractEntityB {

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch= FetchType.EAGER)
    private Set<EntityC> values;

    public Set<EntityC> getValues() {
        return values;
    }

    public void setValues(Set<EntityC> values) {
        this.values = values;
    }
}
