package org.hibernate.bugs;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

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
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		private Integer id;

		@OneToMany
//		@Fetch( FetchMode.JOIN )
		@JoinColumn( name = "entityaid" )
		private final List<EntityB> entityBList = new ArrayList<>();

		public EntityA() {
		}

		public EntityA(Integer id) {
			this.id = id;
		}

		public void addEntityB(final EntityB entityB) {
			entityBList.add( entityB );
		}
	}

	@Entity( name = "EntityB" )
	public static class EntityB {

		@EmbeddedId
		private EmbeddedKey id = new EmbeddedKey();

		public void setType(Integer entityAId, Integer type) {
			id.entityAId = entityAId;
			id.type = type;
		}

		@Embeddable
		static class EmbeddedKey implements Serializable {
			@Column( name = "entityaid", updatable = false )
			private Integer entityAId;
			@Column( name = "type", updatable = false )
			private Integer type;
		}
	}

	@Test
	//Test method in test class
	public void testDeleteEntityA() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		final EntityA entityA = new EntityA( 1 );
		final EntityB entityB = new EntityB();
		entityB.setType( 1, 5 );
		entityA.addEntityB( entityB );
		entityManager.getTransaction().begin();
		entityManager.persist( entityA );
		entityManager.persist( entityB );
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		entityManager.remove( entityA ); //Exception throwed here
		entityManager.getTransaction().commit();

		entityManager.close();
	}
}
