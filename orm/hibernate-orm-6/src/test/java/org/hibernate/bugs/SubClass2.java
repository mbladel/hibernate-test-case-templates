package org.hibernate.bugs;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class SubClass2 extends SuperClass {
}
