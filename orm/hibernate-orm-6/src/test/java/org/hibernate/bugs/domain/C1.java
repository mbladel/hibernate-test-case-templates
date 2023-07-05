package org.hibernate.bugs.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@SuppressWarnings("squid:S2160")
public class C1 extends DomainBaseWithVersion
{
    @Column(nullable = false)
    @Lob
    private String lob;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private C1Set c1Set;

    @OneToOne(cascade = CascadeType.ALL, optional = false, mappedBy = "c1")
    private Pk pk;

    @SuppressWarnings("unused")
    public String getLob()
    {
        return lob;
    }

    @SuppressWarnings("unused")
    public void setLob(String lob)
    {
        this.lob = lob;
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
