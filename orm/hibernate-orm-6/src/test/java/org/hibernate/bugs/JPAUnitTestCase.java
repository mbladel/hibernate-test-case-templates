package org.hibernate.bugs;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Parent> cr = cb.createQuery( Parent.class );
		Root<Parent> parent = cr.from( Parent.class );
		parent.fetch( "association" );
//		List<Parent> resultList = entityManager.createQuery( cr ).getResultList();

		entityManager.getMetamodel()
				.managedType( Parent.class )
				.getAttribute( "association" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Parent" )
	@Table( name = "parent_test" )
	@Inheritance
	static class Parent {
		@Id
		Long id;
	}

	@Entity( name = "Child" )
	@DiscriminatorValue( "Child" )
	static class Child extends Parent {
		@OneToOne
		Association association;
	}

	@Entity( name = "Association" )
	@Table( name = "association_test" )
	static class Association {
		@Id
		Long id;
	}
}
