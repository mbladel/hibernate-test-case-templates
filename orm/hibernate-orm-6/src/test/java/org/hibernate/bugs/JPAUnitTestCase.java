package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createQuery( "select c from JPAUnitTestCase$Car c where c.field = 'Some'" ).getFirstResult();
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@MappedSuperclass
	public static abstract class Vehicle<T> {
		@Id
		@GeneratedValue
		private Long id;
		private T field;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public T getField() {
			return field;
		}

		public void setField(T field) {
			this.field = field;
		}
	}

	@Entity
	public static class Car extends Vehicle<String> {
	}

}
