package org.hibernate.bugs;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.assertj.core.api.Assertions;

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
		Bar bar = new Bar( "unique3" );
		entityManager.persist( bar );
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

		Bar reference = entityManager.getReference( Bar.class, 1L );
		Foo merged = entityManager.merge( new Foo( reference ) );
		// Assertions.assertThat( merged.getBar().getKey() ).isEqualTo( "unique3" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Foo" )
	static class Foo {
		Foo(Bar bar) {
			this.bar = bar;
		}

		Foo() {
		}

		@Id
		@GeneratedValue
		long id;
		@ManyToOne( cascade = CascadeType.PERSIST, fetch = FetchType.EAGER )
		@Fetch( FetchMode.JOIN )
		@JoinColumn( name = "bar_key", referencedColumnName = "nat_key" )
		Bar bar;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Bar getBar() {
			return bar;
		}

		public void setBar(Bar bar) {
			this.bar = bar;
		}
	}

	@Entity( name = "Bar" )
	static class Bar implements Serializable {
		Bar(String key) {
			this.key = key;
		}

		Bar() {
		}

		@Id
		@GeneratedValue
		long id;
		@Column( name = "nat_key", unique = true )
		String key;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

}
