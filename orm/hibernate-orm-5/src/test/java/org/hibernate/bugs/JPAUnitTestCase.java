package org.hibernate.bugs;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.query.Query;

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

		final TypedQuery<EntityA> query = entityManager.createQuery(
				"select a from EntityA a where a.amount = " +
				"(select max(b.amount) from EntityA b where a.reference.name = 'entitya_1' and b.reference is not null)",
				EntityA.class
		);
		final List<EntityA> actual = query.getResultList();
//		assertThat( actual ).hasSize( 1 );
//		assertThat( actual.get( 0 ).getName() ).isEqualTo( "entitya_1" );

//		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<EntityA> query = cb.createQuery( EntityA.class );
//		final Root<EntityA> root = query.from( EntityA.class );
//
//		final Subquery<String> subquery = query.subquery( String.class );
//		final Root<EntityA> subRoot = subquery.from( EntityA.class );
//		subquery.select( subRoot.get( "name" ) ).where( cb.and(
//				cb.isNull( root.get( "reference" ) ),
//				cb.isNull( subRoot.get( "reference" ) )
//		) );
//
//		query.select( root ).where( cb.equal( root.get( "name" ), subquery ) );
//		final List<EntityA> actual = entityManager.createQuery( query ).getResultList();
//		assertThat( actual ).hasSize( 1 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		private Integer amount;

		@ManyToOne
		@JoinColumn( name = "reference" )
		private EntityA reference;

		public EntityA() {
		}

		public EntityA(String name, EntityA reference) {
			this.name = name;
			this.reference = reference;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public EntityA getReference() {
			return reference;
		}
	}
}
