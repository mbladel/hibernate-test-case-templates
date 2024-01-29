package org.hibernate.bugs;

import java.util.Set;

import org.hibernate.annotations.SQLRestriction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

		entityManager.createQuery( "from Parent p join fetch p.children", Parent.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Parent" )
	public static class Parent {
		@Id
		@GeneratedValue
		private Long id;

		@OneToMany( mappedBy = "parent", cascade = CascadeType.ALL )
		@SQLRestriction( value = "removed = false" )
		private Set<Child> children;

		private String name;
	}

	@Entity( name = "Child" )
	public static class Child {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		private boolean removed = false;

		@ManyToOne
		@JoinColumn
		private Parent parent;
	}
}
