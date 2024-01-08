package org.hibernate.bugs;

import java.util.List;

import org.hibernate.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;

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
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist( new Cat( 1, "jason" ) );
		entityManager.persist( new Cat( 2, "john" ) );

		final Session session = entityManager.unwrap( Session.class );
		final List<Tuple> resultList = session.createNativeQuery(
						"SELECT {jason.*}, {john.*}  FROM CAT jason, CAT john WHERE jason.id = 1 and john.id = 2",
						Tuple.class
				)
				.addEntity( "jason", Cat.class )
				.addEntity( "john", Cat.class )
				.getResultList();
		final Tuple result = resultList.get( 0 );
		assertThat( result.get( 0, Cat.class ).getId() ).isEqualTo( 1 );
		assertThat( result.get( 0, Cat.class ).getName() ).isEqualTo( "jason" );
		assertThat( result.get( 1, Cat.class ).getId() ).isEqualTo( 2 );
		assertThat( result.get( 1, Cat.class ).getName() ).isEqualTo( "john" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Cat" )
	public static class Cat {
		@Id
		private Integer id;

		private String name;

		public Cat() {
		}

		public Cat(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
