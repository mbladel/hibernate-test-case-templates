package org.hibernate.bugs;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Persistence;

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
		final HibernateCriteriaBuilder cb = (HibernateCriteriaBuilder) entityManager.getCriteriaBuilder();
		final var insertCriteria = cb.createCriteriaInsertValues( DemoEntity.class );
		insertCriteria.setInsertionTargetPaths(
				insertCriteria.getTarget().get( "id" ),
				// Here we'd like to insert into foreign key columns.
				insertCriteria.getTarget().get( "a" ).get( "id" ), // a_id
				insertCriteria.getTarget().get( "b" ).get( "id" ), // b_id
				insertCriteria.getTarget().get( "c" ).get( "id" )  // c_id
		);
		insertCriteria.values( cb.values(
				cb.value( UUID.fromString( "6a7078ef-d761-4e05-b743-0d4b9eb242cf" ) ),
				cb.value( 1 ),
				cb.value( 2 ),
				cb.value( 3 )
		) );
		entityManager.unwrap( Session.class ).createMutationQuery( insertCriteria )
				.executeUpdate(); // Not expecting multiple table references for an SQM INSERT-SELECT exception :(

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity
	static class DemoEntity {
		@Id
		private UUID id;
		@ManyToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "a_id" )
		private A a;
		@ManyToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "b_id" )
		private B b;
		@ManyToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "c_id" )
		private C c;
	}

	@Entity
	static class A {
		@Id
		private Integer id;
	}

	@Entity
	static class B {
		@Id
		private Integer id;
	}

	@Entity
	static class C {
		@Id
		private Integer id;
	}
}
