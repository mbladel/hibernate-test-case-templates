package org.hibernate.bugs.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@SuppressWarnings("squid:S2160")
public class Pk extends DomainBaseWithVersion
{
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String lob;

    @OneToOne
    private Kp kp;

    @OneToOne
    private C1 c1;

    @ManyToOne
    private Cr cr;

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
    public Kp getKp()
    {
        return kp;
    }

    @SuppressWarnings("unused")
    public void setKp(Kp kp)
    {
        this.kp = kp;
    }

    @SuppressWarnings("unused")
    public C1 getC1()
    {
        return c1;
    }

    @SuppressWarnings("unused")
    public void setC1(C1 c1)
    {
        this.c1 = c1;
    }

    @SuppressWarnings("unused")
    public Cr getCr()
    {
        return cr;
    }

    @SuppressWarnings("unused")
    public void setCr(Cr cr)
    {
        this.cr = cr;
    }
}
