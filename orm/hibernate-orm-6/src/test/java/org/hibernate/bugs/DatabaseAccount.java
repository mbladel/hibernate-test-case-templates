package org.hibernate.bugs;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "DatabaseAccount")
@DiscriminatorValue("Udatabase")
public class DatabaseAccount extends Account {

    public DatabaseAccount() {
        super();
        this.setAuthType("database");
    }

    public DatabaseAccount(String id, String loginName) {
        super(id, loginName);
        this.setAuthType("database");
    }

    public DatabaseAccount(String id, String loginName, User user) {
        super(id, loginName, user);
        this.setAuthType("database");
    }
}
