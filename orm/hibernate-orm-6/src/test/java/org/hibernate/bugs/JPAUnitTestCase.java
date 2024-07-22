package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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

		Person parent = new Person();
		parent.setId( 2L );

		Person child = new Person();
		child.setId( 1L );
		parent.setChild( child );

		// Person mergedParent = entityManager.merge(parent);

		entityManager.persist( parent );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Person" )
	static class Person {
		@Id
		private Long id;

		@OneToOne( cascade = CascadeType.PERSIST, fetch = FetchType.EAGER )
		@JoinTable(
				name = "Parent_Child",
				joinColumns = {
						@JoinColumn( name = "parent_id",
								referencedColumnName = "id",
								nullable = false,
								unique = true,
								foreignKey = @ForeignKey( name = "none", value = ConstraintMode.NO_CONSTRAINT ) )
				},
				inverseJoinColumns = {
						@JoinColumn( name = "child_id",
								referencedColumnName = "id",
								nullable = false,
								unique = true,
								foreignKey = @ForeignKey( name = "none", value = ConstraintMode.NO_CONSTRAINT ) )
				}
		)
		private Person child;

		@OneToOne( cascade = CascadeType.MERGE, mappedBy = "child" )
		private Person parent;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Person getChild() {
			return child;
		}

		public void setChild(Person child) {
			this.child = child;
		}
	}
}
