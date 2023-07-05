package org.hibernate.bugs.domain;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@SuppressWarnings("squid:S2160")
public class KpSet extends DomainBaseWithVersion
{
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "kpSet")
    private Set<Kp> kps;

    @OneToOne
    private C1Set c1Set;

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
    public Set<Kp> getKps()
    {
        return kps;
    }

    @SuppressWarnings("unused")
    public void setKps(Set<Kp> kps)
    {
        this.kps = kps;
    }

    @SuppressWarnings("unused")
    public C1Set getC1Set()
    {
        return c1Set;
    }

    @SuppressWarnings("unused")
    public void setC1Set(C1Set c1Set)
    {
        this.c1Set = c1Set;
    }
}
