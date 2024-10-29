package org.hibernate.bugs;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.type.SqlTypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist( new Foo( "id-1", Map.of( "key-1", 1234 ) ) );

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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		final Session session = entityManager.unwrap( Session.class );

		{
			final HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
			final CriteriaUpdate<Foo> criteria = builder.createCriteriaUpdate( Foo.class );
			final Root<Foo> root = criteria.from( Foo.class );
			criteria.set( root.get( "bar" ), Map.of( "key-2", 88888888 ) );
			criteria.where( builder.equal( root.get( "id" ), "id-1" ) );
			final MutationQuery query = session.createMutationQuery( criteria );
			final int count = query.executeUpdate();

			System.out.println( count + " rows updated" );
		}

		{
			HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Foo> criteria = builder.createQuery( Foo.class );
			Root<Foo> root = criteria.from( Foo.class );
			criteria.select( root );
			criteria.where( builder.equal( root.get( "id" ), "id-1" ) );
			Query<Foo> query = session.createQuery( criteria );
			Foo foo = query.getSingleResult();

			System.out.println( foo.getBar() );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "Foo")
	static class Foo {
		@Id
		private String id;

		@JdbcTypeCode(SqlTypes.JSON)
		private Map<String, Object> bar;

		public Foo(String id, Map<String, Object> bar) {
			this.id = id;
			this.bar = bar;
		}

		public Foo() {
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Map<String, Object> getBar() {
			return bar;
		}

		public void setBar(Map<String, Object> bar) {
			this.bar = bar;
		}
	}

}
