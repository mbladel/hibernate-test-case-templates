package org.hibernate.bugs;

import java.util.List;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.hibernate.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Session session = entityManager.unwrap( Session.class );
		List myEntities = session.getNamedNativeQuery( "MyEntity.findMyEntity" )
				.addEntity( "ame", MyEntity.class )
				.getResultList();
		assertNotNull( myEntities );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@NamedNativeQueries(value = {
			@NamedNativeQuery(
					name = "MyEntity.findMyEntity",
					query = "WITH RECURSIVE all_my_entities AS " +
							"(SELECT me.* FROM my_entity me) " +
							"SELECT {ame.*} FROM all_my_entities ame")
	})
	@Entity
	@Table(name = "my_entity")
	@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "entity_type")
	public static class MyEntity<E extends MyEntity> {

		@Id
		@GeneratedValue
		private Long id;

		private String name;

	}

	@Entity
	public static class System {

		@Id
		@GeneratedValue
		private Long id;
	}

	@Entity
	@DiscriminatorValue("SUPPORTING_ASSET_TYPE")
	public static class EntityType extends MyEntity<EntityType> {

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "system_id")
		System system;
	}
}
