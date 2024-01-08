package org.hibernate.bugs.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("D")
public class DebitAccount extends Account {
    public DebitAccount(AccountBuilder builder) {
        super(builder);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.DEBIT;
    }

    public DebitAccount() {

    }
}
