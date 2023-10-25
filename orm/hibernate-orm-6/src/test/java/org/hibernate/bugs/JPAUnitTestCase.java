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
	public void hhh17280Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();


		var infraOperation = new InfraOperationTestCaseEntity();
		infraOperation.setId( UUID.randomUUID().toString() );
		infraOperation.setStatus( InfraOperationStatus.PENDING );
		infraOperation.setCreatedTimestamp( LocalDateTime.now() );
		infraOperation.setEnvironment(
				new EnvironmentTestCaseEntity( new EnvironmentKey( UUID.randomUUID().toString() ) )
		);

		entityManager.persist( infraOperation );

		infraOperation = new InfraOperationTestCaseEntity();
		infraOperation.setId( UUID.randomUUID().toString() );
		infraOperation.setStatus( InfraOperationStatus.IN_PROGRESS );
		infraOperation.setCreatedTimestamp( LocalDateTime.now() );
		infraOperation.setEnvironment(
				new EnvironmentTestCaseEntity( new EnvironmentKey( UUID.randomUUID().toString() ) )
		);


		entityManager.persist( infraOperation );

		var query = """
					SELECT DISTINCT
						infraOperation.environment
					FROM
						InfraOperationTestCase infraOperation
					WHERE
						infraOperation.status In ('PENDING') AND
						infraOperation.environment.key not in (
						  SELECT
						    existingInfraOperation.environment.key
						  FROM
						    InfraOperationTestCase existingInfraOperation
						  WHERE
						  	existingInfraOperation.status IN ('IN_PROGRESS', 'ERROR')
						)
				""";

		EnvironmentTestCaseEntity environment = entityManager.createQuery( query, EnvironmentTestCaseEntity.class )
				.getSingleResult();

		assertEquals( environment.getKey(), infraOperation.getEnvironment().getKey() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
