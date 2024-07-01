package org.hibernate.bugs;

import java.sql.Timestamp;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
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
		entityManager.getTransaction().begin();

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Employee> cq = cb.createQuery( Employee.class );
		final Root<Employee> root = cq.from( Employee.class );
		cq.select( root );
		final TypedQuery<Employee> query = entityManager.createQuery( cq );
		// Set a very low timeout to force the exception
		query.setHint( "jakarta.persistence.query.timeout", 1 );
		// Execute the query
//		assertThrows( QueryTimeoutException.class, () -> {
		final List<Employee> employees = query.getResultList();
//		} );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Employee" )
	static class Employee {
		@Id
		private Long id;
		private String name;
		private Timestamp lastModifiedAt;
	}
}
