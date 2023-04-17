package org.hibernate.bugs;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

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

		Query query = entityManager.createQuery( "select creator.id = :accountId as ownership from Document" );
		query.setParameter( "accountId", 1L );
		List results = query.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Document" )
	@Table( name = "DOCUMENT" )
	public class Document {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		protected Long id;

		private String name;

		@ManyToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "creator_account_id" )
		private Account creator;
	}

	@Entity( name = "Account" )
	@Table( name = "ACCOUNT" )
	public class Account {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		protected Long id;

		@Column( nullable = false )
		protected String name;
	}
}
