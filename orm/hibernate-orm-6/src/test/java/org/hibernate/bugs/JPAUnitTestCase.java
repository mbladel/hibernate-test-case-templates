package org.hibernate.bugs;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

		final List<Parent> resultList = entityManager.unwrap( Session.class ).createQuery(
				"from Parent p where element(p.children).id in (:ids)",
				Parent.class
		).setParameterList( "ids", List.of( BigInteger.valueOf( 1L ), BigInteger.valueOf( 2L ) ) ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Parent" )
	public static class Parent {
		@Id
		private Long id;

		@OneToMany
		@JoinColumn
		private List<Child> children;
	}

	@Entity( name = "Child" )
	public static class Child {
		@Id
		private BigInteger id;
	}
}
