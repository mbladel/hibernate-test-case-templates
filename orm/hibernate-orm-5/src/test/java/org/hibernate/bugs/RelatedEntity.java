package org.hibernate.bugs;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MY_TABLE")
@DiscriminatorColumn(name = "DISC_COL", discriminatorType = DiscriminatorType.INTEGER)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class RelatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	Integer id;

	@Column(name = "RELATED_VALUE")
	Integer value;
}