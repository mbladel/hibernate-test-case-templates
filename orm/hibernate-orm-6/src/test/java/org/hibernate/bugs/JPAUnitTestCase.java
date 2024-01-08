package org.hibernate.bugs;

import org.hibernate.bugs.entities.Account;
import org.hibernate.bugs.entities.AccountBuilder;
import org.hibernate.bugs.entities.AccountType;
import org.hibernate.bugs.entities.Client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.junit.Assert.assertEquals;

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

		final Client c = new Client( 1, "Gavin" );
		final Account da = new AccountBuilder( 1, 2, 3.0, c, AccountType.DEBIT ).build();
		final Account ca = new AccountBuilder( 2, 2, 3.0, c, AccountType.CREDIT ).build();
		final Account oa = new AccountBuilder( 3, -2, 3.0, c, AccountType.OVERDRAWN ).build();
		c.getAccounts().add( da );
		c.getAccounts().add( ca );
		c.getAccounts().add( oa );
		entityManager.persist( c );

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

		final Client client = entityManager.find( Client.class, 1 );
		assertEquals( 3, client.getAccounts().size() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
