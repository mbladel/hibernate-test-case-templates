package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery(
				"select ef from BugEquipmentFeature ef" +
						"left outer join fetch ef.id.feature f" +
						"left outer join fetch f.id.equipmentClass", BugEquipmentFeature.class ).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "BugEquipment" )
	static class BugEquipment {
		@Id
		private Long id;
	}

	@Entity( name = "BugEquipmentClass" )
	static class BugEquipmentClass {
		@Id
		private String id;
	}

	@Entity( name = "BugEquipmentFeature" )
	static class BugEquipmentFeature {
		@EmbeddedId
		private BugEquipmentFeatureId id;
	}

	@Embeddable
	static class BugEquipmentFeatureId {
		@ManyToOne( cascade = {},        // cascade nothing
				fetch = FetchType.LAZY,
				optional = false )
		private BugEquipment equipment;

		@ManyToOne( cascade = {},        // cascade nothing
				fetch = FetchType.LAZY,
				optional = false )
		private BugFeature feature;
	}

	@Entity( name = "BugFeature" )
	static class BugFeature {
		@EmbeddedId
		private BugFeatureId id;
	}

	@Embeddable
	static class BugFeatureId {
		@ManyToOne( cascade = {},        // cascade nothing
				fetch = FetchType.LAZY,
				optional = false )
		private BugEquipmentClass equipmentClass;

		private String featureName;
	}
}
