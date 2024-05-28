package org.hibernate.bugs;

import java.util.List;

import org.hibernate.SessionFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

		DomainAccount domainAccount1 = new DomainAccount( "DOM_ACC_A" );
		entityManager.persist( domainAccount1 );
		DomainAccount domainAccount4 = new DomainAccount( "DOM_ACC_D" );
		entityManager.persist( domainAccount4 );

//            User user1 = new User("USER_ID_A", "Doe", "John");
//            session.persist(user1);
//            User user2 = new User("USER_ID_B", "Doe", "Jane");
//            session.persist(user2);
//            User user3 = new User("USER_ID_C", "Doe", "Helga");
//            session.persist(user3);

//            DatabaseAccount acc1 = new DatabaseAccount("A", "DB_A", user1);
//            session.persist(acc1);
//            DatabaseAccount acc2 = new DatabaseAccount("B", "DB_B", user2);
//            session.persist(acc2);
//            DatabaseAccount acc3 = new DatabaseAccount("C", "DB_C", user3);
//            session.persist(acc3);
		InternalAccount acc4 = new InternalAccount( "D", "INT_D" );
		acc4.getDomainAccounts().add( domainAccount4 );
		entityManager.persist( acc4 );

//            TestEntity e1 = new TestEntity("A", "Entity_A", acc2);
//            session.persist(e1);

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	private static final String ENTITYQUERY = "from TestEntity";
	private static final String ACCOUNTQUERY = "from Account";
	private static final String ACCOUNTBYNAMEQUERY = "from Account where name = :name";
	private static final String NAME = "INT_D";

	private static void executeQueryAccountFindAll(EntityManager session) {
		final List<Account> entities = session.createQuery( ACCOUNTQUERY, Account.class )
				.setHint( HINT_CACHEABLE, true )
//				.setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
				.getResultList();
		for ( Account a : entities ) {
			executeQueryByName( session, a.getLoginName() );
		}
	}

	private static void executeQueryByName(EntityManager session, String loginName) {
		final Account entity = session.createQuery( ACCOUNTBYNAMEQUERY, Account.class )
				.setParameter( "name", loginName )
				.setHint( HINT_CACHEABLE, true )
//				.setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
				.getSingleResult();
	}

	private static void executeQueryByGivenName(EntityManager session) {
		final Account entity = session.createQuery( ACCOUNTBYNAMEQUERY, Account.class )
				.setParameter( "name", NAME )
				.setHint( HINT_CACHEABLE, true )
//				.setCacheRegion(Grantee.class.getSimpleName() + ".QUERYCACHE")
				.getSingleResult();
		assertEquals( NAME, entity.getLoginName() );
		assertTrue( entity.hasDomainAccounts() );
	}

	@Test
	public void testNormalBehavior() {
		testQueryCache();
	}

	private void testQueryCache() {
//		scope.getSessionFactory().getCache().evictQueryRegions();
//		final StatisticsImplementor statistics = scope.getSessionFactory().getStatistics();
//		statistics.clear();

		entityManagerFactory.unwrap( SessionFactory.class ).getCache().evictQueryRegions();

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

//		scope.inTransaction(session -> {
		// executeQueryElementFindAll( entityManager );
		// entityManager.find( Account.class, "D" );
		// executeQueryAccountFindAll( entityManager );
//		});
		executeQueryByGivenName( entityManager );
		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

//        scope.inTransaction(QueryCacheExistingEntityInstanceTest::executeQueryByGivenName);
//        scope.inTransaction(QueryCacheExistingEntityInstanceTest::executeQueryByGivenName);
//		scope.inTransaction(session -> {
		executeQueryByGivenName( entityManager );
		executeQueryByGivenName( entityManager );
//		});
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
