package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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

		EntityA entityA = new EntityA();
		entityA = entityManager.merge( entityA );

		entityManager.getTransaction().commit();
		entityManager.clear();

		// second transaction

		entityManager.getTransaction().begin();

		EntityB entityB = new EntityB();
		entityB.id = 1L;
		entityB.name = "test";
		entityB.entityA = entityA;
		entityB = entityManager.merge( entityB );

		entityManager.getTransaction().commit();
		entityManager.clear();

		// third transaction

		entityManager.getTransaction().begin();

		entityManager.merge( entityB );

		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.close();
	}
}
