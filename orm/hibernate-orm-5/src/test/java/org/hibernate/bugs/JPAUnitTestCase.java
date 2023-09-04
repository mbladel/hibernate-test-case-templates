package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;

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

		entityManager.persist( new TestEntity( "test" ) );

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

		final Query query = entityManager.createQuery( String.format(
				"select new %s(t.id, t.name) from TestEntity t",
				TestDto.class.getName()
		), TestDto.class );

		final List<TestDto> resultList = query.getResultList();
		assertThat( resultList ).hasSize( 1 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "TestEntity" )
	public static class TestEntity {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		public TestEntity() {
		}

		public TestEntity(String name) {
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class TestDto {
		private String value;
		private String title;

		public TestDto() {
		}

		public TestDto(String value, String title) {
			super();
			this.value = value;
			this.title = title;
		}

		public TestDto(int value, String title) {
			this( String.valueOf( value ), title );
		}

		public TestDto(long value, String title) {
			this( String.valueOf( value ), title );
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
}
