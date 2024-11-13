package org.hibernate.bugs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Persistence;
import jakarta.persistence.SecondaryTable;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test() throws Exception {
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
