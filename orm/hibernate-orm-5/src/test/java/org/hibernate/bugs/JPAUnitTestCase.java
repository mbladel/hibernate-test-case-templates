package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.engine.spi.BatchFetchQueue;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

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

		final LazySelectNode rootNode = new LazySelectNode();
		rootNode.id = 10;

		final LazySelectNode child1 = new LazySelectNode();
		child1.id = 20;

		final LazySelectNode child2 = new LazySelectNode();
		child2.id = 30;

		final LazySelectRoot root = new LazySelectRoot();
		root.id = 1;

		root.nodes.add( rootNode );
		root.nodes.add( child1 );
		root.nodes.add( child2 );
		rootNode.root = root;
		rootNode.children.add( child2 );
		rootNode.children.add( child1 );
		child1.root = root;
		child2.root = root;

		entityManager.persist( root );

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

		final SessionImplementor session = entityManager.unwrap( SessionImplementor.class );
		final EntityPersister persister = session.getEntityPersister( null, new LazySelectNode() );
		final EntityKey key1 = session.generateEntityKey( 10, persister );
		final EntityKey key2 = session.generateEntityKey( 20, persister );
		final EntityKey key3 = session.generateEntityKey( 30, persister );

		final LazySelectRoot in = entityManager.find( LazySelectRoot.class, 1 );

		final BatchFetchQueue batchFetchQueue = session.getPersistenceContextInternal().getBatchFetchQueue();
		assertThat( batchFetchQueue.getSubselect( key1 ), is( notNullValue() ) );
		assertThat( batchFetchQueue.getSubselect( key2 ), is( notNullValue() ) );
		assertThat( batchFetchQueue.getSubselect( key3 ), is( notNullValue() ) );



		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "root")
	public static class LazySelectRoot {
		@Id
		public Integer id;

		@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "root")
		public Set<LazySelectNode> nodes = new HashSet<>();
	}

	@Entity(name = "node")
	public static class LazySelectNode {

		@Id
		public Integer id;

		@ManyToOne(cascade = CascadeType.ALL)
		public LazySelectRoot root;

		@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
		@JoinTable(name = "RELATIONSHIPS")
		@Fetch(FetchMode.SUBSELECT)
		public final Set<LazySelectNode> children = new HashSet<>();
	}
}
