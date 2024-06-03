package org.hibernate.bugs;

import java.io.Serializable;

import org.hibernate.annotations.Imported;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Persistence;

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

		entityManager.createQuery(
				"select new MyConcreteDto(e.id, e.someOtherField) from MyConcreteEntity e",
				MyConcreteDto.class
		).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@MappedSuperclass
	static abstract class AbstractEntity<K extends Serializable> {
		@Id
		protected K id;

		protected K getId() {
			return id;
		}

		protected void setId(K id) {
			this.id = id;
		}
	}

	@Entity( name = "MyConcreteEntity" )
	static class MyConcreteEntity extends AbstractEntity<Long> {
		protected String someOtherField;

		public String getSomeOtherField() {
			return someOtherField;
		}

		public void setSomeOtherField(String someOtherField) {
			this.someOtherField = someOtherField;
		}
	}

	@Imported
	public static class MyConcreteDto {
		protected Long id;

		protected String someOtherField;

		public MyConcreteDto(Long id, String someOtherField) {
			this.id = id;
			this.someOtherField = someOtherField;
		}
	}
}
