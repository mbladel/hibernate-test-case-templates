package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
				"select b from EntityB b " +
						"join fetch b.id.entityC c " +
						"join fetch c.id.entityA", EntityB.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "EntityA" )
	static class EntityA {
		@Id
		private String id;
	}

	@Entity( name = "EntityB" )
	static class EntityB {
		@EmbeddedId
		private EntityBId id;
	}

	@Embeddable
	static class EntityBId {
		@ManyToOne( fetch = FetchType.LAZY )
		private EntityC entityC;

		@ManyToOne( fetch = FetchType.LAZY )
		private AnotherEntity anotherEntity;
	}

	@Entity( name = "EntityC" )
	static class EntityC {
		@EmbeddedId
		private EntityCId id;
	}

	@Embeddable
	static class EntityCId {
		@ManyToOne( fetch = FetchType.LAZY )
		private EntityA entityA;

		private String featureName;
	}

	@Entity( name = "UnusedEntity" )
	static class AnotherEntity {
		@Id
		private Long id;
	}
}
