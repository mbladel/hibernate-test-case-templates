package org.hibernate.bugs;

import java.util.List;

import org.hibernate.bugs.entity.AddressEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

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

		entityManager.persist( new AddressEntity( "Via Roma", "Pegognaga" ) );

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

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<AddressEntity> criteriaQuery = cb.createQuery( AddressEntity.class );
		final Root<AddressEntity> root = criteriaQuery.from( AddressEntity.class );
		final List<Selection<?>> selections = List.of( root.get( "id" ).alias( "id" ) );
		criteriaQuery.multiselect( selections );

		final TypedQuery<AddressEntity> query = entityManager.createQuery( criteriaQuery );

		final List<AddressEntity> list = query.getResultList();
		assertThat( list ).hasSize( 1 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
