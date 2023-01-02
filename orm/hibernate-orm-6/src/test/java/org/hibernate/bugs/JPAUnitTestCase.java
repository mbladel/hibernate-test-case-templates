package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
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
import jakarta.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	public void hhh15866Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		ChildEntity childEntity = new ChildEntity();

		ParentEntity parentEntity = new ParentEntity(
				new ParentEntityId( 1 ),
				Collections.singletonList( childEntity )
		);

		entityManager.persist( parentEntity );

		assertEquals( 1, parentEntity.getChildEntities().size() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "ChildEntity")
	@Table(name = "child_entity")
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
	public static class ParentEntityId implements Serializable {
		int id;

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


	@Entity(name = "ParentEntity")
	@Table(name = "parent_entity")
	public static class ParentEntity {
		@EmbeddedId
		private ParentEntityId id;

		@OneToMany(cascade = CascadeType.ALL)
		@JoinColumn(name = "parent_entity_id", referencedColumnName = "id", nullable = false)
		private List<ChildEntity> childEntities;

		public ParentEntity() {
		}

		public ParentEntity(ParentEntityId id, List<ChildEntity> childEntities) {
			this.id = id;
			this.childEntities = childEntities;
		}

		public ParentEntityId getId() {
			return id;
		}

		public void setId(ParentEntityId id) {
			this.id = id;
		}

		public List<ChildEntity> getChildEntities() {
			return childEntities;
		}

		public void setChildEntities(List<ChildEntity> childEntities) {
			this.childEntities = childEntities;
		}
	}
}
