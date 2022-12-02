package org.hibernate.bugs;

import java.time.Year;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

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

		EntityA entityA = new EntityA( 1, Year.parse( "2022" ));
		entityManager.persist( entityA );

		entityManager.getTransaction().commit();

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

		TypedQuery<EntityA> query = entityManager.createQuery(
				"from entitya a where :year is null or a.year = :year",
				EntityA.class
		);
		query.setParameter( "year", Year.parse( "2022" ) );

		EntityA entityA = query.getSingleResult();
		assertEquals(1, entityA.getId());
		assertEquals("2022", entityA.getYear().toString());

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name = "entitya")
	public static class EntityA {
		@Id
		private Integer id;

		@Column(name = "year_attribute")
		@Convert(converter = YearConverter.class)
		private Year year;

		public EntityA() {
		}

		public EntityA(Integer id, Year year) {
			this.id = id;
			this.year = year;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Year getYear() {
			return year;
		}

		public void setYear(Year year) {
			this.year = year;
		}
	}

	@Converter
	public static class YearConverter implements AttributeConverter<Year, String> {
		@Override
		public String convertToDatabaseColumn(Year attribute) {
			return attribute != null ? attribute.toString() : null;
		}
		@Override
		public Year convertToEntityAttribute(String dbData) {
			return dbData != null ? Year.parse( dbData ) : null;
		}
	}
}
