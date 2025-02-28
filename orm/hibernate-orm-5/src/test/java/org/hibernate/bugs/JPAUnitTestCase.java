package org.hibernate.bugs;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;

import org.hibernate.testing.orm.junit.EntityManagerFactoryScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		final Date now = new Date();

		final EntIngotNoRelationship entNo = new EntIngotNoRelationship();
		entNo.ingotKey = 1L;
		entNo.numberSerial = "1";
		entNo.codeIngotToken = now;
		em.persist( entNo );

		final EntIngotId id1 = new EntIngotId();
		id1.entIngot = entNo;
		id1.id = new EntIngotIdPK("1", now);
		em.persist( id1 );

		final EntIngotId id2 = new EntIngotId();
		id2.entIngot = entNo;
		id2.id = new EntIngotIdPK("2", now);
		em.persist( id2 );

		em.getTransaction().commit();
		em.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
//		entityManager.getTransaction().begin();

		final EntIngotRelationship result = EntIngotRelationship.findBySerial( em, "1" );
		assert result != null;
		em.close();

//		entityManager.getTransaction().commit();
		em.close();
	}

	@Embeddable
	static class EntIngotIdPK implements Serializable {
		// default serial version id, required for serializable classes.
		private static final long serialVersionUID = 1L;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CODE_INGOT_TOKEN")
		private Date codeIngotToken;

		@Column(name = "NUMBER_SERIAL")
		private String numberSerial;

		public EntIngotIdPK() {
		}

		public EntIngotIdPK(String numberSerial, Date codeIngotToken) {
			this.numberSerial = numberSerial;
			this.codeIngotToken = codeIngotToken;
		}

		public String getNumberSerial() {
			return this.numberSerial == null ? null : this.numberSerial.trim();
		}

		public void setNumberSerial(String numberSerial) {
			this.numberSerial = numberSerial;
		}
	}

	@Entity(name = "EntIngotId")
	static class EntIngotId implements Serializable {
		private static final long serialVersionUID = 1L;

		@EmbeddedId
		private EntIngotIdPK id;

		@ManyToOne
		@JoinColumn//(name = "CODE_INGOT_TOKEN", referencedColumnName = "CODE_INGOT_TOKEN", insertable = false, updatable = false)
		private EntIngotNoRelationship entIngot;

		public EntIngotId() {
		}

		public EntIngotIdPK getId() {
			return this.id;
		}

		public void setId(EntIngotIdPK id) {
			this.id = id;
		}

		public EntIngotNoRelationship getEntIngot() {
			return this.entIngot;
		}

		public void setEntIngot(EntIngotNoRelationship entIngot) {
			this.entIngot = entIngot;
		}

	}

	@Entity(name = "EntIngotRelationship")
	@Table(name = "EntIngot")
	@NamedQueries({
			@NamedQuery(name = "EntIngotRelationship.findBySerial", query = "select distinct object(i) from EntIngotRelationship i, IN (i.entIdList) as ingotIdList where ingotIdList.id.numberSerial = ?1"),
	})
	static class EntIngotRelationship implements Serializable {
		private static final long serialVersionUID = 1L;

		public static EntIngotRelationship findBySerial(EntityManager em, String serial) {
			if ( serial == null ) {
				return null;
			}

			serial = serial.toUpperCase();
			Query qry = em.createNamedQuery( "EntIngotRelationship.findBySerial" );
			qry.setParameter( 1, serial );
			try {
				return (EntIngotRelationship) qry.getSingleResult();
			}
			catch (Exception e) {
				return null;
			}
		}

		@Id
		@Column(name = "INGOT_KEY")
		private long ingotKey;

		@Column(name = "NUMBER_SERIAL")
		private String numberSerial;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CODE_INGOT_TOKEN")
		private Date codeIngotToken;

		@OneToMany(mappedBy = "entIngot", fetch = FetchType.EAGER)
		private Set<EntIngotId> entIdList;
	}

	@Entity(name = "EntIngotNoRelationship")
	@Table(name = "EntIngot")
	@NamedQueries({
			@NamedQuery(name = "EntIngotNoRelationship.findBySerial", query = "select distinct object(i) from EntIngotNoRelationship i where i.numberSerial = ?1"),
	})
	static class EntIngotNoRelationship implements Serializable {
		private static final long serialVersionUID = 1L;

		public static EntIngotNoRelationship findBySerial(EntityManager em, String serial) {
			if ( serial == null ) {
				return null;
			}

			serial = serial.toUpperCase();
			Query qry = em.createNamedQuery( "EntIngotNoRelationship.findBySerial" );
			qry.setParameter( 1, serial );
			try {
				return (EntIngotNoRelationship) qry.getSingleResult();
			}
			catch (Exception e) {
				return null;
			}
		}

		@Id
		@Column(name = "INGOT_KEY")
		private long ingotKey;

		@Column(name = "NUMBER_SERIAL")
		private String numberSerial;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CODE_INGOT_TOKEN")
		private Date codeIngotToken;
	}
}
