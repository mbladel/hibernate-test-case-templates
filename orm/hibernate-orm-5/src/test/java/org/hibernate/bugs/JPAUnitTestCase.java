package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.RowId;

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
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void errorDuringUpdatingSameEntityFieldInSameTransaction() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		SimpleTestEntity testEntity = new SimpleTestEntity( 1L, "TestStatus" );
		entityManager.persist( testEntity );
		entityManager.flush();
//		entityManager.getTransaction().commit();
		//Second transaction
//		entityManager.getTransaction().begin();
		testEntity.setStatus( "NewStatus" );
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void errorDuringUpdatingRelatedEntityFieldInDifferentTransactions() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		UUID testId = UUID.randomUUID();
		RelatedMainEntity testEntity = new RelatedMainEntity( testId, new RelatedSecondaryEntity( testId, "OldStatus" ) );
		//First transaction
		entityManager.getTransaction().begin();
		entityManager.persist( testEntity );
		entityManager.flush();
//		entityManager.getTransaction().commit();
		//Second transaction
//		entityManager.getTransaction().begin();
		RelatedMainEntity savedEntity = entityManager.find( RelatedMainEntity.class, testId );
		savedEntity.setStatus( "NewStatus" );
		entityManager.flush();
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}

@Entity
@RowId( "_rowid_" )
class SimpleTestEntity {
	@Id
	@Column( name = "id" )
	public Long id;
	@Column( name = "status" )
	public String status;

	public SimpleTestEntity() {
	}

	public SimpleTestEntity(Long id, String status) {
		this.id = id;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

@Entity
@RowId( "_rowid_" )
class RelatedMainEntity {
	@Id
	@Column( name = "id" )
	public UUID id;
	@OneToOne( cascade = { CascadeType.ALL } )
	@MapsId
	public RelatedSecondaryEntity secondary;

	public RelatedMainEntity() {
	}

	public RelatedMainEntity(UUID id, RelatedSecondaryEntity secondary) {
		this.id = id;
		this.secondary = secondary;
	}

	public String getStatus() {
		return secondary.status;
	}

	public void setStatus(String newStatus) {
		secondary.status = newStatus;
	}
}

@Entity
@RowId( "_rowid_" )
class RelatedSecondaryEntity {
	@Id
	@Column( name = "id" )
	public UUID id;
	@Column( name = "status" )
	public String status;

	public RelatedSecondaryEntity() {
	}

	public RelatedSecondaryEntity(UUID id, String status) {
		this.id = id;
		this.status = status;
	}
}