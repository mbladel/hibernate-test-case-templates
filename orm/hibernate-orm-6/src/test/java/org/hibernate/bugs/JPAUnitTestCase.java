package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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

		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery( Long.class );
		final Root<Document> queryRoot = criteriaQuery.from( Document.class );

		Predicate predicate = criteriaBuilder.equal( queryRoot.get( "test" ), "XYZ" );
		criteriaQuery
				.select( criteriaBuilder.count( queryRoot ) )
				.where( predicate );

		final TypedQuery<Long> query = entityManager.createQuery( criteriaQuery );
		query.getSingleResult();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Document" )
	@Inheritance( strategy = InheritanceType.JOINED )
	public static class Document {
		@Id
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	@Entity( name = "TestDocument" )
	public static class TestDocument extends Document {
		private String test;

		public String getTest() {
			return test;
		}

		public void setTest(String test) {
			this.test = test;
		}
	}
}
