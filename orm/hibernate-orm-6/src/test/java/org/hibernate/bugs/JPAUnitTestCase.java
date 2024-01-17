package org.hibernate.bugs;

import java.util.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
	public void hibernate6test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Account parent = new Account();
		entityManager.persist( parent );

		final Person user = new Person( parent );
		parent.addChild( user );
		entityManager.persist( parent );

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery( Long.class );
		final Root<Ticket> ticket = cq.from( Ticket.class );
		cq.select( cb.count( ticket ) ).where( ticket.get( "owner" ).in( List.of( user ) ) );
		final Long result = entityManager.createQuery( cq ).getSingleResult();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
