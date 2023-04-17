package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    private long id;
    private String name;
}
