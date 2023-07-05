package org.hibernate.bugs.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@SuppressWarnings("squid:S2160")
public class Kp extends DomainBaseWithVersion
{
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private KpSet kpSet;

    @OneToOne(cascade = CascadeType.ALL, optional = false, mappedBy = "kp")
    private Pk pk;

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
    public Pk getPk()
    {
        return pk;
    }

    @SuppressWarnings("unused")
    public void setPk(Pk pk)
    {
        this.pk = pk;
    }
}
