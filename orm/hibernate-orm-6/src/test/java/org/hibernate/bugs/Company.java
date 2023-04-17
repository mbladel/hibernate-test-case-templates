package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    private long id;
    private String name;
}
