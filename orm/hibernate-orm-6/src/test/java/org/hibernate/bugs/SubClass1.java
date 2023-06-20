package org.hibernate.bugs;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class SubClass1 extends SuperClass {
}
