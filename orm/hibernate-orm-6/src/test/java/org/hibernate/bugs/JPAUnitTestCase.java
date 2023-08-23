package org.hibernate.bugs;

import java.util.*;

import jakarta.persistence.*;

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

		Parent parent = new Parent();
		parent.id = 1L;
		entityManager.persist( parent );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void testFindParentById() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Parent parent = entityManager.find( Parent.class, 1L );
		assertThat( parent ).isNotNull();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void testQueryParentById() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Parent parent = entityManager.createQuery( "select p from Parent p where p.id = :id", Parent.class )
				.setParameter( "id", 1L )
				.getSingleResult();
		assertThat( parent ).isNotNull();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Parent" )
	public static class Parent {
		@Id
		Long id;
		@OneToOne
		Child child;
		// Getter and setters omitted for brevity
	}

	@Entity( name = "Child" )
	@Inheritance( strategy = InheritanceType.JOINED )
	public static class Child {
		@Id
		Long id;
		// Getter and setters omitted for brevity
	}

	@Entity( name = "ChildA" )
	public static class ChildA extends Child {
		@OneToOne
		Something something;
		// Getter and setters omitted for brevity
	}

	@Entity( name = "ChildB" )
	public static class ChildB extends Child {
		@OneToOne
		Something something;
		// Getter and setters omitted for brevity
	}

	@Entity( name = "Something" )
	public static class Something {
		@Id
		Long id;
		// Getter and setters omitted for brevity
	}
}
