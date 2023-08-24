package org.hibernate.bugs;

import java.math.BigDecimal;
import java.util.*;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

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
	public void hhh17085Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		persistData( entityManager );
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		Root<Primary> root = query.from( Primary.class );
		Join<Object, Object> secondaryJoin = root.join( "secondary" );
		//when
		query.multiselect(
				secondaryJoin.get( "entityName" ).alias( "secondary" ),
				cb.sum( root.get( "amount" ) ).alias( "sum" )
		).groupBy(
				secondaryJoin
		).orderBy(
				cb.desc( secondaryJoin.get( "entityName" ) )
		);
		List<Tuple> list = entityManager.createQuery( query ).getResultList();
		//then
		assertThat( list ).hasSize( 3 );
		Iterator<Tuple> tupleIt = list.iterator();
		Tuple tuple = tupleIt.next();
		assertThat( tuple.get( "secondary", String.class ) ).isEqualTo( "c" );
		assertThat( tuple.get( "sum", BigDecimal.class ) ).isEqualByComparingTo( new BigDecimal( "6000" ) );
		tuple = tupleIt.next();
		assertThat( tuple.get( "secondary", String.class ) ).isEqualTo( "b" );
		assertThat( tuple.get( "sum", BigDecimal.class ) ).isEqualByComparingTo( new BigDecimal( "600" ) );
		tuple = tupleIt.next();
		assertThat( tuple.get( "secondary", String.class ) ).isEqualTo( "a" );
		assertThat( tuple.get( "sum", BigDecimal.class ) ).isEqualByComparingTo( new BigDecimal( "60" ) );
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void persistData(EntityManager em) {
		Secondary secondaryA = createSecondary( 1, "a" );
		Secondary secondaryB = createSecondary( 2, "b" );
		Secondary secondaryC = createSecondary( 3, "c" );

		ArrayList<Object> entities = new ArrayList<>( Arrays.asList(
				createPrimary( 1, new BigDecimal( "10" ), secondaryA ),
				createPrimary( 2, new BigDecimal( "20" ), secondaryA ),
				createPrimary( 3, new BigDecimal( "30" ), secondaryA ),
				createPrimary( 4, new BigDecimal( "100" ), secondaryB ),
				createPrimary( 5, new BigDecimal( "200" ), secondaryB ),
				createPrimary( 6, new BigDecimal( "300" ), secondaryB ),
				createPrimary( 7, new BigDecimal( "1000" ), secondaryC ),
				createPrimary( 8, new BigDecimal( "2000" ), secondaryC ),
				createPrimary( 9, new BigDecimal( "3000" ), secondaryC )
		) );
		entities.addAll( Arrays.asList(
				secondaryA, secondaryB, secondaryC
		) );
		entities.forEach( em::persist );
		em.flush();
		em.clear();
	}

	private Primary createPrimary(int id, BigDecimal amount, Secondary secondary) {
		Primary entity = new Primary();
		entity.setId( id );
		entity.setAmount( amount );
		entity.setSecondary( secondary );
		return entity;
	}

	private Secondary createSecondary(int id, String entityName) {
		Secondary entity = new Secondary();
		entity.setId( id );
		entity.setEntityName( entityName );
		return entity;
	}
}
