package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class ChildOneEntity extends RelatedEntity {
	@Column(name = "ONLY_CHILD_ONE")
	private Integer onlyChildOne;
}