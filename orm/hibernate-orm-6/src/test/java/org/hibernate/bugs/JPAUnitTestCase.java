package org.hibernate.bugs;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
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
	public void hhh15865Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		ParentEntityIdWrapper idWrapper = new ParentEntityIdWrapper(
				new ParentEntityId(1)
		);
		ParentEntity parentEntity = new ParentEntity(
				idWrapper,
				List.of(new ChildEntity())
		);

		entityManager.merge( parentEntity );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity
	public static class ChildEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	@Embeddable
	public static class ParentEntityId {
		private int id;

		public ParentEntityId() {
		}

		public ParentEntityId(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	@Embeddable
	public static class ParentEntityIdWrapper implements Serializable {
		@Embedded
		private ParentEntityId parentEntityId;

		public ParentEntityIdWrapper() {
		}

		public ParentEntityIdWrapper(ParentEntityId parentEntityId) {
			this.parentEntityId = parentEntityId;
		}

		public ParentEntityId getParentEntityId() {
			return parentEntityId;
		}

		public void setParentEntityId(ParentEntityId parentEntityId) {
			this.parentEntityId = parentEntityId;
		}
	}

	@Entity
	public static class ParentEntity {
		@EmbeddedId
		private ParentEntityIdWrapper parentEntityIdWrapper;

		@OneToMany(cascade = CascadeType.ALL)
		@JoinColumn(name = "parent_entity_id", referencedColumnName = "id")
		private List<ChildEntity> childEntities;

		public ParentEntity() {
		}

		public ParentEntity(ParentEntityIdWrapper parentEntityIdWrapper, List<ChildEntity> childEntities) {
			this.parentEntityIdWrapper = parentEntityIdWrapper;
			this.childEntities = childEntities;
		}

		public ParentEntityIdWrapper getParentEntityIdWrapper() {
			return parentEntityIdWrapper;
		}

		public void setParentEntityIdWrapper(ParentEntityIdWrapper parentEntityIdWrapper) {
			this.parentEntityIdWrapper = parentEntityIdWrapper;
		}

		public List<ChildEntity> getChildEntities() {
			return childEntities;
		}

		public void setChildEntities(List<ChildEntity> childEntities) {
			this.childEntities = childEntities;
		}
	}
}
