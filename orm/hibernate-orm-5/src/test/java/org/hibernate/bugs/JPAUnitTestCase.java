package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;

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

		final List<Animal> animals = new ArrayList<>();
		animals.add( new Dog( 1L, "TestDog" ) );
		animals.add( new Cat( 2L, 2, Arrays.asList( new Kitten( "TestKitten" ) ) ) );
		animals.add( new Cat( 3L, 3, new ArrayList<>() ) );

		final Zoo zoo = new Zoo();
		zoo.setAnimals( animals );

		entityManager.persist( zoo );

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

		final List<Animal> animals = entityManager.createQuery(
				"select animal from Animal animal left join fetch animal.kittens",
				Animal.class
		).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();

		for ( final Animal animal : animals ) {
			if ( animal instanceof Cat ) {
				final Cat cat = (Cat) animal;
				final List<Kitten> kittens = cat.getKittens();
				assertThat( Hibernate.isInitialized( kittens ) ).isTrue();
				assertThat( kittens ).hasSizeBetween( 0, 1 );
			}
		}
	}

	@Entity( name = "Zoo" )
	public static class Zoo {
		@Id
		@GeneratedValue
		private Long id;

		@OneToMany( mappedBy = "zoo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true )
		private List<Animal> animals;

		public List<Animal> getAnimals() {
			return animals;
		}

		public void setAnimals(final List<Animal> animals) {
			this.animals = animals;
		}
	}

	@Entity( name = "Animal" )
	@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
	@DiscriminatorColumn( name = "type", discriminatorType = DiscriminatorType.STRING )
	public static class Animal {
		@Id
		private Long id;

		@ManyToOne
		@JoinColumn( name = "zooId" )
		private Zoo zoo;

		public Animal() {
		}

		public Animal(Long id) {
			this.id = id;
		}

		@Column( name = "type", nullable = false, insertable = false, updatable = false )
		private String type;

		public Long getId() {
			return id;
		}

		public String getType() {
			return type;
		}
	}

	@Entity( name = "Dog" )
	@DiscriminatorValue( "Dog" )
	public static class Dog extends Animal {
		@Column( name = "name" )
		private String name;

		public Dog() {
		}

		public Dog(Long id, String name) {
			super( id );
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}
	}

	@Entity( name = "Cat" )
	@DiscriminatorValue( "Cat" )
	public static class Cat extends Animal {

		@Column( name = "age" )
		private int age;

		@ManyToMany( cascade = CascadeType.ALL )
		@JoinTable( name = "Cat_Kitten", joinColumns = { @JoinColumn( name = "catId" ) }, inverseJoinColumns = { @JoinColumn( name = "kittenId" ) } )
		public List<Kitten> kittens = new ArrayList<>();

		public Cat(Long id, int age, List<Kitten> kittens) {
			super( id );
			this.age = age;
			this.kittens = kittens;
		}

		public Cat() {
		}

		public int getAge() {
			return age;
		}

		public void setAge(final int age) {
			this.age = age;
		}

		public List<Kitten> getKittens() {
			return kittens;
		}

		public void setKittens(final List<Kitten> kittens) {
			this.kittens = kittens;
		}
	}

	@Entity( name = "Kitten" )
	public static class Kitten {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		public Kitten() {
		}

		public Kitten(String name) {
			this.name = name;
		}
	}
}
