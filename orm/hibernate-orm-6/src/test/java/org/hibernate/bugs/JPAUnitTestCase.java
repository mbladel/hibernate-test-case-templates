package org.hibernate.bugs;

import java.util.*;
import jakarta.persistence.*;

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

		Entity1 entity1 = new Entity1();
		entity1.setId("1");
		entityManager.persist(entity1);

		SubClass1 subClass1 = new SubClass1();
		subClass1.setId("sub1");
		subClass1.setEntity1(entity1);
		entityManager.persist(subClass1);

		SubClass2 subClass2 = new SubClass2();
		subClass2.setId("sub2");
		subClass2.setEntity1(entity1);
		entityManager.persist(subClass2);

		entityManager.getTransaction().commit();
		entityManager.close();

		// start test

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		// this triggers and error!
		entity1 = entityManager.createQuery("select e from Entity1 e left join fetch e.subClass1 left join fetch e.subClass2", Entity1.class).getSingleResult();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
