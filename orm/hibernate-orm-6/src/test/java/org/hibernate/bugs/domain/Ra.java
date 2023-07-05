package org.hibernate.bugs.domain;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

import org.hibernate.annotations.OptimisticLock;

@Entity
@SuppressWarnings("squid:S2160")
public class Ra extends DomainBaseWithVersion
{
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @OptimisticLock(excluded = true)
    private Set<Ca> cas;

    @SuppressWarnings("unused")
    public String getName()
    {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name)
    {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public Set<Ca> getCa()
    {
        return cas;
    }

    @SuppressWarnings("unused")
    public void setCa(Set<Ca> cas)
    {
        this.cas = cas;
    }
}
