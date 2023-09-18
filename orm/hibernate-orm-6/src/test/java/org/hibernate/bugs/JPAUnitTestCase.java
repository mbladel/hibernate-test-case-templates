package org.hibernate.bugs;

import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;

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

		final ParentEntity parentEntity = new ParentEntity( "1", "one" );
		final ChildEntity childEntity = new ChildEntity( "1", "one" );
		entityManager.persist( parentEntity );
		entityManager.persist( childEntity );

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

		var query = entityManager.createQuery(
				"select p from ParentEntity p where p.first = :first",
				ParentEntity.class
		);
		query.setParameter( "first", "1" );

		var result = query.getSingleResult();
		System.out.println( result );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static class CompositeId implements Serializable {
		private String first;
		private String second;
	}

	@Entity( name = "ParentEntity" )
	@IdClass( CompositeId.class )
	public static class ParentEntity {
		@Id
		@Column( name = "col1" )
		private String first;

		@Id
		@Column( name = "col2" )
		private String second;

		@OneToOne( mappedBy = "parentEntity" )
		private ChildEntity childEntity;

		public ParentEntity() {
		}

		public ParentEntity(String first, String second) {
			this.first = first;
			this.second = second;
		}

		public ChildEntity getChildEntity() {
			return childEntity;
		}

		public void setChildEntity(ChildEntity childEntity) {
			this.childEntity = childEntity;
		}
	}

	@Entity( name = "ChildEntity" )
	@IdClass( CompositeId.class )
	public static class ChildEntity {
		@Id
		@Column( name = "col1" )
		private String first;

		@Id
		@Column( name = "col2" )
		private String second;

		@OneToOne
		@JoinColumn( name = "col1", referencedColumnName = "col1" )
		@JoinColumn( name = "col2", referencedColumnName = "col2" )
		private ParentEntity parentEntity;

		public ChildEntity() {
		}

		public ChildEntity(String first, String second) {
			this.first = first;
			this.second = second;
		}

		public ParentEntity getParentEntity() {
			return parentEntity;
		}

		public void setParentEntity(ParentEntity parentEntity) {
			this.parentEntity = parentEntity;
		}
	}
}
