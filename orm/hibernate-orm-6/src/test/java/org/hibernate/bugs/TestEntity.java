package org.hibernate.bugs;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "TABLE_TESTENTITY")
@Entity(name = "TestEntity")
public class TestEntity {
    @Id
    @Column(name = "ENTITY_ID")
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(nullable = false, name = "CHNG_ACCOUNT_ID")
    private Account changeAccount;
    @Column(nullable = false, name = "ENTITY_NAME")
    private String entityName;

    public TestEntity() {

    }

    public TestEntity(String id, String name, Account a){
        this.id = id;
        this.entityName = name;
        this.changeAccount = a;
    }
}
