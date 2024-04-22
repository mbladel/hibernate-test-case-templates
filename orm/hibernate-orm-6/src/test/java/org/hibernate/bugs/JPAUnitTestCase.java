package org.hibernate.bugs;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Test
	public void hhh17393Test() throws Exception {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Employee emp1 = new Employee();
		emp1.id = 1;
		emp1.phoneNumbers = List.of( "0911 111 111", "0922 222 222" );
		entityManager.persist( emp1 );
		entityManager.flush();
		entityManager.clear();

		final List<String> phoneNumbersJpql;
		{
			phoneNumbersJpql = (List<String>) entityManager
					.createQuery( "select emp.phoneNumbers from Employee emp where emp.id = :EMP_ID" )
					.setParameter( "EMP_ID", emp1.id )
					.getSingleResult();
		}

		final List<String> phoneNumbersCriteria;
		{
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			final CriteriaQuery<List> q = cb.createQuery( List.class );
			final Root<Employee> r = q.from( Employee.class );
			q.select( r.get( "phoneNumbers" ) );
			q.where( cb.equal( r.get( "id" ), emp1.id ) );
			phoneNumbersCriteria = (List<String>) entityManager.createQuery( q ).getSingleResult();
		}

		assertEquals( emp1.phoneNumbers, phoneNumbersJpql );    //OK
		assertEquals( emp1.phoneNumbers, phoneNumbersCriteria ); //OK in 6.2, FAILS in 6.3, OK in 6.4.1

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh17598Test() throws Exception {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Employee emp1 = new Employee();
		emp1.id = 1;
		entityManager.persist( emp1 );
		entityManager.flush();
		entityManager.clear();

		final Object objectIds;
		{
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Object[]> q = cb.createQuery( Object[].class );
			final Root<Employee> r = q.from( Employee.class );
			q.multiselect( r.get( "id" ), r.get( "id" ) );
			q.where( cb.equal( r.get( "id" ), emp1.id ) );
			objectIds = entityManager.createQuery( q ).getSingleResult();
		}

		final Object integerIds;
		{
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Integer[]> q = cb.createQuery( Integer[].class );
			final Root<Employee> r = q.from( Employee.class );
			q.multiselect( r.get( "id" ), r.get( "id" ) );
			q.where( cb.equal( r.get( "id" ), emp1.id ) );
			integerIds = entityManager.createQuery( q ).getSingleResult();
		}

		assertTrue( objectIds.getClass().isArray() );
		assertInstanceOf( Object[].class, objectIds );
		assertArrayEquals( new Object[] { emp1.id, emp1.id }, (Object[]) objectIds );

		assertTrue( integerIds.getClass().isArray() ); //OK in 6.1.7, FAILS in 6.2, 6.3, 6.4.0, 6.4.1, 6.4.2
		assertInstanceOf( Object[].class, integerIds ); //OK in 6.1.7, FAILS in 6.2, 6.3, 6.4.0, 6.4.1, 6.4.2
		assertArrayEquals(
				new Object[] { emp1.id, emp1.id },
				(Object[]) integerIds
		); //OK in 6.1.7, FAILS in 6.2, 6.3, 6.4.0, 6.4.1, 6.4.2

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh17956Test() throws Exception {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Employee emp1 = new Employee();
		emp1.id = 1;
		entityManager.persist( emp1 );
		entityManager.flush();
		entityManager.clear();

		final List<Integer[]> idPairs;
		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Integer[]> q = cb.createQuery( Integer[].class );
		final Root<Employee> r = q.from( Employee.class );
		q.multiselect( List.of( r.get( "id" ), r.get( "id" ) ) );
		idPairs = entityManager
				.createQuery( q )
				.getResultList();

		final Object rawIdPair0 = idPairs.get( 0 );
		assertInstanceOf( Integer[].class, rawIdPair0 );

		final Integer[] idPair0 = (Integer[]) rawIdPair0;
		assertEquals( 1, idPair0[0] );
		assertEquals( 1, idPair0[1] );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Employee" )
	public static class Employee {
		@Id
		private Integer id;

		@Convert( converter = StringListConverter.class )
		private List<String> phoneNumbers;
	}

	public static class StringListConverter implements AttributeConverter<List<String>, String> {
		@Override
		public String convertToDatabaseColumn(List<String> elements) {
			return elements == null || elements.isEmpty() ? null : String.join( ",", elements );
		}

		@Override
		public List<String> convertToEntityAttribute(String dbData) {
			return dbData == null ? null : List.of( dbData.split( "," ) );
		}
	}
}
