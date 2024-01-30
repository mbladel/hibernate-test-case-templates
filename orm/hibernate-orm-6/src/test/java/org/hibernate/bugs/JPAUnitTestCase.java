package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;

import static org.junit.Assert.assertEquals;

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

		SiteUser user = new SiteUser();

		CommunityProfile profile = new CommunityProfile( user, "myProfile" );
		user.setCommunityProfile( profile );

		Wallet wallet = new Wallet( user, "usd" );
		user.setWallet( wallet );

		entityManager.persist( wallet );
		entityManager.persist( profile );
		entityManager.persist( user );

		entityManager.flush();

		profile.setProfileName( "newProfile" );

		// first entity with inheritance
		// generates sub-select join as "... from SITE_USER su1_0 join (select * from PROFILE ..."
		// auto-flush not working for profile anymore
		int sizeInheritance = entityManager.createQuery(
				"select u from SiteUser u inner join u.communityProfile p where p.profileName = 'newProfile'",
				SiteUser.class
		).getResultList().size();

		assertEquals( 1, sizeInheritance );

		wallet.setWalletName( "newWallet" );

		// now entity without inheritance
		// generates standard join as "... from SITE_USER su1_0 join WALLET ..."
		int size = entityManager.createQuery(
				"select u from SiteUser u inner join u.wallet as wallet where wallet.walletName = 'newWallet'",
				SiteUser.class
		).getResultList().size();

		assertEquals( 1, size );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Profile" )
	@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
	@DiscriminatorColumn( name = "disc_col" )
	public static class Profile {

		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		@Column( name = "ID" )
		private Long id;

		public Profile() {
		}
	}

	@Entity( name = "CommunityProfile" )
	@DiscriminatorValue( "COMMUNITY" )
	public static class CommunityProfile extends Profile {
		@OneToOne( fetch = FetchType.LAZY, mappedBy = "communityProfile", optional = false )
		private SiteUser user;

		@Column( name = "PROFILE_NAME" )
		private String profileName;

		public CommunityProfile() {
		}

		public CommunityProfile(SiteUser user, String profileName) {
			this.user = user;
			this.profileName = profileName;
		}

		public SiteUser getUser() {
			return user;
		}

		public void setProfileName(String newProfile) {
			this.profileName = newProfile;
		}
	}

	@Entity( name = "SiteUser" )
	public static class SiteUser {
		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		@Column( name = "ID" )
		private Long id;

		@OneToOne( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
		@JoinColumn( name = "PROFILE_ID", nullable = false )
		private CommunityProfile communityProfile;

		@OneToOne( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
		@JoinColumn( name = "WALLET_ID", nullable = false )
		private Wallet wallet;

		public SiteUser() {
		}

		public Profile getCommunityProfile() {
			return communityProfile;
		}

		public void setCommunityProfile(CommunityProfile communityProfile) {
			this.communityProfile = communityProfile;
		}

		public void setWallet(Wallet wallet) {
			this.wallet = wallet;
		}
	}

	@Entity( name = "Wallet" )
	public static class Wallet {
		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		@Column( name = "ID" )
		private Long id;

		@OneToOne( fetch = FetchType.LAZY, mappedBy = "wallet", optional = false )
		private SiteUser user;

		@Column( name = "WALLET_NAME" )
		private String walletName;

		public Wallet() {
		}

		public Wallet(SiteUser user, String walletName) {
			this.user = user;
			this.walletName = walletName;
		}

		public void setWalletName(String walletName) {
			this.walletName = walletName;
		}
	}
}
