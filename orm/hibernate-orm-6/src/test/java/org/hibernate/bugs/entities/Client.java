package org.hibernate.bugs.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Client {
    @Id
    private Integer id;
    private String name;

    /*
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = TRAFIKANT_ID, nullable = false, updatable=false)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = BATCH_SIZE)
    private final Set<ForerettighetEntity> forerettigheter = new HashSet<>();
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false, updatable=false,insertable = false)
    @Fetch(FetchMode.SELECT)
    private final Set<Account> accounts = new HashSet<>();



    public Client() {}

    public Client(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Set<Account> getAccounts() {
        return accounts;
    }

}

