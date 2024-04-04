package org.hibernate.bugs;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import jakarta.persistence.Version;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

		final Dog dog = entityManager.merge( new Dog() );
		assertEquals( 0L, dog.getVersion() );

		entityManager.flush();
		entityManager.clear();

//		final List<Animal> resultList = entityManager.createQuery( "from Animal", Animal.class )
//				.setLockMode( LockModeType.OPTIMISTIC_FORCE_INCREMENT )
//				.getResultList();
		// final Animal result = resultList.get( 0 );
		Animal result = entityManager.find( Animal.class, 1L, LockModeType.PESSIMISTIC_FORCE_INCREMENT );
		result.setName("updated");
		result = entityManager.merge( result );
		entityManager.flush();

		assertEquals( 2L, result.getVersion() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Animal" )
	@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
	abstract static class Animal {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		@Version
		private Long version;

		public Animal() {
		}

		public Animal(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getVersion() {
			return version;
		}
	}

	@Entity( name = "Dog" )
	static class Dog extends Animal {
		public Dog() {
		}

		public Dog(String name) {
			super( name );
		}
	}
}
