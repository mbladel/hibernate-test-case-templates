package org.hibernate.bugs.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class CreditAccount extends Account {
    public CreditAccount() {

    }

    public CreditAccount(AccountBuilder builder) {
        super(builder);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CREDIT;
    }
}
