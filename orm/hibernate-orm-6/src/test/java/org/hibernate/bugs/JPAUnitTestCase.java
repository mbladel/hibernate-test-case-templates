package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaCteCriteria;
import org.hibernate.query.criteria.JpaRoot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		// Build main query
		final JpaCriteriaQuery<ClassA> mainQuery = (JpaCriteriaQuery<ClassA>) cb.createQuery( ClassA.class );
		final JpaRoot<ClassA> mainRoot = mainQuery.from( ClassA.class );

		final List<Predicate> joinWherePredicates = new ArrayList<>();

		// First criteria
		final CriteriaQuery<Tuple> firstQuery = cb.createTupleQuery();
		final Root<ClassA> firstRoot = firstQuery.from( ClassA.class );
		firstQuery.multiselect( firstRoot.get( "id" ).alias( "id_for_first" ) );
		firstQuery.where( cb.and(
				cb.isNotNull( firstRoot.get( "price" ) ),
				cb.greaterThan( firstRoot.get( "price" ), 1.2d )
		) );
		final JpaCteCriteria<Tuple> firstWithAlias = mainQuery.with( firstQuery );
		final JpaRoot<Tuple> fromFirstQuery = mainQuery.from( firstWithAlias );

		// Second criteria
		final CriteriaQuery<Tuple> secondQuery = cb.createTupleQuery();
		final Root<ClassA> secondRoot = secondQuery.from( ClassA.class );
		secondQuery.multiselect( secondRoot.get( "id" ).alias( "id_for_second" ) );
		secondQuery.where( cb.and(
				cb.isNotNull( secondRoot.get( "price" ) ),
				cb.lessThan( secondRoot.get( "price" ), 150.0d )
		) );
		final JpaCteCriteria<Tuple> secondWithAlias = mainQuery.with( secondQuery );
		final JpaRoot<Tuple> fromSecondQuery = mainQuery.from( secondWithAlias );

		// Join everything
		joinWherePredicates
				.add( cb.equal( mainRoot.get( "id" ), fromFirstQuery.get( "id_for_first" ) ) );
		joinWherePredicates
				.add( cb.equal( mainRoot.get( "id" ), fromSecondQuery.get( "id_for_second" ) ) );

		mainQuery.select( mainRoot );
		mainQuery.where( cb.and( joinWherePredicates.toArray( new Predicate[0] ) ) );
		mainQuery.orderBy( cb.asc( mainRoot.get( "name" ) ) );
		mainQuery.fetch( 10 );

		final List<ClassA> resultList = entityManager.createQuery( mainQuery )/*.setMaxResults( 10 )*/.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "ClassA" )
	public static class ClassA {
		@Id
		private Long id;

		private String name;

		private double price;
	}
}
