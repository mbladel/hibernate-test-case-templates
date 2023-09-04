package org.hibernate.bugs;

import java.util.*;

import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

import jakarta.persistence.*;

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
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final User user1 = new User( "user1" );
		final User user2 = new User( "user2" );
		final Project project1 = new Project( "p1" );
		project1.getManagers().add( user1 );
		project1.getMembers().add( user2 );
		final Project project2 = new Project( "p2" );
		project2.getMembers().add( user1 );
		entityManager.persist( user1 );
		entityManager.persist( user2 );
		entityManager.persist( project1 );
		entityManager.persist( project2 );

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

		final User user = entityManager.find( User.class, "user1" );
		assertThat( user.getManagedProjects().stream().map( Project::getName ) ).contains( "p1" );
//		assertThat( user.getOtherProjects().stream().map( Project::getName ) ).contains( "p1", "p2" );
		final Project p1 = entityManager.find( Project.class, "p1" );
		p1.getManagers().remove( user );
		assertThat( p1.getMembers().stream().map( User::getName ) ).contains( "user1" );
		entityManager.persist( user );

		entityManager.getTransaction().commit();
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.createQuery( "delete from Project" ).executeUpdate();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Project" )
	@Table( name = "t_project" )
	public static class Project {
		@Id
		private String name;

		@ManyToMany
		@JoinTable(
				name = "project_users",
				joinColumns = { @JoinColumn( name = "project_id" ) },
				inverseJoinColumns = { @JoinColumn( name = "user_id" ) }
		)
		@WhereJoinTable( clause = "project_id is not null" )
		@Where( clause = "deleted = false" )
		private Set<User> managers = new HashSet<>();

		@ManyToMany
		@JoinTable(
				name = "project_users",
				joinColumns = { @JoinColumn( name = "project_id" ) },
				inverseJoinColumns = { @JoinColumn( name = "user_id" ) }
		)
		@WhereJoinTable( clause = "user_id is not null" )
		private Set<User> members = new HashSet<>();

		public Project() {
		}

		public Project(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Set<User> getManagers() {
			return managers;
		}

		public Set<User> getMembers() {
			return members;
		}
	}

//	@Embeddable
//	public static class ProjectUsersId implements Serializable {
//		@Column( name = "project_id" )
//		private String projectId;
//		@Column( name = "user_id" )
//		private String userId;
//		@Column( name = "role" )
//		private String role;
//	}
//
//	@Entity( name = "ProjectUsers" )
//	@Table( name = "project_users" )
//	public static class ProjectUsers {
//		@EmbeddedId
//		private ProjectUsersId id;
//	}

	@Entity( name = "User" )
	@Table( name = "t_user" )
	public static class User {
		@Id
		private String name;

		@ManyToMany( mappedBy = "managers" )
		@WhereJoinTable( clause = "project_id is not null" )
		private Set<Project> managedProjects = new HashSet<>();

		@ManyToMany( mappedBy = "members" )
		@WhereJoinTable( clause = "user_id is not null" )
		private Set<Project> otherProjects = new HashSet<>();

		private boolean deleted;

		public User() {
		}

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Set<Project> getManagedProjects() {
			return managedProjects;
		}

		public Set<Project> getOtherProjects() {
			return otherProjects;
		}
	}
}

