package org.hibernate.bugs;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final EntityGraph<?> entityGraph = entityManager.getEntityGraph( "test-graph" );
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final ArrayList<Predicate> predicates = new ArrayList<>();
		final CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery( Person.class );
		final Root<Person> root = criteriaQuery.from( Person.class );
		final Join<Object, Object> join = root.join( "address", JoinType.LEFT );
		predicates.add( criteriaBuilder.equal( join.get( "description" ), "test" ) );
		criteriaQuery.distinct( true ).where( predicates.toArray( new Predicate[0] ) ).orderBy( criteriaBuilder.asc(
				join.get( "id" ) ) );
		final TypedQuery<Person> typedQuery = entityManager.createQuery( criteriaQuery );
		typedQuery.setHint( "jakarta.persistence.fetchgraph", entityGraph );
		typedQuery.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity
	public static class Address {
		@Id
		@GeneratedValue
		private Integer id;

		private String description;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String value) {
			this.description = value;
		}
	}

	@Entity
	@NamedEntityGraph( name = "test-graph", attributeNodes = {
			@NamedAttributeNode( "address" ),
	} )
	public static class Person {
		@Id
		@GeneratedValue
		private Integer id;

		private String name;

		@ManyToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "address_id" )
		private Address address;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address number) {
			this.address = number;
		}
	}
}
