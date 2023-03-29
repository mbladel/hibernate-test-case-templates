package org.hibernate.bugs;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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