package org.hibernate.bugs;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.ALL;

@Entity
public class EntityWithAny {

	@Id
	@GeneratedValue( strategy = IDENTITY )
	private Integer id;

	public String name;
	@JoinColumn( name = "OBJECTANY_ROLE" )
	@Cascade( ALL )
	@Any( metaColumn = @Column( name = "OBJECTANY_ID" ) )
	@AnyMetaDef(
			metaType = "string",
			idType = "integer",
			metaValues= {
			@MetaValue( value = "ANY", targetEntity = ObjectAny.class ),
	}
	)
	public Object objectAny;

}
