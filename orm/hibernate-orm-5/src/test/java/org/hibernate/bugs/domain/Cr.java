package org.hibernate.bugs.domain;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@SuppressWarnings("squid:S2160")
public class Cr extends DomainBaseWithVersion
{
    @Lob
    private String lob;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "cr")
    // Do not use plain cascade = CascadeType.ALL as we should delete the PK only on last reference!
    private Set<Pk> pks;

    @ManyToOne
    private Ra ra;

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
    public Set<Pk> getPks()
    {
        return pks;
    }

    @SuppressWarnings("unused")
    public void setPks(Set<Pk> pks)
    {
        this.pks = pks;
    }

    @SuppressWarnings("unused")
    public Ra getRa()
    {
        return ra;
    }

    @SuppressWarnings("unused")
    public void setRa(Ra ra)
    {
        this.ra = ra;
    }
}
