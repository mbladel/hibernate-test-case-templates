package org.hibernate.bugs;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

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

		entityManager.persist( new Parent( new SomeString( "something" ), new CreationDate( new Date() ) ) );

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

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<Parent> from = cq.from( Parent.class );

		final Path<Comparable> someStringPath = from.get(Parent_.someString).get(SomeString_.value);
		final Expression<Timestamp> maxDate = cb.function("max", Timestamp.class,
														  from.get(Parent_.date).get(CreationDate_.value));
		cq.multiselect( someStringPath, maxDate );
		cq.groupBy( someStringPath );

		final TypedQuery<Tuple> query = entityManager.createQuery( cq );

		// This fails in Hibernate 6.5.2 and 6.6.0 because the mapping of the
		// generic "value" attribute is taken from SomeString and not from
		// CreationDate and will cause a ClassCastException. This worked fine
		// in Hibernate 6.4.10 and lower.
		final Tuple resultDate = query.getSingleResult();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
