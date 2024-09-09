package org.hibernate.bugs;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		{
			String id = UUID.randomUUID().toString();
			{
				entityManager.getTransaction().begin();
				Foo foo = new Foo();
				foo.setId( id );
//				foo.setBar( "test" );
				entityManager.persist( foo );
				entityManager.getTransaction().commit();
			}

			{
				entityManager.getTransaction().begin();
				Foo foo = entityManager.find( Foo.class, id );
//				String actual = foo.getBar();
//				assertEquals( "test", actual );
				entityManager.getTransaction().commit();
			}
		}

		{
			String id = UUID.randomUUID().toString();
			String[] bars = { "1", "2", "3" };
			{
				entityManager.getTransaction().begin();
				Foo foo = new Foo();
				foo.setId( id );
//				foo.setBars( bars );
				entityManager.persist( foo );
				entityManager.getTransaction().commit();
			}

			{
				entityManager.getTransaction().begin();
				Foo foo = entityManager.find( Foo.class, id );
//				String[] actual = foo.getBars();
//				assertArrayEquals( bars, actual );
				entityManager.getTransaction().commit();
			}
		}

		{
			String id = UUID.randomUUID().toString();
			Integer[][] expected = new Integer[2][2];
			{
				entityManager.getTransaction().begin();
				// Do stuff...
				Foo foo = new Foo();
				foo.setId( id );
				expected[0] = new Integer[2];
				expected[0][0] = 1;
				expected[0][1] = 2;
				expected[1] = new Integer[2];
				expected[1][0] = 3;
				expected[1][1] = 4;
				foo.setIntegers( expected );
				entityManager.persist( foo );
				entityManager.getTransaction().commit();
			}

			{
				entityManager.getTransaction().begin();
				Foo foo = entityManager.find( Foo.class, id );
				Integer[][] actual = foo.getIntegers();
				assertArrayEquals( expected[0], actual[0] );
				assertArrayEquals( expected[1], actual[1] );

				entityManager.getTransaction().commit();
			}

		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Foo" )
	static class Foo {
		@Id
		@Column( name = "id", nullable = false, length = 36 )
		private String id;

//		@Column( name = "bar" )
//		private String bar;
//
//		@JdbcTypeCode( SqlTypes.ARRAY )
//		@Column( name = "bars" )
//		private String[] bars;

		@Column( name = "integer" )
		@JdbcTypeCode( SqlTypes.ARRAY )
		private Integer[][] integers;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

//		public String getBar() {
//			return bar;
//		}
//
//		public void setBar(String bar) {
//			this.bar = bar;
//		}
//
//		public String[] getBars() {
//			return bars;
//		}
//
//		public void setBars(String[] bars) {
//			this.bars = bars;
//		}

		public Integer[][] getIntegers() {
			return integers;
		}

		public void setIntegers(Integer[][] integers) {
			this.integers = integers;
		}
	}
}
