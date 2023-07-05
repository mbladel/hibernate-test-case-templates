package org.hibernate.bugs.domain;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
@SuppressWarnings("squid:S2160")
public class Ca extends DomainBaseWithVersion
{
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String lob;

    @ManyToMany(mappedBy = "cas")
    private Set<Ra> ras;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ca")
    private Set<C1Set> c1Sets;

    // Getter and Setter

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
    public Set<C1Set> getC1Sets()
    {
        return c1Sets;
    }

    @SuppressWarnings("unused")
    public void setC1Sets(Set<C1Set> c1Sets)
    {
        this.c1Sets = c1Sets;
    }

    @SuppressWarnings("unused")
    public Set<Ra> getRas()
    {
        return ras;
    }

    @SuppressWarnings("unused")
    public void setRas(Set<Ra> ras)
    {
        this.ras = ras;
    }
}
