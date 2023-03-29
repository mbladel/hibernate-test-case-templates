package org.hibernate.bugs;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MY_TABLE")
public class MyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "amount")
	private Integer amount;

	@JoinColumn(name = "REL_ENTITY", foreignKey = @ForeignKey(name = "FK_REL_ENT_01"))
	@ManyToOne
	@Fetch(FetchMode.SELECT)
	private RelatedEntity relatedEntity;
}