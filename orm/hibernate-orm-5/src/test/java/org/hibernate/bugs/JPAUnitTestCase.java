package org.hibernate.bugs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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

	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		Building building1 = new Building( 0L, "building number one" );
		Building building2 = new Building( 1L, "building number two" );
		Building building3 = new Building( 2L, "building number three" );
		entityManager.persist( building1 );
		entityManager.persist( building2 );
		entityManager.persist( building3 );
		entityManager.persist( new Classroom( 0L, "classroom1", null, Collections.emptyList() ) );

		entityManager.createNativeQuery( "update classroom set OID_BUILDING = 4 where OID = 0" ).executeUpdate();

		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<Classroom> classrooms = fetchEagerNative( entityManager, "%classroom1%" );
//		List<Classroom> classrooms = fetchEager(entityManager, "%classroom1%");
		entityManager.getTransaction().commit();
		entityManager.close();

//		System.out.println( "----------------------> Record count " + classrooms.size() );
//		classrooms.forEach( a -> System.out.println( "----------------------> Description " + a.getDesCompletaAula() ) );
		assertTrue( Hibernate.isInitialized( classrooms.get( 0 ).getBuilding() ) );
		assertTrue( Hibernate.isInitialized( classrooms.get( 0 ).getAdiacentBuildings() ) );
	}

	private List<Classroom> fetchEagerNative(EntityManager entityManager, String pattern) {
//		String sqlQuery = "select t.*" +
//				" from CLASSROOM t," +
//				"     BUILDING e" +
//				" where t.OID_BUILDING = e.OID" +
//				"  and t.description like :pattern";
		String sqlQuery = "select * from classroom";

		return (List<Classroom>) entityManager
				.createNativeQuery( sqlQuery, Classroom.class )
//				.setParameter("pattern", pattern)
				.getResultList();
	}

	private List<Classroom> fetchEager(EntityManager entityManager, String pattern) {
		String sqlQuery = //"select t.*" +
				" from Classroom t" +
						" where" +
						"  t.description like :pattern";

		return (List<Classroom>) entityManager
				.createQuery( sqlQuery )
				.setParameter( "pattern", pattern )
				.getResultList();
	}
}
