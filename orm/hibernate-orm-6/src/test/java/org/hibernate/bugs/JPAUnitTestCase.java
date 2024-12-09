package org.hibernate.bugs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import example.DerDA;
import example.DerDB;
import example.DerOA;
import example.DerOB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		final DerDB derba1 = new DerDB( 5);
		final DerDA derda1 = new DerDA( "1", "abase");
		deroa = new DerOA(derda1);
		derob  = new DerOB(derba1);

		em.getTransaction().commit();
		em.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	DerOA deroa;
	DerOB derob;

	@Test
	void testBaseProperty() {
		final EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.persist( deroa );
		em.persist( derob );
		em.getTransaction().commit();
		final Integer ida = deroa.getId();
		final Integer idb = derob.getId();

		em.clear();
		final TypedQuery<DerOA> qa = em.createQuery( "select o from DerOA o where o.id =:id", DerOA.class );
		qa.setParameter( "id", ida );
		final DerOA deroain = qa.getSingleResult();
		assertEquals( "abase", deroain.derda.baseprop );
	}

	@Test
	void testDerivedProperty() {
		final EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.persist( deroa );
		em.persist( derob );
		em.getTransaction().commit();
		final Integer idb = derob.getId();
		em.clear();

		final TypedQuery<DerOB> qb = em.createQuery( "select o from DerOB o where o.id =:id", DerOB.class );
		qb.setParameter( "id", idb );
		final DerOB derobin = qb.getSingleResult();
		assertNotNull( derobin );
		assertEquals( 5, derobin.derdb().b );
	}
}
