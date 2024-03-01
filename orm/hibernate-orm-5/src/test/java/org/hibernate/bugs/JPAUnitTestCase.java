package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

		final EntityC entityC = new EntityC( 1L, "entity_c" );
		entityManager.persist( entityC );
		final EntityD entityD = new EntityD( 2L, "entity_d" );
		entityManager.persist( entityD );
		final EntityB entityB2 = new EntityB( 3L, entityC, entityD );
		entityManager.persist( entityB2 );
		entityManager.persist( new EntityA( 4L, entityB2 ) );

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

		root.fetch( "entityB", JoinType.INNER ).fetch( "entityC", JoinType.LEFT );
		root.fetch( "entityB", JoinType.LEFT ).fetch( "entityD", JoinType.LEFT );

		final EntityA singleResult = entityManager.createQuery( query ).getSingleResult();
		assertThat( singleResult ).isNotNull();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		private Long id;

		@ManyToOne( fetch = FetchType.LAZY )
		private EntityB entityB;

		public EntityA() {
		}

		public EntityA(Long id, EntityB entityB) {
			this.id = id;
			this.entityB = entityB;
		}

		public EntityB getEntityB() {
			return entityB;
		}
	}

	@Entity( name = "EntityB" )
	public static class EntityB {
		@Id
		private Long id;

		@ManyToOne( fetch = FetchType.LAZY )
		private EntityC entityC;

		@ManyToOne
		private EntityD entityD;

		public EntityB() {
		}

		public EntityB(Long id, EntityC entityC, EntityD entityD) {
			this.id = id;
			this.entityC = entityC;
			this.entityD = entityD;
		}

		public EntityD getEntityD() {
			return entityD;
		}

		public EntityC getEntityC() {
			return entityC;
		}
	}

	@Entity( name = "EntityC" )
	public static class EntityC {
		@Id
		private Long id;

		private String name;

		public EntityC() {
		}

		public EntityC(Long id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Entity( name = "EntityD" )
	public static class EntityD {
		@Id
		private Long id;

		private String name;

		public EntityD() {
		}

		public EntityD(Long id, String name) {
			this.id = id;
			this.name = name;
		}
	}
}
