package org.hibernate.bugs;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @AfterEach
    void destroy() {
        entityManagerFactory.close();
    }

    // Entities are auto-discovered, so just add them anywhere on class-path
    // Add your tests, using standard JUnit.
    @Test
    void hhh18502Test_SqmSubQuery_In_Collection() throws Exception {
        createSomeEntity();

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<SomeEntity> query = builder.createQuery( SomeEntity.class);

		final Subquery<String> subquery = query.subquery( String.class);
		final Root<SomeEntity> subRoot = subquery.from( SomeEntity.class);
		final Path<String> subPathName = subRoot.get( "name");
		subquery.select(subPathName);

		final Root<SomeEntity> root = query.from( SomeEntity.class);
		query.select(root);
		// This line is the only difference between this test and hhh18502Test_SqmSubQuery_In_Array
		query.where(subquery.in(List.of("some_name", "another_name")));

		final TypedQuery<SomeEntity> typedQuery = entityManager.createQuery( query);
		final List<SomeEntity> resultList = typedQuery.getResultList();
		assertEquals(1, resultList.size());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

	@Test
	void hhh18502Test_SqmSubQuery_In_Array() throws Exception {
		createSomeEntity();

		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<SomeEntity> query = builder.createQuery( SomeEntity.class);

		final Subquery<String> subquery = query.subquery( String.class);
		final Root<SomeEntity> subRoot = subquery.from( SomeEntity.class);
		final Path<String> subPathName = subRoot.get( "name");
		subquery.select(subPathName);

		final Root<SomeEntity> root = query.from( SomeEntity.class);
		query.select(root);
		// This line is the only difference between this test and hhh18502Test_SqmSubQuery_In_Collection
		query.where(subquery.in(new Object[] {"some_name", "another_name" }));

		final TypedQuery<SomeEntity> typedQuery = entityManager.createQuery( query);
		final List<SomeEntity> resultList = typedQuery.getResultList();
		assertEquals(1, resultList.size());

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void createSomeEntity() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final SomeEntity entity = new SomeEntity();
		entity.setName("some_name");
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
