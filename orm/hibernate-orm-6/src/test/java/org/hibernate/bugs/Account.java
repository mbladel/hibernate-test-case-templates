package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "Account")
@DiscriminatorValue("Unone")
public abstract class Account extends Grantee {
    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "STATE", nullable = false)
    public String state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL, targetEntity =
            DomainAccount.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<DomainAccount> domainAccounts;

    public Account() {
        super();
        this.setGranteeType("U");
        this.setAuthType("none");
        this.domainAccounts = new HashSet<>();
    }

    public Account(String id, String name) {
        super(id);
        this.setGranteeType("U");
        this.setAuthType("none");
        this.name = name;
        this.state = "O";
        this.domainAccounts = new HashSet<>();
    }

    public String getLoginName() {
        return name;
    }

    public Set<DomainAccount> getDomainAccounts() {
        return domainAccounts;
    }

    public boolean hasDomainAccounts() {
        return domainAccounts.isEmpty();
    }
}
