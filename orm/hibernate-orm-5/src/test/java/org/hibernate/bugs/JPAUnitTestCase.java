package org.hibernate.bugs;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

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

		final SomeEntity someEntity = new SomeEntity( 1L );
		final SomeChildEntity child1 = new SomeChildEntity( 1L, someEntity );
		final SomeChildEntity child2 = new SomeChildEntity( 2L, someEntity );
//			someEntity.getChildEntities().addAll( List.of( child1, child2 ) );
		entityManager.persist( child1 );
		entityManager.persist( child2 );
		entityManager.persist( someEntity );
		entityManager.persist( new CompositePkEntity( new CompositePk( someEntity, "value" ), "initial-value" ) );

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

		final SomeEntity someEntity = entityManager.find( SomeEntity.class, 1L );
//		assertThat( someEntity.getChildEntities() ).hasSize( 2 );
		final CompositePkEntity compositePkEntity = new CompositePkEntity(
				new CompositePk( someEntity, "value" ),
				"new-value"
		);
		entityManager.merge( compositePkEntity );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "SomeChildEntity" )
	public static class SomeChildEntity {
		@Id
		private Long id;

		@ManyToOne
		@JoinColumn( name = "some_entity" )
		private SomeEntity someEntity;

		public SomeChildEntity() {
		}

		public SomeChildEntity(Long id, SomeEntity someEntity) {
			this.id = id;
			this.someEntity = someEntity;
		}
	}

	@Entity( name = "SomeEntity" )
	public static class SomeEntity {
		@Id
		private Long id;

		@OneToMany( mappedBy = "someEntity", cascade = CascadeType.ALL )
		private List<SomeChildEntity> childEntities = new ArrayList<>();

		public SomeEntity() {
		}

		public SomeEntity(Long id) {
			this.id = id;
		}

		public List<SomeChildEntity> getChildEntities() {
			return childEntities;
		}
	}

	@Embeddable
	public static class CompositePk implements Serializable {
		@ManyToOne(cascade = CascadeType.ALL)
		@JoinColumn( name = "some_entity_id", nullable = false )
		private SomeEntity someEntity;

		@Column( name = "some_pk_value", nullable = false )
		private String somePkValue;

		public CompositePk() {
		}

		public CompositePk(SomeEntity someEntity, String somePkValue) {
			this.someEntity = someEntity;
			this.somePkValue = somePkValue;
		}
	}

	@Entity( name = "CompositePkEntity" )
	public static class CompositePkEntity {
		@EmbeddedId
		private CompositePk id;

		@Column( name = "some_value" )
		private String someValue;

		public CompositePkEntity() {
		}

		public CompositePkEntity(CompositePk id, String someValue) {
			this.id = id;
			this.someValue = someValue;
		}

		public String getSomeValue() {
			return someValue;
		}
	}
}
