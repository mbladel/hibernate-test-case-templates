package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;

import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

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

		final Person person = new Person( 1L, "Some person" );
		entityManager.persist( person );
		for ( int i = 0; i < 5; i++ ) {
			final PrivateData privateData = new PrivateData( person, "Some data " + i );
			entityManager.persist( privateData );
			for ( int j = 0; j < 5; j++ ) {
				entityManager.persist( new PrivateSubData( privateData, "Some subdata " + i ) );
			}
		}

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

		final Query query = entityManager.createQuery(
				"UPDATE PrivateSubData psd set psd.dataName = '...' where psd.data.person.id = ?1"
		);
		query.setParameter( 1, 1L );
		query.executeUpdate();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Person" )
	public static class Person {
		@Id
		private Long id;

		@Basic
		private String userName;

		public Person() {
		}

		public Person(Long id, String userName) {
			this.id = id;
			this.userName = userName;
		}
	}

	@Entity( name = "PrivateData" )
	public static class PrivateData {
		@Id
		@GeneratedValue
		private Long id;

		@Basic
		private String dataName;

		@ManyToOne
		private Person person;

		public PrivateData() {
		}

		public PrivateData(Person person, String dataName) {
			this.person = person;
			this.dataName = dataName;
		}
	}

	@Entity( name = "PrivateSubData" )
	public static class PrivateSubData {
		@Id
		@GeneratedValue
		private Long id;

		@Basic
		private String dataName;

		@ManyToOne
		private PrivateData data;

		public PrivateSubData() {
		}

		public PrivateSubData(PrivateData data, String dataName) {
			this.data = data;
			this.dataName = dataName;
		}
	}
}
