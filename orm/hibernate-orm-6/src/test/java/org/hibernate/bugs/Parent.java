package org.hibernate.bugs;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
public class Parent {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverride(name = SomeString.VALUE, column = @Column(name = "some_string"))
    private SomeString someString;

    @Embedded
    @AttributeOverride(name = CreationDate.VALUE, column = @Column(name = "some_date"))
    @Temporal(TIMESTAMP)
    private CreationDate date;

    public Parent() {
    }

    public Parent(final SomeString someString, final CreationDate date) {
        this.someString = someString;
        this.date = date;
    }
}
