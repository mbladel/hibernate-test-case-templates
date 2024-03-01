package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;

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
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<EntityA> query = builder.createQuery( EntityA.class );
		final Root<EntityA> root = query.from( EntityA.class );

		root.fetch( "entityB", JoinType.INNER ).fetch( "entityB", JoinType.LEFT );
		root.fetch( "entityB", JoinType.INNER ).fetch( "entityC", JoinType.LEFT );

		final List<EntityA> results = entityManager.createQuery( query ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		private Long id;

		@ManyToOne
		private EntityB entityB;
	}

	@Entity( name = "EntityB" )
	public static class EntityB {
		@Id
		private Long id;

		@ManyToOne
		private EntityB entityB;

		@ManyToOne
		private EntityC entityC;
	}

	@Entity( name = "EntityC" )
	public static class EntityC {
		@Id
		private Long id;
	}
}
