package org.hibernate.bugs;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;

import org.hibernate.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );

		{
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			Level1 level1 = new Level1();
			level1.setId( 1 );

			DerivedLevel2 level2 = new DerivedLevel2();
			level2.setId( 2 );

			Level3 level3 = new Level3();
			level3.setId( 3 );

			level1.setLevel2Child( level2 );
			level2.setLevel1Parent( level1 );
			level2.setLevel3Child( level3 );
			level3.setLevel2Parent( level2 );

			level3.setName( "initial-name" );

			entityManager.persist( level1 );
			entityManager.persist( level2 );
			entityManager.persist( level3 );

			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		{
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			Level3 level3 = entityManager.unwrap( Session.class ).load( Level3.class, 3 );
			Level2 level2 = level3.getLevel2Parent();
			assertThat( level2 ).isNotNull();
			final Level3 level3Child = level2.getLevel3Child();
			assertThat( level3Child ).extracting( "id" ).isEqualTo( 3 );

			entityManager.getTransaction().commit();
			entityManager.close();
		}
		{
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();

			Level1 level1 = entityManager.unwrap( Session.class ).load( Level1.class, 1 );
			Level2 level2 = level1.getLevel2Child();
			assertThat( level2 ).isNotNull();
			assertThat( level2.getLevel1Parent() ).extracting( "id" ).isEqualTo( 1 );
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	@Entity( name = "Level1" )
	public static class Level1 {

		@Id
		private Integer id;

		@OneToOne( mappedBy = "level1Parent" )
		private Level2 level2Child;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Level2 getLevel2Child() {
			return level2Child;
		}

		public void setLevel2Child(Level2 level2Child) {
			this.level2Child = level2Child;
		}
	}

	@Entity( name = "Level2" )
	public static class Level2 {

		@Id
		private Integer id;

		@OneToOne
		private Level1 level1Parent;

		@OneToOne( mappedBy = "level2Parent" )
		private Level3 level3Child;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Level3 getLevel3Child() {
			return level3Child;
		}

		public void setLevel3Child(Level3 level3Child) {
			this.level3Child = level3Child;
		}

		public Level1 getLevel1Parent() {
			return level1Parent;
		}

		public void setLevel1Parent(Level1 level1Parent) {
			this.level1Parent = level1Parent;
		}
	}

	@Entity( name = "DerivedLevel2" )
	public static class DerivedLevel2 extends Level2 {

	}

	@Entity( name = "Level3" )
	static class Level3 {

		@Id
		private Integer id;

		@Basic
		private String name;

		// todo marco : this should be Level2 because the not-owning side is Level2 (not Derived)
		//  and it defines a mappedBy, so level2Parent could be Level2 or another subclass and not only Derived
		@OneToOne( fetch = FetchType.LAZY )
		private DerivedLevel2 level2Parent;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public DerivedLevel2 getLevel2Parent() {
			return level2Parent;
		}

		public void setLevel2Parent(DerivedLevel2 level2Parent) {
			this.level2Parent = level2Parent;
		}
	}
}
