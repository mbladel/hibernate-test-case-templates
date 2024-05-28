package org.hibernate.bugs;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "InternalAccount")
@DiscriminatorValue("Uinternal")
public class InternalAccount extends Account {

    public InternalAccount() {
        super();
        this.setAuthType("internal");
    }

    public InternalAccount(String id, String loginName) {
        super(id, loginName);
        this.setAuthType("internal");
    }

    public InternalAccount(String id, String loginName, User user) {
        super(id, loginName, user);
        this.setAuthType("internal");
    }
}
