package org.hibernate.bugs;

import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "MYASSOCIATEDENTITY")
public class MyAssociatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_myassociatedentity")
    @Column(name = "ID")
    private Long id;

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof MyAssociatedEntity && Objects.equals(this.id, ((MyAssociatedEntity) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
