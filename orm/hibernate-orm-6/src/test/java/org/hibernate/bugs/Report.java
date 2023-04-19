package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table( name = "CS_REPORT" )
public class Report extends GenericObject<Long> {

	private Long id;
	private String code;

	@Override
	@Id
	@SequenceGenerator( name = "CS_SEQ", sequenceName = "CS_SEQ" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "CS_SEQ" )
	@Column( name = "ID" )
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column( name = "code" )
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
