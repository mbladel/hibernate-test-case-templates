package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.hibernate.Hibernate;

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
		@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		protected NestedEntity nestedEntity;

		public NestedEntity getNestedEntity() {
			return nestedEntity;
		}
	}


	@Entity(name = "Leg")
	@DiscriminatorValue("LegBodyPart")
	public static class Leg extends BodyPart {
		@ManyToOne(fetch = FetchType.LAZY, optional = false)
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
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
		private Set<Leg> legs = new HashSet<>();

		public Set<Leg> getLegs() {
			return legs;
		}
	}
}
