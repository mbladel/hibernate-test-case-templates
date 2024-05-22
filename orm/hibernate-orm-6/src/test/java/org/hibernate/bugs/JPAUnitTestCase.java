package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final UserEntity ue = new UserEntity();
		ue.setName( "gavin" );
		final UserAuthorityEntity uae = new UserAuthorityEntity();
		ue.addUserAuthority( uae );
		uae.setUser( ue );
		uae.setAuthority( "blah" );
		entityManager.persist( ue );
		entityManager.persist( uae );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "UserAuthorityEntity" )
	@IdClass( UserAuthorityId.class )
	@Table( name = "user_authorities" )
	static class UserAuthorityEntity {
		@Id
		private Long userId;

		@Id
		private String authority;

		@ManyToOne
		@MapsId( "userId" )
		@PrimaryKeyJoinColumn( name = "user_id" )
		private UserEntity user;

		public void setUser(UserEntity user) {
			this.user = user;
		}

		public void setAuthority(String authority) {
			this.authority = authority;
		}
	}

	record UserAuthorityId(Long userId, String authority) {
	}

//	@Embeddable
//	static class UserAuthorityId {
//		private Long userId;
//		private String authority;
//
//		public UserAuthorityId() {
//		}
//
//		public UserAuthorityId(Long userId, String authority) {
//			this.userId = userId;
//			this.authority = authority;
//		}
//	}

	@Entity( name = "UserEntity" )
	@Table( name = "users" )
	static class UserEntity {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		Long id;

		String name;

		public void setName(String name) {
			this.name = name;
		}

		@OneToMany( /*cascade = { PERSIST, MERGE, REMOVE },*/ mappedBy = "user", orphanRemoval = true )
		private Set<UserAuthorityEntity> userAuthorities = new HashSet<>();

		public void addUserAuthority(UserAuthorityEntity userAuthority) {
			this.userAuthorities.add( userAuthority );
		}
	}
}
