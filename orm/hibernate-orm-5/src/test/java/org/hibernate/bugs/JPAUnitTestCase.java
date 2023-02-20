package org.hibernate.bugs;

import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;

import org.hibernate.Hibernate;

import org.junit.After;
import org.junit.Assert;
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
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void findTest() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createNativeQuery( "insert into EntityA(id) values(1)" )
				.executeUpdate();
		entityManager.createNativeQuery(
						"insert into AbstractEntityB(id,property_type, entitya_id) values(1,'DISC1', 1)" )
				.executeUpdate();
		entityManager.createNativeQuery(
						"insert into AbstractEntityB(id,property_type, entitya_id, parent_id) values(2,'DISC1', 1,1)" )
				.executeUpdate();
		entityManager.createNativeQuery( "insert into EntityB1(id) values(2)" ).executeUpdate();
		entityManager.createNativeQuery(
						"insert into AbstractEntityB(id,property_type, entitya_id) values(3,'DISC2', 1)" )
				.executeUpdate();
		entityManager.createNativeQuery( "insert into EntityB2(id) values(3)" ).executeUpdate();
		entityManager.createNativeQuery( "insert into EntityC(id,entityb_id) values(1,3)" ).executeUpdate();
		entityManager.createNativeQuery( "insert into EntityC(id,entityb_id) values(2,3)" ).executeUpdate();
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		EntityGraph<EntityA> graph = entityManager.createEntityGraph( EntityA.class );
		Attribute<EntityA, ?>[] attributes = new Attribute[] {
				EntityA_.properties
		};
		graph.addAttributeNodes( attributes );

		List<EntityA> entityAListWithGraph = entityManager.createQuery( "select a from EntityA a ", EntityA.class )
				.setHint( "javax.persistence.loadgraph", graph )
//				.setHint( "jakarta.persistence.loadgraph", graph )
				.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();

		for ( EntityA a : entityAListWithGraph ) {
			Assert.assertTrue( Hibernate.isInitialized( a.getProperties() ) );
			for ( AbstractEntityB b : a.getProperties() ) {
				if ( b instanceof EntityB1 ) {
					Assert.assertTrue( Hibernate.isInitialized( ( (EntityB1) b ).getChildren() ) );
				}
				else if ( b instanceof EntityB2 ) {
					Assert.assertTrue( Hibernate.isInitialized( ( (EntityB2) b ).getValues() ) );
				}
			}
		}


	}
}
