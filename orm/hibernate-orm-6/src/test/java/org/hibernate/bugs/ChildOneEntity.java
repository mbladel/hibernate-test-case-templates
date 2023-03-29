package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class ChildOneEntity extends RelatedEntity {
	@Column(name = "ONLY_CHILD_ONE")
	private Integer onlyChildOne;
}