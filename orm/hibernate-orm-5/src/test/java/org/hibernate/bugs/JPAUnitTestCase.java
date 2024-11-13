package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Persistence;
import javax.persistence.SecondaryTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery( "update SecondaryTableEntitySub e set e.b=:b, e.c=:c" )
				.setParameter( "b", 1 )
				.setParameter( "c", 2 )
				.executeUpdate();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "SecondaryTableEntityBase")
	@Inheritance(strategy = InheritanceType.JOINED)
	public class SecondaryTableEntityBase {

		private Long id;

		@Id
		@GeneratedValue
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity(name = "SecondaryTableEntitySub")
	@Inheritance(strategy = InheritanceType.JOINED)
	@SecondaryTable(name = "test")
	public class SecondaryTableEntitySub extends SecondaryTableEntityBase {
		private Long b;

		private Long c;

		@Column
		public Long getB() {
			return b;
		}

		public void setB(Long b) {
			this.b = b;
		}

		@Column(table = "test")
		public Long getC() {
			return c;
		}

		public void setC(Long c) {
			this.c = c;
		}
	}
}
