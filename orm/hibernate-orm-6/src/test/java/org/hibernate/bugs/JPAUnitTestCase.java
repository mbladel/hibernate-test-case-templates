package org.hibernate.bugs;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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

		final Child child = new Child();
		child.id = 1L;
		entityManager.persist( child );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Parent" )
	@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
	static class Parent {
		@Id
		protected Long id;
	}

	@Entity( name = "Child" )
	static class Child extends Parent {
		@Generated( event = EventType.INSERT )
		@Column( unique = true, insertable = false, updatable = false )
		protected Integer someGenerated;
	}
}
