package org.hibernate.bugs;

import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;

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

		final SimpleEntity e2 = new SimpleEntity( 2, LocalTime.MAX );
		entityManager.persist( e2 );

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

		final SimpleEntity entity = entityManager.find( SimpleEntity.class, 2 );
		assertThat( entity.getTheLocalTime() ).isEqualToIgnoringNanos( LocalTime.MAX );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "SimpleEntity" )
	public static class SimpleEntity {
		@Id
		private Integer id;

		private LocalTime theLocalTime;

		public SimpleEntity() {
		}

		public SimpleEntity(Integer id, LocalTime theLocalTime) {
			this.id = id;
			this.theLocalTime = theLocalTime;
		}

		public LocalTime getTheLocalTime() {
			return theLocalTime;
		}
	}
}
