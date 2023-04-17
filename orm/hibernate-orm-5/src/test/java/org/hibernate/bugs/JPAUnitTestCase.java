package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

//		String hql = "SELECT ilog.lateralEntity.project FROM ImageLog ilog JOIN FETCH ilog.lateralEntity.project.company";
		String hql1 = "select le from LateralEntity le join fetch le.project.company";
		Query query = entityManager.createQuery( hql1 );
//		query.setParameter(1, imageLogUuid);
		query.getResultList();

		String hql3 = "select le from LateralEntity le join fetch le.project p join fetch p.company c";
		Query query3 = entityManager.createQuery( hql3 );
		query3.getResultList();

		// this is expected to fail, we should select `le` not `p`
		String hql2 = "select p from LateralEntity le join fetch le.project p join fetch p.company c";
		Query query2 = entityManager.createQuery( hql2 );
		query2.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
