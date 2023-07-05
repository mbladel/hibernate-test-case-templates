package org.hibernate.bugs.domain;

import java.security.SecureRandom;
import java.util.Objects;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class DomainBase
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String uuid;

    @SuppressWarnings("unused")
    protected DomainBase()
    {
    }

    @SuppressWarnings("unused")
    public String getUuid()
    {
        return uuid;
    }

    @SuppressWarnings("unused")
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        // Caution: When an object has not been saved, it will have no uuid yet.
        // This will occur while executing some tests.
        if (this.getUuid() == null)
        {
            return false;
        }

        DomainBase that = (DomainBase) o;

        return this.getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode()
    {
        if (getUuid() == null)
        {
            // Caution: Objects.hash() is able to handle null values.
            // But two objects with at least one has a null value for uuid are treated as not equal (see equals() above).
            // And two objects which are not equal must have different hashes.

            SecureRandom secureRandom = new SecureRandom();

            return secureRandom.nextInt();
        }

        return Objects.hash(getUuid());
    }
}
