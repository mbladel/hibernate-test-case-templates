package org.hibernate.bugs;

import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Table;

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



		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "MapContainerEntity" )
	@Table( name = "map_container_entity" )
	public static class MapContainerEntity {
		@Id
		private Long id;
		@OneToMany(mappedBy = "container")
		private Map<MapKeyEntity, MapValueEntity> map;
	}

	@Entity(name="MapKeyEntity")
	@Table( name = "map_key_entity" )
	public static class MapKeyEntity {
		@Id
		private Long id;
	}

	@Entity(name="MapValueEntity")
	@Table( name = "map_value_entity" )
	public static class MapValueEntity {
		@Id
		private Long id;
		@ManyToOne
		private MapContainerEntity container;
	}
}
