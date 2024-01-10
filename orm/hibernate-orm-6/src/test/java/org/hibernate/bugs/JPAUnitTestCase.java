package org.hibernate.bugs;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UuidGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Persistence;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

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

		final List<LegalPerson> resultList = entityManager.createQuery(
				"select count(*) from LegalPerson p where p.id = :id",
				LegalPerson.class
		).setParameter( "id", 1L ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Person" )
	@SoftDelete( columnName = "soft_delete" )
	@Inheritance( strategy = InheritanceType.JOINED )
	public static class Person {
		@Id
		private Long id;

		private Long taxNumber;
	}

	@Entity( name = "LegalPerson" )
	@Table( name = "legal_persons" )
	@PrimaryKeyJoinColumn( name = "joined_fk" )
	public static class LegalPerson extends Person {
		private String legalName;
	}

	@Entity( name = "NaturalPerson" )
	@PrimaryKeyJoinColumn( name = "joined_fk" )
	public static class NaturalPerson extends Person {
		private String firstName;
	}
}
