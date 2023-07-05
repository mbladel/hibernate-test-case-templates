package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

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

		final CustomerComputerSystem customerComputer = new CustomerComputerSystem();
		entityManager.persist( customerComputer );
		final CustomerCompany customerCompany = new CustomerCompany( 1L );
		customerCompany.addComputerSystem( customerComputer );
		entityManager.persist( customerCompany );

		final DistributorComputerSystem distributorComputer = new DistributorComputerSystem();
		entityManager.persist( distributorComputer );
		final DistributorCompany distributorCompany = new DistributorCompany( 2L );
		distributorCompany.addComputerSystem( distributorComputer );
		entityManager.persist( distributorCompany );


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

		entityManager.find( CustomerCompany.class, 1L ).getComputerSystems().size();

		entityManager.getTransaction().commit();
		entityManager.close();
	}


	@Entity( name = "Company" )
	@Inheritance( strategy = InheritanceType.JOINED )
	public static abstract class Company {
		@Id
		private long id;

		public Company() {
		}

		public Company(long id) {
			this.id = id;
		}
	}

	@Entity( name = "CustomerCompany" )
	public static class CustomerCompany extends Company {
		@OneToMany( mappedBy = "owner" )
		private List<CustomerComputerSystem> computerSystems = new ArrayList<>();

		public CustomerCompany() {
		}

		public CustomerCompany(long id) {
			super( id );
		}

		public void addComputerSystem(CustomerComputerSystem computerSystem) {
			computerSystems.add( computerSystem );
			computerSystem.setOwner( this );
		}

		public List<CustomerComputerSystem> getComputerSystems() {
			return computerSystems;
		}
	}

	@Entity( name = "DistributorCompany" )
	public static class DistributorCompany extends Company {
		@OneToMany( mappedBy = "owner" )
		private List<DistributorComputerSystem> computerSystems = new ArrayList<>();

		public DistributorCompany() {
		}

		public DistributorCompany(long id) {
			super( id );
		}

		public void addComputerSystem(DistributorComputerSystem computerSystem) {
			computerSystems.add( computerSystem );
			computerSystem.setOwner( this );
		}

		public List<DistributorComputerSystem> getComputerSystems() {
			return computerSystems;
		}
	}

	@Entity( name = "ComputerSystem" )
	@Table( name = "computer_system" )
	@Inheritance( strategy = InheritanceType.JOINED )
	public static abstract class ComputerSystem {
		@Id
		@GeneratedValue
		private long id;

		@ManyToOne
		@JoinColumn( name = "owner_id" )
		protected Company owner = null;

		public void setOwner(Company owner) {
			this.owner = owner;
		}
	}

	@Entity( name = "CustomerComputerSystem" )
	public static class CustomerComputerSystem extends ComputerSystem {
	}

	@Entity( name = "DistributorComputerSystem" )
	public static class DistributorComputerSystem extends ComputerSystem {
	}
}
