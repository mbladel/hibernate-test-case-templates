package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.TenantId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
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
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		entityManager.getTransaction().begin();
//
//		entityManager.getTransaction().commit();
//		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		final Session session = entityManagerFactory.unwrap( SessionFactory.class )
				.withOptions()
				.tenantIdentifier( 1 )
				.openSession();
		session.getTransaction().begin();

		session.createQuery( "select count(t.id) from DxEntity t where type(t) = TemporaryHazard", Long.class ).getResultList();

		session.getTransaction().commit();
		session.close();
	}


	@MappedSuperclass
	static class MultiTenantEntity {
		@Id
		private Long id;

		@TenantId
		private Integer tenantId;
	}

	@Entity( name = "DxEntity" )
	@Table( name = "dx_entity" )
	@Inheritance( strategy = InheritanceType.JOINED )
	@DiscriminatorColumn( name = "table_name", discriminatorType = DiscriminatorType.STRING )
	@SQLRestriction( "deleted is null" )
	static class DxEntity extends MultiTenantEntity {
		private String dxEntityProp;

		private boolean deleted;
	}

	@Entity( name = "TemporaryHazard" )
	@Table( name = "dx_temporary_hazard" )
	@DiscriminatorValue( "dx_temporary_hazard" )
	static class TemporaryHazard extends DxEntity {
		private String temporaryHazardProp;
	}
}
