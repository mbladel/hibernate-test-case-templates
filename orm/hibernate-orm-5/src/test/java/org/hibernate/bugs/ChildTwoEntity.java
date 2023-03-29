package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class ChildTwoEntity extends RelatedEntity {
	@Column(name = "ONLY_CHILD_TWO")
	private Integer onlyChildTwo;
}