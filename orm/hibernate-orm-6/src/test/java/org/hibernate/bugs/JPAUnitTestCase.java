package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
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

		final Person person = new Person();
		person.setName( "initialName" );

		entityManager.persist( person );

//		entityManager.getTransaction().commit();
//		entityManager.close();
//
//		// enter new transaction
//
//		entityManager = entityManagerFactory.createEntityManager();
//		entityManager.getTransaction().begin();

		final Person foundPerson = entityManager.find( Person.class, 1L );
		assertEquals( "initialName", foundPerson.getName() );
		assertEquals( 1L, foundPerson.getId() );

		foundPerson.setName( "changedName" );

		entityManager.getTransaction().commit();
		entityManager.close();

		// enter new transaction

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Person newPerson = entityManager.find( Person.class, 1L );
		assertEquals( "changedName", newPerson.getName() );
		assertEquals( 1L, newPerson.getId() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void testQueryJoinFetch() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Person person = new Person();
		person.setName( "initialName" );
		person.getLegs().add( new Leg( "left leg", person, new NestedEntity( "nested in left leg" ) ) );
		entityManager.persist( person );

		entityManager.getTransaction().commit();
		entityManager.close();

		// enter new transaction

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		String query = "select person " +
				"from Person person " +
				"left join fetch person.legs as leg " +
				"left join fetch leg.nestedEntity " +
				"where person.id = :id";

		final Person foundPerson = entityManager.createQuery( query, Person.class )
				.setParameter( "id", 1L )
				.getSingleResult();
		assertEquals( 1L, foundPerson.getId() );
		final Leg leg = foundPerson.getLegs().iterator().next();
		assertEquals( "left leg", leg.getName() );
		assertTrue( Hibernate.isInitialized( leg.getNestedEntity() ) );
		assertEquals( "nested in left leg", leg.getNestedEntity().getName() );


		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@MappedSuperclass
	public abstract static class BaseEntity {
		@Id
		@GeneratedValue
		private Long id;

		protected String name;

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}


	@Entity(name = "BodyPart")
	@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	@DiscriminatorColumn(name = "discriminator")
	@Table(name = "body_part")
	public abstract static class BodyPart extends BaseEntity {
		@OneToOne(fetch = FetchType.LAZY  , cascade = CascadeType.ALL)
		protected NestedEntity nestedEntity;

		public NestedEntity getNestedEntity() {
			return nestedEntity;
		}
	}


	@Entity(name = "Leg")
	@DiscriminatorValue("LegBodyPart")
	public static class Leg extends BodyPart {
		@ManyToOne(fetch = FetchType.EAGER, optional = false)
		private Person person;

		public Leg() {
		}

		public Leg(String name, Person person, NestedEntity nestedEntity) {
			this.name = name;
			this.person = person;
			this.nestedEntity = nestedEntity;
		}

		public Person getPerson() {
			return person;
		}
	}

	@Entity(name = "NestedEntity")
	public static class NestedEntity extends BaseEntity {
		public NestedEntity() {
		}

		public NestedEntity(String name) {
			this.name = name;
		}
	}

	@Entity(name = "Person")
	@Table(name = "person")
	public static class Person extends BaseEntity {
		@OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
		private Set<Leg> legs = new HashSet<>();

		public Set<Leg> getLegs() {
			return legs;
		}
	}
}
