package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		String sql = "insert into application (id, version, name) values (1, 0, 'app')";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into role (id, version, name, application_id) values(1, 0, 'manager', 1)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into role (id, version, name, application_id) values(2, 0, 'business', 1)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into role (id, version, name, application_id) values(3, 0, 'development', 1)";
		em.createNativeQuery(sql).executeUpdate();

		sql = "insert into approval_step_config (id) values(1)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into approval_step (id, version, approval_step_config_id, signer_role_id) values(1, 0, 1, 1)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into approval_step (id, version, approval_step_config_id, signer_role_id) values(2, 0, 1, 2)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "update role set approval_step_config_id=1, resp_approval_step_id=2 where id=2";
		em.createNativeQuery(sql).executeUpdate();

		sql = "insert into approval_step_config (id) values(2)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into approval_step (id, version, approval_step_config_id, signer_role_id) values(3, 0, 2, 1)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "insert into approval_step (id, version, approval_step_config_id, signer_role_id) values(4, 0, 2, 2)";
		em.createNativeQuery(sql).executeUpdate();
		sql = "update role set approval_step_config_id=2, resp_approval_step_id=4 where id=3";
		em.createNativeQuery(sql).executeUpdate();

		em.getTransaction().commit();
		em.close();
		// L2 cache should be empty at this point
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh17338Test() throws Exception {
		// NOK case: 1st find role with id=2, 2nd find role with id=3. All other cases OK
		EntityManager em = entityManagerFactory.createEntityManager();
		Role r2 = em.find(Role.class, 2L);
		// required to reproduce bug
		em.close();

		// L2 cache should contain loaded entities at this point

		em = entityManagerFactory.createEntityManager();
		// find crashes with "org.hibernate.PropertyValueException: Detached entity with generated id '1' has an uninitialized version value 'null' : org.hibernate.bugs.hhh17338.entities.Application.version"
		Role r3 = em.find(Role.class, 3L);
		em.close();
	}

}
