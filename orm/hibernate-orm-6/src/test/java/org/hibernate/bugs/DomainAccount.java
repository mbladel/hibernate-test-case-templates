package org.hibernate.bugs;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "DomainAccount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DomainAccount {
    @Id
    public String id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    public Account account;

    public DomainAccount() {

    }

    public DomainAccount(String id) {
        super();
        this.id = id;
    }
}
