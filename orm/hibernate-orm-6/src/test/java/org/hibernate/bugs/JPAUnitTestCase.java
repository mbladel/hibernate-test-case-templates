package org.hibernate.bugs;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

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

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Role role = new Role();
		role.setName("TEST_ROLE");
		entityManager.persist(role);

		RoleScopePermission roleScopePermission1 = new RoleScopePermission();
		roleScopePermission1.setRoleName("TEST_ROLE");
		roleScopePermission1.setScopeName("test_scope_name_1");
		entityManager.persist(roleScopePermission1);

		RoleScopePermission roleScopePermission2 = new RoleScopePermission();
		roleScopePermission2.setRoleName("TEST_ROLE");
		roleScopePermission2.setScopeName("test_scope_name_2");
		entityManager.persist(roleScopePermission2);

		RoleScopePermission roleScopePermission3 = new RoleScopePermission();
		roleScopePermission3.setRoleName("TEST_ROLE");
		roleScopePermission3.setScopeName("test_scope_name_3");
		entityManager.persist(roleScopePermission3);




		RoleScopeOperationPermission roleScopeOperationPermission = new RoleScopeOperationPermission();
		roleScopeOperationPermission.setRoleName("TEST_ROLE");
		roleScopeOperationPermission.setScopeName("test_scope_name_2");
		roleScopeOperationPermission.setOperationName("SAVE");
		entityManager.persist(roleScopeOperationPermission);




		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission1 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission1.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission1.setScopeName("test_scope_name_1");
		roleScopeOperationObjectPermission1.setOperationName("CREATE");
		roleScopeOperationObjectPermission1.setObjectName("object_name_1");
		entityManager.persist(roleScopeOperationObjectPermission1);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission2 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission2.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission2.setScopeName("test_scope_name_1");
		roleScopeOperationObjectPermission2.setOperationName("UPDATE");
		roleScopeOperationObjectPermission2.setObjectName("object_name_1");
		entityManager.persist(roleScopeOperationObjectPermission2);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission3 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission3.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission3.setScopeName("test_scope_name_1");
		roleScopeOperationObjectPermission3.setOperationName("CREATE");
		roleScopeOperationObjectPermission3.setObjectName("object_name_2");
		entityManager.persist(roleScopeOperationObjectPermission3);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission4 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission4.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission4.setScopeName("test_scope_name_1");
		roleScopeOperationObjectPermission4.setOperationName("UPDATE");
		roleScopeOperationObjectPermission4.setObjectName("object_name_2");
		entityManager.persist(roleScopeOperationObjectPermission4);



		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission5 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission5.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission5.setScopeName("test_scope_name_2");
		roleScopeOperationObjectPermission5.setOperationName("CREATE");
		roleScopeOperationObjectPermission5.setObjectName("object_name_3");
		entityManager.persist(roleScopeOperationObjectPermission5);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission6 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission6.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission6.setScopeName("test_scope_name_2");
		roleScopeOperationObjectPermission6.setOperationName("UPDATE");
		roleScopeOperationObjectPermission6.setObjectName("object_name_3");
		entityManager.persist(roleScopeOperationObjectPermission6);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission7 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission7.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission7.setScopeName("test_scope_name_2");
		roleScopeOperationObjectPermission7.setOperationName("CREATE");
		roleScopeOperationObjectPermission7.setObjectName("object_name_4");
		entityManager.persist(roleScopeOperationObjectPermission7);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission8 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission8.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission8.setScopeName("test_scope_name_2");
		roleScopeOperationObjectPermission8.setOperationName("UPDATE");
		roleScopeOperationObjectPermission8.setObjectName("object_name_4");
		entityManager.persist(roleScopeOperationObjectPermission8);




		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission9 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission9.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission9.setScopeName("test_scope_name_3");
		roleScopeOperationObjectPermission9.setOperationName("CREATE");
		roleScopeOperationObjectPermission9.setObjectName("object_name_5");
		entityManager.persist(roleScopeOperationObjectPermission9);

		RoleScopeOperationObjectPermission roleScopeOperationObjectPermission10 = new RoleScopeOperationObjectPermission();
		roleScopeOperationObjectPermission10.setRoleName("TEST_ROLE");
		roleScopeOperationObjectPermission10.setScopeName("test_scope_name_3");
		roleScopeOperationObjectPermission10.setOperationName("UPDATE");
		roleScopeOperationObjectPermission10.setObjectName("object_name_5");
		entityManager.persist(roleScopeOperationObjectPermission10);



		// !!! All roleScopeOperationObjectPermission count is 10


		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		String hql = "FROM Role R WHERE R.name = :role_name";
		Query query = entityManager.createQuery( hql);
		query.setParameter("role_name","TEST_ROLE");

		List resultList = query.getResultList();

		Assert.assertEquals( 1, resultList.size());
		Role role = (Role) resultList.get(0);
		Assert.assertEquals(3, role.getScopePermissions().size()); // here error - role.getScopePermissions().size() == 10
	}
}
