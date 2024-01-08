package org.hibernate.bugs.entities;

import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.DiscriminatorOptions;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorFormula("CASE WHEN amount < 0 THEN 'O' WHEN accountType=0 then 'C' ELSE 'D' END")
@DiscriminatorOptions(force = true)
public class Account {

    @Id
    private Integer id;
    private double amount;
    private Double rate;
    private AccountType accountType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    public Account() {}

    public Account(Integer id, double amount, Double rate, Client client, AccountType accountType) {
        this.id = id;
        this.amount = amount;
        this.rate = rate;
        this.client = client;
        this.accountType = accountType;
    }

    public Account(AccountBuilder builder) {
        this.id = builder.getId();
        this.amount = builder.getAmount();
        this.rate = builder.getRate();
        this.client = builder.getClient();
        this.accountType = builder.getAccountType();
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

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public void setRate(Double rate) {
        this.rate = rate;
    }
    public AccountType getAccountType(){
        return null;
    }
}

