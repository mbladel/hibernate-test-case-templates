package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		final Phone phone = new Phone( 1L, "123" );
		final Person person = new Person( 1L, "Marco" );

		entityManager.persist( person );
		entityManager.persist( phone );

		// clear all from 1st and 2nd level cache
		entityManager.unwrap( Session.class ).getSessionFactory().getCache().evictAll();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void hhh123Test() throws Exception {
		loadPerson( 1L );
		loadPhone( 1L, false );
		attachPhoneToPerson( 1L, 1L );
		loadPhone( 1L, true );
	}

	private void loadPerson(long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		final Person person = entityManager.find( Person.class, id );
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void loadPhone(long id, boolean assertPerson) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		final Phone phone = entityManager.find( Phone.class, id );
		if ( assertPerson ) {
			assertNotNull( phone.getPerson() );
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void attachPhoneToPerson(long personId, long phoneId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		// N.B. without this the test passes
		entityManager.unwrap( Session.class ).setCacheMode( CacheMode.GET );
		final Person person = entityManager.find( Person.class, personId );
		final Phone phone = entityManager.find( Phone.class, phoneId );
		phone.setPerson( person );
		person.getPhones().add( phone );
		entityManager.persist( phone );
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Phone" )
	@Table( name = "PHONE" )
	@Cacheable
	@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
	public static class Phone {
		@Id
		@Column( name = "PHONEID", nullable = false, columnDefinition = "NUMBER(20)" )
		private Long id;

		@Column( name = "PHONENUMBER", nullable = false, columnDefinition = "VARCHAR2(256)" )
		private String number;

		@ManyToOne( fetch = FetchType.LAZY, targetEntity = Person.class, optional = true )
		@JoinColumn( name = "PERSONID", columnDefinition = "NUMBER(20)", nullable = true, referencedColumnName = "PERSONID" )
		private Person person;

		public Phone() {
		}

		public Phone(final long id, final String number) {
			setId( id );
			setNumber( number );
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public Person getPerson() {
			return person;
		}

		public void setPerson(Person person) {
			this.person = person;
		}

		@Override
		public String toString() {
			return "Phone{" +
					"id=" + id +
					", number='" + number + '\'' +
					", person=" + person +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			Phone phone = (Phone) o;
			return Objects.equals( id, phone.id ) && Objects.equals( number, phone.number ) && Objects.equals(
					person,
					phone.person
			);
		}

		@Override
		public int hashCode() {
			return Objects.hash( id, number, person );
		}
	}

	@Entity( name = "Person" )
	@Table( name = "PERSON" )
	@Cacheable
	@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
	public static class Person {

		@Id
		@Column( name = "PERSONID", nullable = false, columnDefinition = "NUMBER(20)" )
		private Long id;

		@Column( name = "NAME", nullable = false, columnDefinition = "VARCHAR2(256)" )
		private String name;

		@OneToMany( fetch = FetchType.LAZY, targetEntity = Phone.class, mappedBy = "person" )
		@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
		private final Set<Phone> phones = new HashSet<>();

		public Person() {
		}

		public Person(final long id, final String name) {
			setId( id );
			setName( name );
		}

		public Long getId() {
			return id;
		}

		public void setId(final Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Person{" +
					"id=" + id +
					", name='" + name +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			Person person = (Person) o;
			return Objects.equals( id, person.id ) && Objects.equals( name, person.name );
		}

		@Override
		public int hashCode() {
			return Objects.hash( id, name );
		}

		public void setName(final String name) {
			this.name = name;
		}

		public Set<Phone> getPhones() {
			return phones;
		}
	}
}
