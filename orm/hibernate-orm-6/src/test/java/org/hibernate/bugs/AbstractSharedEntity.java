package org.hibernate.bugs;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractSharedEntity {
    @Version
    private Integer version;

    public AbstractSharedEntity() {
    }
}
