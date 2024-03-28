package org.hibernate.bugs;

import java.util.Objects;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;

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
		SessionFactory sessionFactory = entityManagerFactory.unwrap( SessionFactory.class );
		final StatelessSession session = sessionFactory.openStatelessSession();
		session.getTransaction().begin();

		session.setJdbcBatchSize( 5 );
		for ( int i = 0; i < 20; i++ ) {
			session.insert( new BasicEntity( i, "entity_" + i ) );
		}

		session.getTransaction().commit();
		session.close();
	}

	@Entity( name = "BasicEntity" )
	static class BasicEntity {
		@Id
		private Integer id;
		private String data;

		public BasicEntity() {

		}

		public BasicEntity(Integer id, String data) {
			this.id = id;
			this.data = data;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			final BasicEntity that = (BasicEntity) o;
			return Objects.equals( id, that.id ) &&
					Objects.equals( data, that.data );
		}

		@Override
		public int hashCode() {
			return Objects.hash( id, data );
		}
	}
}
