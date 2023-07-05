package org.hibernate.bugs.domain;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@SuppressWarnings("squid:S2160")
public class C1Set extends DomainBaseWithVersion
{
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "c1Set")
    private Set<C1> c1s;

    @OneToOne(mappedBy = "c1Set")
    private KpSet kpSet;

    @ManyToOne(optional = false)
    private Ca ca;

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
    public Set<C1> getC1s()
    {
        return c1s;
    }

    @SuppressWarnings("unused")
    public void setC1s(Set<C1> c1s)
    {
        this.c1s = c1s;
    }

    @SuppressWarnings("unused")
    public KpSet getKpSet()
    {
        return kpSet;
    }

    @SuppressWarnings("unused")
    public void setKpSet(KpSet kpSet)
    {
        this.kpSet = kpSet;
    }

    @SuppressWarnings("unused")
    public Ca getCa()
    {
        return ca;
    }

    @SuppressWarnings("unused")
    public void setCa(Ca ca)
    {
        this.ca = ca;
    }
}
