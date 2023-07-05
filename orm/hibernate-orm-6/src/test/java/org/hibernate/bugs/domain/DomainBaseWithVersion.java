package org.hibernate.bugs.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
@MappedSuperclass
@SuppressWarnings("squid:S2160")
public class DomainBaseWithVersion extends DomainBase
{
    @Version
    @SuppressWarnings("unused")
    protected long version;

    @SuppressWarnings("unused")
    protected DomainBaseWithVersion()
    {
        version = 0;
    }

    @SuppressWarnings("unused")
    public long getVersion()
    {
        return version;
    }

    @SuppressWarnings("unused")
    public void setVersion(long version)
    {
        this.version = version;
    }
}
