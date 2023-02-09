package org.hibernate.bugs;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

		final User user = new User();
		final UserSettings userSettings = new UserSettings();
		userSettings.setUser( user );
		user.setUserSettings( userSettings );

		final UserTermsOfUse userTermsOfUse = new UserTermsOfUse();
		userTermsOfUse.setUser( user );
		user.setUserTermsOfUse( userTermsOfUse );

		entityManager.persist( user );

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

		List<User> all = entityManager.createQuery( "from User" ).getResultList();

		assertEquals( 1, all.size() );
		final User user = all.get( 0 );
		System.out.println( "------------------------" );
		System.out.println( "User.ID: " + user.getId() );
		System.out.println( "User.userSettings.ID: " + user.getUserSettings().getId() );
		System.out.println( "User.userTermsOfUse.ID: " + user.getUserTermsOfUse().getId() );
		System.out.println( "------------------------" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "User")
	@Table(name = "user_model")
	public static class User {
		@Id
		@GeneratedValue(generator = "system-uuid")
		@GenericGenerator(name = "system-uuid", strategy = "uuid")
		private String id;

		/**
		 * IMPORTENT: Here it will be set to EAGER, as after migrating to Spring Boot 3.0
		 * User Settings is not loaded and therefore. This leads to Impossible User delete
		 * as a User Settings still exists within DB.
		 */
		@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
		private UserSettings userSettings;

		@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
		private UserTermsOfUse userTermsOfUse;

		public String getId() {
			return id;
		}

		public UserSettings getUserSettings() {
			return userSettings;
		}

		public void setUserSettings(UserSettings userSettings) {
			this.userSettings = userSettings;
		}

		public UserTermsOfUse getUserTermsOfUse() {
			return userTermsOfUse;
		}

		public void setUserTermsOfUse(UserTermsOfUse userTermsOfUse) {
			this.userTermsOfUse = userTermsOfUse;
		}
	}

	@Entity(name = "UserSettings")
	public static class UserSettings {
		@Id
		@GeneratedValue(generator = "system-uuid")
		@GenericGenerator(name = "system-uuid", strategy = "uuid")
		private String id;

		@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
		@JoinColumn(name = "user_id", referencedColumnName = "id")
		private User user;

		private String language;

		private String lastActiveClientId;

		public String getId() {
			return id;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}

	@Entity(name = "UserTermsOfUse")
	@Table(name = "user_termsofuse")
	public static class UserTermsOfUse {
		@Id
		@GeneratedValue(generator = "system-uuid")
		@GenericGenerator(name = "system-uuid", strategy = "uuid")
		private String id;

		@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
		@JoinColumn(name = "user_id", referencedColumnName = "id")
		private User user;

		@Column(name = "accepted_termsofuse")
		private String acceptedTermsOfUse;

		public String getId() {
			return id;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}
