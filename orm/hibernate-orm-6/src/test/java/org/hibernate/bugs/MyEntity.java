package org.hibernate.bugs;

import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "MYENTITY")
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_myentity")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "PRIMARY_ASSOCIATED_ENTITY_ID")
    private MyAssociatedEntity primary;

    @ManyToOne
    @JoinColumn(name = "SECONDARY_ASSOCIATED_ENTITY_ID")
    private MyAssociatedEntity secondary;

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof MyEntity && Objects.equals(this.id, ((MyEntity) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
