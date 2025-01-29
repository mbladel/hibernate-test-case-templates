package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import org.hibernate.testing.orm.domain.gambit.EntityWithAggregateId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This template demonstrates how to develop a standalone test case for Hibernate ORM.  Although this is perfectly
 * acceptable as a reproducer, usage of ORMUnitTestCase is preferred!
 */
public class ORMStandaloneTestCase {

	private SessionFactoryImplementor sf;

	@Before
	public void setup() {
		StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder()
			// Add in any settings that are specific to your test. See resources/hibernate.properties for the defaults.
			.applySetting( "hibernate.show_sql", "true" )
			.applySetting( "hibernate.format_sql", "true" )
			.applySetting( "hibernate.hbm2ddl.auto", "update" );

		Metadata metadata = new MetadataSources( srb.build() )
		// Add your entities here.
			.addAnnotatedClass( EntityWithAggregateId.class )
			.addAnnotatedClass( Customer.class )
			.addAnnotatedClass( User.class )
			.addAnnotatedClass( Company.class )
			.buildMetadata();

		sf = (SessionFactoryImplementor) metadata.buildSessionFactory();

		EntityManager entityManager = sf.createEntityManager();
		entityManager.getTransaction().begin();

		customerList.forEach( entityManager::persist );
		entityWithAggregateIdList.forEach( entityManager::persist );
		userList.forEach( entityManager::persist );
		entityManager.getEntityManagerFactory().getCache().evictAll();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		final Session session = sf.openSession();
		session.getTransaction().begin();

		session.createQuery( "delete from Customer" ).executeUpdate();
		session.createQuery( "delete from EntityWithAggregateId" ).executeUpdate();
		session.createQuery( "delete from User" ).executeUpdate();

		session.getTransaction().commit();
		session.close();
	}

	private final List<Customer> customerList = List.of(
			new Customer( 1L, "Customer A" ),
			new Customer( 2L, "Customer B" ),
			new Customer( 3L, "Customer C" ),
			new Customer( 4L, "Customer D" ),
			new Customer( 5L, "Customer E" )
	);

	private final List<Long> customerIds = customerList.stream().map( Customer::getId ).collect( Collectors.toList() );

	private final List<EntityWithAggregateId> entityWithAggregateIdList = List.of(
			new EntityWithAggregateId( new EntityWithAggregateId.Key( "1", "1" ), "Entity A" ),
			new EntityWithAggregateId( new EntityWithAggregateId.Key( "2", "2" ), "Entity B" ),
			new EntityWithAggregateId( new EntityWithAggregateId.Key( "3", "3" ), "Entity C" ),
			new EntityWithAggregateId( new EntityWithAggregateId.Key( "4", "4" ), "Entity D" ),
			new EntityWithAggregateId( new EntityWithAggregateId.Key( "5", "5" ), "Entity E" )
	);

	private final List<EntityWithAggregateId.Key> entityWithAggregateIdKeys = entityWithAggregateIdList.stream().map(
			EntityWithAggregateId::getKey ).collect( Collectors.toList() );

	public final List<User> userList = List.of(
			new User( 1, null ),
			new User( 2, null ),
			new User( 3, null ),
			new User( 4, null ),
			new User( 5, null )
	);

	private final List<Integer> userIds = userList.stream().map( User::getId ).collect( Collectors.toList() );


	// (1) simple Id entity w/ pessimistic read lock
	@Test
	public void testMultiLoadSimpleIdEntityPessimisticReadLock() {
		EntityManager entityManager = sf.createEntityManager();
		entityManager.getTransaction().begin();
		final Session session = entityManager.unwrap( Session.class );

		List<Customer> customersLoaded = session.byMultipleIds( Customer.class )
				.with( new LockOptions( LockMode.PESSIMISTIC_READ ) )
				.multiLoad( customerIds );
		assertNotNull( customersLoaded );
		assertEquals( customerList.size(), customersLoaded.size() );
		customersLoaded.forEach( customer -> assertEquals(
				LockMode.PESSIMISTIC_READ,
				session.getCurrentLockMode( customer )
		) );


		entityManager.getTransaction().commit();
		entityManager.close();
	}

	// (2) composite Id entity w/ pessimistic read lock (one of the entities already in L1C)
	@Test
	public void testMultiLoadCompositeIdEntityPessimisticReadLockAlreadyInSession() {
		EntityManager entityManager = sf.createEntityManager();
		entityManager.getTransaction().begin();
		final Session session = entityManager.unwrap( Session.class );
		EntityWithAggregateId entityInL1C = session.find(
				EntityWithAggregateId.class,
				entityWithAggregateIdList.get( 0 ).getKey()
		);
		assertNotNull( entityInL1C );
		List<EntityWithAggregateId> entitiesLoaded = session.byMultipleIds( EntityWithAggregateId.class )
				.with( new LockOptions( LockMode.PESSIMISTIC_READ ) )
				.enableSessionCheck( true )
				.multiLoad( entityWithAggregateIdKeys );
		assertNotNull( entitiesLoaded );
		assertEquals( entityWithAggregateIdList.size(), entitiesLoaded.size() );
		entitiesLoaded.forEach( entity -> assertEquals(
				LockMode.PESSIMISTIC_READ,
				session.getCurrentLockMode( entity )
		) );
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	// (3) simple Id entity w/ pessimistic write lock (one in L1C & some in L2C)
	@Test
	public void testMultiLoadSimpleIdEntityPessimisticWriteLockSomeInL1CAndSomeInL2C() {
		Integer userInL2CId = userIds.get( 0 );
		Integer userInL1CId = userIds.get( 1 );
		EntityManager entityManager = sf.createEntityManager();
		entityManager.getTransaction().begin();


		User userInL2C = entityManager.find( User.class, userInL2CId );
		assertNotNull( userInL2C );
		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = sf.createEntityManager();
		entityManager.getTransaction().begin();
		final Session session = entityManager.unwrap( Session.class );
		assertTrue( session.getSessionFactory().getCache().containsEntity( User.class, userInL2CId ) );
		User userInL1C = session.find( User.class, userInL1CId );
		assertNotNull( userInL1C );
		List<User> usersLoaded = session.byMultipleIds( User.class ).enableSessionCheck( true ).with( new LockOptions(
				LockMode.PESSIMISTIC_WRITE ) ).multiLoad( userIds );
		assertNotNull( usersLoaded );
		assertEquals( userList.size(), usersLoaded.size() );
		usersLoaded.forEach( user -> assertEquals( LockMode.PESSIMISTIC_WRITE, session.getCurrentLockMode( user ) ) );
		entityManager.getTransaction().commit();
		entityManager.close();
	}


	@Entity(name = "Customer")
	public static class Customer {
		@Id
		private Long id;
		@Basic
		private String name;

		protected Customer() {
		}

		public Customer(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity(name = "User")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@Table(name = "`User`")
	static class User {
		@Id
		int id;


		@Version
		@Column(name = "OPTLOCK")
		private int version;


		@Column
		private String name;


		@ManyToOne(fetch = FetchType.LAZY)
		Company company;

		public User() {
		}

		public User(int id) {
			this.id = id;
		}

		public User(int id, Company company) {
			this.id = id;
			this.company = company;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company group) {
			this.company = group;
		}


		public int getVersion() {
			return version;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}


	@Entity(name = "Company")
	static class Company {
		@Id
		int id;

		String name;

		@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
		@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
		List<User> users = new ArrayList<User>();

		public Company() {
		}

		public Company(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

	}
}
