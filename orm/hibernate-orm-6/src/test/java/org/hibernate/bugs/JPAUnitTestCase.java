package org.hibernate.bugs;

import java.util.*;

import org.hibernate.bugs.model.Cat;
import org.hibernate.bugs.model.Leg;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();

		final long catId = createContents( entityManager );

		entityManager.getTransaction().begin();

		final List<Leg> resultList = entityManager.createQuery(
				"SELECT leg FROM Cat cat JOIN cat.legs leg",
				Leg.class
		).getResultList();

		System.err.println( "Found legs: " + resultList.size() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private static long createContents(EntityManager entityManager) {
		entityManager.getTransaction().begin();

		final Leg owner = new Leg();

		final Cat cat = new Cat();
		cat.addToLegs( owner );
		entityManager.persist( owner );
		entityManager.persist( cat );

		entityManager.getTransaction().commit();

		return cat.getId();

	}
}
