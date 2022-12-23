package org.hibernate.bugs;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

		ParentEntity parentEntity = new ParentEntity(
				new ChildEntityWrapper( Collections.singletonList( new ChildEntity() ) )
		);
		entityManager.persist( parentEntity );

		entityManager.getTransaction().commit();
		entityManager.close();

		// delete

		EntityManager em2 = entityManagerFactory.createEntityManager();
		em2.getTransaction().begin();

		ParentEntity foundEntity = em2.find( ParentEntity.class, 1 );
		em2.remove( foundEntity );

		em2.getTransaction().commit();
		em2.close();
	}

	@Entity
	public static class ChildEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;

		public ChildEntity() {
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	@Embeddable
	public static class ChildEntityWrapper {
		@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
		@JoinColumn(name = "parent_entity_id", referencedColumnName = "id")
		private List<ChildEntity> childEntities;

		public ChildEntityWrapper() {
		}

		public ChildEntityWrapper(List<ChildEntity> childEntities) {
			this.childEntities = childEntities;
		}

		public List<ChildEntity> getChildEntities() {
			return childEntities;
		}

		public void setChildEntities(List<ChildEntity> childEntities) {
			this.childEntities = childEntities;
		}
	}

	@Entity
	public static class ParentEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;

		@Embedded
		private ChildEntityWrapper childEntityWrapper = new ChildEntityWrapper();

		public ParentEntity() {
		}

		public ParentEntity(ChildEntityWrapper childEntityWrapper) {
			this.childEntityWrapper = childEntityWrapper;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public ChildEntityWrapper getChildEntityWrapper() {
			return childEntityWrapper;
		}

		public void setChildEntityWrapper(ChildEntityWrapper childEntityWrapper) {
			this.childEntityWrapper = childEntityWrapper;
		}
	}
}
