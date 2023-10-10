package org.hibernate.bugs;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.junit.Assert.assertEquals;

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


		final var infraOperation = new InfraOperationTestCaseEntity(
				UUID.randomUUID().toString(),
				InfraOperationStatus.PENDING,
				LocalDateTime.now(),
				new EnvironmentTestCaseEntity( UUID.randomUUID().toString() )
		);

		entityManager.persist( infraOperation );

		final var infraOperation2 = new InfraOperationTestCaseEntity(
				UUID.randomUUID().toString(),
				InfraOperationStatus.IN_PROGRESS,
				LocalDateTime.now(),
				new EnvironmentTestCaseEntity( UUID.randomUUID().toString() )
		);

		entityManager.persist( infraOperation );

		var query = """
					SELECT DISTINCT
						infraOperation.environment
					FROM
						InfraOperation infraOperation
					WHERE
						infraOperation.status In ('PENDING') AND
						infraOperation.environment.key not in (
						  SELECT
						    existingInfraOperation.environment.key
						  FROM
						    InfraOperation existingInfraOperation
						  WHERE
						  	existingInfraOperation.status IN ('IN_PROGRESS', 'ERROR')
						)
				""";

		final var environment = entityManager.createQuery( query, EnvironmentTestCaseEntity.class )
				.getSingleResult();

		assertEquals( environment.getKey(), infraOperation.getEnvironment().getKey() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
