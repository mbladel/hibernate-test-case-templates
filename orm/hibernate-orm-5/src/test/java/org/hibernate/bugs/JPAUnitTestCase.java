package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Table;

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
				new ParentEntityId( 1, new NestedEmbeddable( "1" ) ),
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
	public static class NestedEmbeddable {
		String name;

		public NestedEmbeddable() {
		}

		public NestedEmbeddable(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Embeddable
	public static class ParentEntityId implements Serializable {
		int id;

		@Embedded
		NestedEmbeddable nestedEmbeddable;

		public ParentEntityId() {
		}

		public ParentEntityId(int id, NestedEmbeddable nestedEmbeddable) {
			this.id = id;
			this.nestedEmbeddable = nestedEmbeddable;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public NestedEmbeddable getNestedEmbeddable() {
			return nestedEmbeddable;
		}

		public void setNestedEmbeddable(NestedEmbeddable nestedEmbeddable) {
			this.nestedEmbeddable = nestedEmbeddable;
		}
	}


	@Entity(name = "ParentEntity")
	@Table(name = "parent_entity")
	public static class ParentEntity {
		@EmbeddedId
		private ParentEntityId id;

		@OneToMany(cascade = CascadeType.ALL)
		@JoinColumns({
				@JoinColumn(name = "parent_entity_id", referencedColumnName = "id", nullable = false),
				@JoinColumn(name = "parent_entity_name", referencedColumnName = "name", nullable = false)
		})
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
