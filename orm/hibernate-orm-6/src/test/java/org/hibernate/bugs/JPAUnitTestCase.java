package org.hibernate.bugs;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SqlFragmentAlias;

import org.hibernate.testing.orm.junit.SessionFactoryScope;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

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

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void testUpdateQuery() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Session session = entityManager.unwrap( Session.class );
		session.enableFilter( "deleted_filter" ).setParameter( "deleted_name", "deleted" );
		session.createMutationQuery( "update UserEntity set name = 'updated'" ).executeUpdate();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "UserEntity" )
	@FilterDef( name = "deleted_filter", parameters = @ParamDef( name = "deleted_name", type = String.class ) )
	@Filter(
			name = "deleted_filter",
			condition = "( ({o}.deleted = true and {o}.name = :deleted_name) or ( {o}.deleted = false and exists(select u.id from \"user\" u WHERE u.id = {o}.id and u.name = {o}.name )  ) )",
			aliases = @SqlFragmentAlias( alias = "o", table = "\"user\"" ),
			deduceAliasInjectionPoints = false
	)
	@Table( name = "\"user\"" )
	public static class UserEntity {
		@Id
		private Long id;

		@Column( name = "deleted" )
		private boolean deleted;

		@Column( name = "name" )
		private String name;

		public UserEntity() {
		}

		public UserEntity(Long id, boolean deleted, String name) {
			this.id = id;
			this.deleted = deleted;
			this.name = name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
