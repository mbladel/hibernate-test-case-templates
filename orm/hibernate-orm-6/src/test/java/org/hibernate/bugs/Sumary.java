package org.hibernate.bugs;

import java.math.BigDecimal;

public class Sumary {

	private Item item;
	
	private BigDecimal total;
	
	private Sumary(Item item, BigDecimal total) {
		this.item = item;
		this.total = total;
	}
	
	public Item getItem() {
		return item;
	}

	public BigDecimal getTotal() {
		return total;
	}
}
