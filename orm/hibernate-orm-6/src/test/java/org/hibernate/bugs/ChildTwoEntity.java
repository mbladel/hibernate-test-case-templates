package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class ChildTwoEntity extends RelatedEntity {
	@Column(name = "ONLY_CHILD_TWO")
	private Integer onlyChildTwo;
}