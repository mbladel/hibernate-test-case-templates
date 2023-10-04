package org.hibernate.bugs;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
		// Do stuff...
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@MappedSuperclass
	public static abstract class BaseEntity<U extends AuditedEntity> {
		@Id
		private UUID id;

		@Embedded
		private U audit;
	}

	@Embeddable
	public static class ActionData {
		private LocalDateTime when;

		@ManyToOne
		private UserData user;
	}

	@Entity(name="UserData")
	public static class UserData {
		@Id
		private Long id;
		private String name;
	}

	@MappedSuperclass
	@Embeddable
	public static class AuditedEntity implements Serializable {
		@Embedded
		@AttributeOverrides( {
				@AttributeOverride( name = "when", column = @Column( name = "created_when" ) ),
				@AttributeOverride( name = "user.id", column = @Column( name = "created_user_id" ) ),
				@AttributeOverride( name = "user.name", column = @Column( name = "created_user_name" ) )
		} )
		private ActionData created;

		@Embedded
		@AttributeOverrides( {
				@AttributeOverride( name = "when", column = @Column( name = "modified_when" ) ),
				@AttributeOverride( name = "user.id", column = @Column( name = "modified_user_id" ) ),
				@AttributeOverride( name = "user.name", column = @Column( name = "modified_user_name" ) )
		} )
		private ActionData modified;
	}

	@Embeddable
	public static class LifeCycleAuditEntity extends AuditedEntity {
		@Embedded
		@AttributeOverrides( {
				@AttributeOverride( name = "when", column = @Column( name = "archived_when" ) ),
				@AttributeOverride( name = "user.id", column = @Column( name = "archived_user_id" ) ),
				@AttributeOverride( name = "user.name", column = @Column( name = "archived_user_name" ) )
		} )
		private ActionData archived;
	}

	@MappedSuperclass
	public static abstract class BaseNamedEntity<U extends AuditedEntity> extends BaseEntity<U> {

		@Basic( optional = false )
		@Column( name = "name", columnDefinition = "varchar(50)", length = 50, nullable = false )
		private String name;

		@Basic
		@Column( name = "description", columnDefinition = "varchar(1000)", length = 1000 )
		private String description;
	}

	@MappedSuperclass
	public static abstract class BaseNamedLimitedEntity<U extends AuditedEntity> extends BaseNamedEntity<U> {
		@Column( name = "is_editable", columnDefinition = "boolean", nullable = false )
		private Boolean isEditable = true;

		@Column( name = "is_deletable", columnDefinition = "boolean", nullable = false )
		private Boolean isDeletable = true;
	}


	@Entity( name = "Scenario" )
	@Table( name = "scenarios" )
	@DynamicUpdate
	public static class MainEntity extends BaseNamedLimitedEntity<LifeCycleAuditEntity> {
	}
}
