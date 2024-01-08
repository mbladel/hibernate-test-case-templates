package org.hibernate.bugs.entities;

public enum AccountType {
    OVERDRAWN("O"),
    DEBIT("D"),
    CREDIT("C");

    private String dbValue;
    AccountType(String dbValue) {
        this.dbValue = dbValue;
    }

}