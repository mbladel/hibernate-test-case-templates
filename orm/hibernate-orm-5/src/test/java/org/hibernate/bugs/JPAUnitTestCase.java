package org.hibernate.bugs;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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

		ReferencedEntity ref1 = new ReferencedEntity();
		ref1.foo = 1;
		ReferencedEntity ref2 = new ReferencedEntity();
		ref2.foo = 2;
		entityManager.persist( ref1 );
		entityManager.persist( ref2 );

		EntityA entityA1 = new EntityA();
		EntityB entityB1 = new EntityB();
		entityA1.entityB = entityB1;
		entityB1.reference = ref1.id;
		entityManager.persist( entityB1 );
		entityManager.persist( entityA1 );

		EntityA entityA2 = new EntityA();
		EntityB entityB2 = new EntityB();
		entityA2.entityB = entityB2;
		entityB2.reference = ref2.id;
		entityManager.persist( entityB2 );
		entityManager.persist( entityA2 );

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

		TypedQuery<EntityA> query = entityManager.createQuery(
				"select a from EntityA a join a.entityB ab " //
				+ " where 0 < (select count(*) from ReferencedEntity r where r.foo = 1 and r.id = ab.reference)",
				EntityA.class
		);
		List<EntityA> actual = query.getResultList();
		assertThat( actual ).hasSize( 1 );


		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
