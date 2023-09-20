package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

	private static final String TEXT = "text";
	private static final String QUERY = "select E from TestEntity E where text=:text";

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		// create entity
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		entityManager1.getTransaction().begin();
		TestEntity entity1 = new TestEntity();
		entity1.setId( 1L );
		entity1.setText( TEXT );
		entityManager1.persist( entity1 );
		entityManager1.getTransaction().commit();
		entityManager1.clear();
		entityManager1.close();

		// save query cache from managed entity
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		entityManager2.find( TestEntity.class, 1L );
		TestEntity entity2 = entityManager2.createQuery( QUERY, TestEntity.class ).setParameter( "text", TEXT ).setHint(
				"org.hibernate.cacheable",
				"true"
		).getSingleResult();
		assertEquals( TEXT, entity2.getText() );
		entityManager2.clear();
		entityManager2.close();

		// use query cache
		EntityManager entityManager3 = entityManagerFactory.createEntityManager();
		TestEntity entity3 = entityManager3.createQuery( QUERY, TestEntity.class ).setParameter( "text", TEXT ).setHint(
				"org.hibernate.cacheable",
				"true"
		).getSingleResult();
		assertEquals( TEXT, entity3.getText() ); // entity3.getText() is null
		entityManager3.close();
	}

	@Entity( name = "TestEntity" )
	@Cacheable
	public static class TestEntity {
		@Id
		private Long id;

		private String text;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
}
