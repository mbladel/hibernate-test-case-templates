package org.hibernate.bugs;

import java.util.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

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
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final ContainingEntity entity1 = new ContainingEntity();
		entity1.setId( 1 );

		final ContainingEntity containingEntity1 = new ContainingEntity();
		containingEntity1.setId( 2 );

		entity1.setChild( containingEntity1 );
		containingEntity1.setParent( entity1 );

		final ContainedEntity containedEntity = new ContainedEntity();
		containedEntity.setId( 3 );

		entityManager.persist( containingEntity1 );
		entityManager.persist( entity1 );
		entityManager.persist( containedEntity );

		containingEntity1.setContained( containedEntity );
		containedEntity.setContaining( containingEntity1 );

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

		final ContainingEntity containing = entityManager.find( ContainingEntity.class, 2 );
		final ContainedEntity containedEntity = containing.getContained();

		entityManager.remove( containedEntity );
		entityManager.flush();

		final ContainingEntity parent = containing.getParent();
		final ContainingEntity child = parent.getChild();
		assertThat( child.getId() ).isEqualTo( 2 );
		assertThat( child ).isSameAs( containing );
		assertThat( child.getContained() ).isNull();

		entityManager.getTransaction().commit();
		entityManager.close();
	}


	@Entity( name = "ContainingEntity" )
	static class ContainingEntity {
		@Id
		private Integer id;

		@OneToOne( fetch = FetchType.LAZY )
		private ContainingEntity parent;

		@OneToOne( mappedBy = "parent", fetch = FetchType.LAZY )
		private ContainingEntity child;

		@OneToOne( mappedBy = "containing" )
		private ContainedEntity contained;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public ContainingEntity getParent() {
			return parent;
		}

		public void setParent(ContainingEntity parent) {
			this.parent = parent;
		}

		public ContainingEntity getChild() {
			return child;
		}

		public void setChild(ContainingEntity child) {
			this.child = child;
		}

		public ContainedEntity getContained() {
			return contained;
		}

		public void setContained(ContainedEntity contained) {
			this.contained = contained;
		}

	}

	@Entity( name = "ContainedEntity" )
	static class ContainedEntity {
		@Id
		private Integer id;

		@OneToOne( fetch = FetchType.LAZY )
		@JoinColumn( name = "containing" )
		private ContainingEntity containing;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public ContainingEntity getContaining() {
			return containing;
		}

		public void setContaining(ContainingEntity containing) {
			this.containing = containing;
		}
	}
}
