package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
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

	private static Entity1 prepareData() {
		Entity1 entity1 = new Entity1();

		Set<Entity2> entities2 = new HashSet<>();
		for ( int i = 0; i < 10; i++ ) {
			Entity2 entity2 = new Entity2();
			entity2.setParent( entity1 );
			entities2.add( entity2 );

			// Add deeper children only to the first and the last items
			if ( i == 0 || i == 9 ) {
				Set<Entity3> entities3 = new HashSet<>();
				for ( int j = 0; j < 5; j++ ) {
					Entity3 entity3 = new Entity3();
					entity3.setParent( entity2 );
					entities3.add( entity3 );
				}
				entity2.setChildren( entities3 );
			}
		}
		entity1.setChildren( entities2 );
		return entity1;
	}

	@Test
	public void hhh16043Test() throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
		Statistics statistics = entityManagerFactory.unwrap( SessionFactory.class ).getStatistics();
		em.getTransaction().begin();

		Entity1 entity1 = prepareData();

		em.persist( entity1 );
		em.flush();
		em.clear();

		statistics.clear();

		Entity1 fromDb = em.find( Entity1.class, entity1.getId() );

		System.out.println( fromDb );
		for ( Entity2 child2 : fromDb.getChildren() ) {
			System.out.println( "  " + child2 );
			for ( Entity3 child3 : child2.getChildren() ) {
				System.out.println( "    " + child3 );
			}
		}

		try {
			assertEquals( 3L, statistics.getPrepareStatementCount() );
		}
		finally {
			em.getTransaction().commit();
			em.close();
		}
	}

	@MappedSuperclass
	public static class AbstractEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE)
		private Long id;

		private String name;

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "#" + getId();
		}
	}


	@Entity
	public static class Entity1 extends AbstractEntity {
		@OneToMany(mappedBy = "parent", cascade = ALL, orphanRemoval = true)
		private Set<Entity2> children = new HashSet<>();

		public Set<Entity2> getChildren() {
			return children;
		}

		public void setChildren(Set<Entity2> children) {
			this.children = children;
		}
	}

	@Entity
	public static class Entity2 extends AbstractEntity {
		@ManyToOne(fetch = LAZY)
		private Entity1 parent;

		@OneToMany(mappedBy = "parent", cascade = ALL, orphanRemoval = true)
		Set<Entity3> children = new HashSet<>();

		public Entity1 getParent() {
			return parent;
		}

		public void setParent(Entity1 parent) {
			this.parent = parent;
		}

		public Set<Entity3> getChildren() {
			return children;
		}

		public void setChildren(Set<Entity3> children) {
			this.children = children;
		}
	}

	@Entity
	public static class Entity3 extends AbstractEntity {
		@ManyToOne(fetch = LAZY)
		private Entity2 parent;

		public Entity2 getParent() {
			return parent;
		}

		public void setParent(Entity2 parent) {
			this.parent = parent;
		}
	}

}
