package org.hibernate.bugs;

import java.util.*;

import jakarta.persistence.*;

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

		final TypedQuery<ParentEntity> query = entityManager.createQuery(
				"SELECT parent FROM ParentEntity parent " +
						"JOIN parent.children child " +
						"WHERE (child.name = 'child_a' OR child.name = 'child_b') " +
						"   OR(TYPE(child) = B_ChildEntity AND TREAT(parent.children AS B_ChildEntity).b = 'b')",
				ParentEntity.class
		);
		final List<ParentEntity> result = query.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "ParentEntity")
	public static class ParentEntity {
		@Id
		private long id;
		private String name;
		@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
		private List<AbstractChildEntity> children = new ArrayList<>();
		public ParentEntity() {
		}
		public ParentEntity(long id, String name) {
			this.id = id;
			this.name = name;
		}
		public long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public void addChild(AbstractChildEntity child) {
			children.add(child);
			child.setParent(this);
		}
	}

	@Entity( name = "AbstractChildEntity" )
	@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
	@DiscriminatorColumn( name = "discriminator", discriminatorType = DiscriminatorType.CHAR )
	@DiscriminatorValue( "X" )
	public static abstract class AbstractChildEntity {
		@Id
		private long id;
		private String name;
		@ManyToOne
		private ParentEntity parent;

		public AbstractChildEntity() {
		}

		public AbstractChildEntity(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setParent(ParentEntity parent) {
			this.parent = parent;
		}
	}

	@Entity( name = "A_ChildEntity" )
	@DiscriminatorValue( "A" )
	public static class A_ChildEntity extends AbstractChildEntity {
		private String a;

		public A_ChildEntity() {
		}

		public A_ChildEntity(long id, String name, String a) {
			super( id, name );
			this.a = a;
		}

		public String getA() {
			return a;
		}
	}

	@Entity( name = "B_ChildEntity" )
	@DiscriminatorValue( "B" )
	public static class B_ChildEntity extends AbstractChildEntity {
		private String b;

		public B_ChildEntity() {
		}

		public B_ChildEntity(long id, String name, String b) {
			super( id, name );
			this.b = b;
		}

		public String getB() {
			return b;
		}
	}
}
