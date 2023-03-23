package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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

		Long interpretationVersion = 111L;

		Interpretation interpretation = new Interpretation();
		interpretation.uuid = 1L;

		InterpretationData interpretationData = new InterpretationData();
		interpretationData.interpretationVersion = new InterpretationVersion(interpretationVersion, interpretation.uuid);
		interpretationData.name = "TEST_NAME";
		entityManager.persist(interpretationData);
//		entityManager.flush();

		interpretation.interpretationData = interpretationData;
		interpretation.interpretationVersion = interpretationVersion;
		entityManager.persist(interpretation);
//		entityManager.flush();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
