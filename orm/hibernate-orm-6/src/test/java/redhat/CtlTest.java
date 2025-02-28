package redhat;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.testing.orm.junit.EntityManagerFactoryScope;
import org.hibernate.testing.orm.junit.Jpa;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Jpa(annotatedClasses = {
		EntIngotId.class,
		EntIngotIdPK.class,
		EntIngotRelationship.class,
		EntIngotNoRelationship.class,
})
public class CtlTest implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long ingotKey;
	private String numberSerial = "1";

	@BeforeAll
	public void setUp(EntityManagerFactoryScope scope) {
		scope.inTransaction( em -> {
			final Date date = new Date();

			final EntIngotNoRelationship entNo = new EntIngotNoRelationship();
			entNo.ingotKey = 1L;
			entNo.numberSerial = "1";
			entNo.codeIngotToken = date;
			em.persist( entNo );

			final EntIngotId id1 = new EntIngotId();
			id1.entIngot = entNo;
			id1.id = new EntIngotIdPK( date, new Date(), "1" );
			em.persist( id1 );

			final EntIngotId id2 = new EntIngotId();
			id2.entIngot = entNo;
			id2.id = new EntIngotIdPK( date, new Date(), "2" );
			em.persist( id2 );
		} );
	}

	@Test
	public void doSearchRelationship(EntityManagerFactoryScope scope) {
		scope.inTransaction( em -> {
			EntIngotRelationship entIngotRelationship = EntIngotRelationship.findBySerial( em, numberSerial );
			if ( entIngotRelationship != null ) {
				ingotKey = entIngotRelationship.getIngotKey();
			}
			else {
				ingotKey = null;
			}
		} );
	}

//	public void doSearchNoRelationship() {
//		EntIngotNoRelationship entIngotNoRelationship = sesnEJB.findBySerial( numberSerial );
//		if ( entIngotNoRelationship != null ) {
//			ingotKey = entIngotNoRelationship.getIngotKey();
//		}
//		else {
//			ingotKey = null;
//		}
//	}
//
//	public void doSearchRelationshipNotSupported() {
//		EntIngotRelationship entIngotRelationship = sesnEJB.findBySerialRelationshipNotSupported( numberSerial );
//		if ( entIngotRelationship != null ) {
//			ingotKey = entIngotRelationship.getIngotKey();
//		}
//		else {
//			ingotKey = null;
//		}
//	}
//
//	public void doSearchNoRelationshipNotSupported() {
//		EntIngotNoRelationship entIngotNoRelationship = sesnEJB.findBySerialNotSupported( numberSerial );
//		if ( entIngotNoRelationship != null ) {
//			ingotKey = entIngotNoRelationship.getIngotKey();
//		}
//		else {
//			ingotKey = null;
//		}
//	}
}
