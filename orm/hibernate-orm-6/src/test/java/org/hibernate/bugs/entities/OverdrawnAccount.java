package org.hibernate.bugs.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("O")
public class OverdrawnAccount extends Account {
    public OverdrawnAccount() {

    }
    public OverdrawnAccount(AccountBuilder builder) {
        super(builder);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.OVERDRAWN;
    }


}
