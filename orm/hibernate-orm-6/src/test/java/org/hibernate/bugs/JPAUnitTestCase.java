package org.hibernate.bugs;

import jakarta.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.bugs.model.Cat;
import org.hibernate.bugs.model.Person;

import static org.junit.Assert.assertEquals;

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

	/**
	 * Test selection of specific columns of entity {@link Person} on a postgres
	 * database
	 */
	@Test
	public void testSelectColumnsOnPostgres() {
		final EntityManager entityManager = createEntityManager();
		testWithDefinedColumns( entityManager );
	}

	/**
	 * Test selection of specific columns of entity {@link Person} on a h2 database
	 */
	@Test
	public void testSelectColumnsOnH2() {
		final EntityManager entityManager = createEntityManager();
		testWithDefinedColumns( entityManager );
	}

	/**
	 * Test selection of full entity {@link Person} on a postgres database
	 */
	@Test
	public void testSelectPersonEntityOnPostgres() {
		// Has been fixed in 6.2.0 and reintroduced in 6.2.4
		final EntityManager entityManager = createEntityManager();
		testWithFullPersonEntitySelect( entityManager );
	}

	/**
	 * Test selection of full entity {@link Person} on a h2 database
	 */
	@Test
	public void testSelectPersonEntityOnH2() {
		final EntityManager entityManager = createEntityManager();
		testWithFullPersonEntitySelect( entityManager );
	}

	private void testWithFullPersonEntitySelect(EntityManager entityManager) {

		createContents( entityManager );
		entityManager.getTransaction().begin();

		final String queryString = "SELECT owner FROM Cat cat JOIN cat.owners owner GROUP BY owner";
		final TypedQuery<Person> query = entityManager.createQuery( queryString, Person.class );

		assertEquals( 1, query.getResultList().size() );

		entityManager.getTransaction().commit();
	}

	private void testWithDefinedColumns(EntityManager entityManager) {

		createContents( entityManager );
		entityManager.getTransaction().begin();

		final String queryString = "SELECT owner.id, owner.name FROM Cat cat JOIN cat.owners owner GROUP BY owner";
		final TypedQuery<Object[]> query = entityManager.createQuery( queryString, Object[].class );

		assertEquals( 1, query.getResultList().size() );

		entityManager.getTransaction().commit();
	}

	/**
	 * Create basic contents
	 *
	 * @param entityManager
	 *
	 * @return
	 */
	private static void createContents(EntityManager entityManager) {
		entityManager.getTransaction().begin();

		final Cat cat = new Cat();
		final Person owner = new Person();
		cat.addToOwners( owner );
		entityManager.persist( owner );
		entityManager.persist( cat );

		entityManager.getTransaction().commit();

	}

	/**
	 * Create an entity manager based on given data source properties
	 *
	 * @return the entity manager
	 */
	private EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
}
