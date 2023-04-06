package org.hibernate.bugs;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

		entityManager.unwrap( Session.class ).enableFilter( "filter_1" );

		entityManager.createQuery( "DELETE FROM UserEntity WHERE firstName = 'ciao'" ).executeUpdate();
		entityManager.createQuery( "DELETE FROM UserEntity" ).executeUpdate();

		entityManager.createQuery( "UPDATE UserEntity SET firstName = 'ciao' where id = 1" ).executeUpdate();
		entityManager.createQuery( "UPDATE UserEntity SET firstName = 'ciao'" ).executeUpdate();

		entityManager.createQuery( "from UserEntity where id = 1" ).getResultList();
		entityManager.createQuery( "from UserEntity" ).getResultList();

		entityManager.find( UserEntity.class, 1L );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@MappedSuperclass
	public static class BaseEntity {
		@Id
		@GeneratedValue
		private Long id;
	}

	@Entity( name = "UserEntity" )
	@Table( name = "users" )
	@SQLDelete( sql = "UPDATE users SET deleted = true WHERE id = ? and version = ?" )
//	@Where( clause = "deleted = false" )
	@FilterDef( name="filter_1", defaultCondition = "deleted = false")
	@Filter( name = "filter_1", condition = "deleted = false")
	public class UserEntity extends BaseEntity {
		private String firstName;

		private String lastName;

		@ManyToMany( fetch = FetchType.EAGER )
		@JoinTable(
				name = "users_roles",
				joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "id" ),
				inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id" ) )
		private List<RoleEntity> roles;

		@Column( insertable = false, updatable = false )
		private boolean deleted;
	}

	@Entity
	@Table( name = "role" )
	public class RoleEntity extends BaseEntity {
		@Column( length = 25, unique = true )
		private String name;

		@ManyToMany( mappedBy = "roles" )
		private List<UserEntity> users;
	}
}
