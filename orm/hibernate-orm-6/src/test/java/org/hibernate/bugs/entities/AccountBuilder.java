package org.hibernate.bugs.entities;

public class AccountBuilder {



    private final Integer id;
    private final double amount;
    private final Double rate;
    private final Client client;
    private final AccountType accountType;


    public AccountBuilder(Integer id, double amount, Double rate, Client client, AccountType accountType) {
        this.id = id;
        this.amount = amount;
        this.rate = rate;
        this.client = client;
        this.accountType = accountType;
    }

    public Account build() {
        switch (accountType) {
            case CREDIT -> {
                return new CreditAccount(this);
            }
            case DEBIT -> {
                return new DebitAccount(this);
            }
            case OVERDRAWN -> {
                return new OverdrawnAccount(this);
            }
        }
        throw new IllegalArgumentException("Error");
    }

    public Integer getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Double getRate() {
        return rate;
    }

    public Client getClient() {
        return client;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
