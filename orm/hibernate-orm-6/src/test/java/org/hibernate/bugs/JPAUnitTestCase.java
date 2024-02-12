package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Basic;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	private static final String TABLE_NAME_COUNTRY = "COUNTRY";
	private static final String TABLE_NAME_LANGUAGE = "LANGUAGE";

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.persist( new Country( 1L, "ITA", "Italy" ) );

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

		entityManager.createQuery( "select a.code from VA a", String.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh123TestSelectClass() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery( "select a.class from VA a", String.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh123TestSelectType() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery( "select type(a) from VA a", String.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "VA" )
	@Inheritance( strategy = InheritanceType.JOINED )
	@DiscriminatorColumn( discriminatorType = DiscriminatorType.STRING )
	public static class VA {
		@Basic
		private String code;
		@Basic
		private String name;

		public VA() {
			//
		}

		public VA(final long id, final String code, final String name) {
			this.id = id;
			this.code = code;
			this.name = name;
		}

		@Id
		private long id;

		public long getId() {
			return id;
		}

		public void setId(final long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(final String code) {
			this.code = code;
		}
	}

	@Entity( name = "Country" )
	@Table( name = TABLE_NAME_COUNTRY )
	public static class Country extends VA {
		public Country() {
			//
		}

		public Country(final long id, final String code, final String name) {
			super( id, code, name );
		}
	}

	@Entity( name = "Language" )
	@Table( name = TABLE_NAME_LANGUAGE )
	public static class Language extends VA {
		public Language() {
			//
		}

		public Language(final long id, final String code, final String name) {
			super( id, code, name );
		}
	}
}
