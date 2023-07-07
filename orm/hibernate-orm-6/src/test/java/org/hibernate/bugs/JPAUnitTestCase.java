package org.hibernate.bugs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		private Integer id;

		@OneToMany( mappedBy = "id.entityA" )
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

		public void setType(EntityA entityA, Integer type) {
			id.entityA = entityA;
			id.type = type;
		}

		public EmbeddedKey getId() {
			return id;
		}

		@Embeddable
		static class EmbeddedKey implements Serializable {
			@ManyToOne
			@JoinColumn( name = "entityaid" )
			private EntityA entityA;
			@Column( name = "type", updatable = false )
			private Integer type;
		}
	}

	@Test
	//Test method in test class
	public void testDeleteEntityA() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final EntityA entityA = new EntityA( 1 );
		final EntityB entityB = new EntityB();
		entityB.setType( entityA, 5 );
		entityA.addEntityB( entityB );
		entityManager.persist( entityA );
		entityManager.persist( entityB );

		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final EntityA found = entityManager.find( EntityA.class, 1 );
		found.entityBList.get( 0 ).getId().setEntityA( null );
//		entityManager.persist( entityB );
//		entityManager.remove( entityB );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
