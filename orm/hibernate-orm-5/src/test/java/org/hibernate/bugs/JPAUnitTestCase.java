package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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

		TestEntity test = new TestEntity();
		test.setName("test");

		entityManager.persist(test);

		//from net.binis.example.db.entity.TransactionEntity u where (u.account.user.username like ?1) order by  u.timestamp desc' with params [(%ini%)]

		TypedQuery<TestEntity> query = entityManager.createQuery( "from org.hibernate.bugs.TestEntity u where (u.name like ?1)", TestEntity.class);
		query.setParameter(1, "test");
		System.out.println("With implementation: " + query.getSingleResult().getName());

		TypedQuery<org.hibernate.bugs.Test> queryFails = entityManager.createQuery("from org.hibernate.bugs.Test u where (u.name like ?1)", org.hibernate.bugs.Test.class);
		queryFails.setParameter(1, "test");
		System.out.println("With interface and where clause: " + queryFails.getSingleResult().getName());

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
