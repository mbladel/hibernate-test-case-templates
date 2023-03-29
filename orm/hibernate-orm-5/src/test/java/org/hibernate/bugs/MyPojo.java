package org.hibernate.bugs;

public class MyPojo {
	Long amount;
	RelatedEntity relatedEntity;

	public MyPojo(Long amount, RelatedEntity relatedEntity) {
		this.amount = amount;
		this.relatedEntity = relatedEntity;
	}

}
