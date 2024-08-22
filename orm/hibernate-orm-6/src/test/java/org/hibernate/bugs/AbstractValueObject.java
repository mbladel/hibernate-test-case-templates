package org.hibernate.bugs;

import java.io.Serializable;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import static jakarta.persistence.AccessType.FIELD;


@MappedSuperclass
@Access(FIELD)
public abstract class AbstractValueObject<V extends Comparable<V>> implements Serializable,
    Comparable<AbstractValueObject<V>> {

  public static final String VALUE = "value";

  @Column(name = VALUE)
  private V value;

  protected AbstractValueObject() {
    super();
  }

  protected AbstractValueObject(final V value) {
    this.value = value;
  }

  @Override
  public int compareTo(final AbstractValueObject<V> object) {
    return value.compareTo(object.value);
  }
}
