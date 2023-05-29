package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class ObjectAny {

  @Id
  @GeneratedValue(strategy= IDENTITY)
  private Integer id;
}
