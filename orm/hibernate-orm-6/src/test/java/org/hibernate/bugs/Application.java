package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "APPLICATION")
public class Application extends AbstractSharedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_application")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = Role.ATTR_APPLICATION)
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof Application && Objects.equals(this.id, ((Application) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
