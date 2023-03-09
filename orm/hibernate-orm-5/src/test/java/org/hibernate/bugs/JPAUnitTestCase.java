package org.hibernate.bugs;

import java.io.Serializable;
import java.time.YearMonth;
import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;

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
		entityManager.persist( new DemoEntity( 1L, YearMonth.of( 2022, 12 ) ) );
		entityManager.persist( new DemoEntity( 2L, YearMonth.of( 2023, 1 ) ) );
		entityManager.persist( new DemoEntity( 3L, YearMonth.of( 2023, 2 ) ) );
		entityManager.persist( new DemoEntity( 4L, YearMonth.of( 2023, 3 ) ) );
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

		Object result = entityManager.createQuery(
				"select max(de.yearMonth) from DemoEntity de",
				YearMonth.class
		).getSingleResult();
		System.out.println( "Result: " + result );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "DemoEntity" )
	public static class DemoEntity implements Serializable {
		@Id
		@Column( name = "id" )
		private Long id;
		@Convert( converter = YearMonthConverter.class )
		@Column( name = "year_month" )
		private YearMonth yearMonth;

		public DemoEntity() {
		}

		public DemoEntity(Long id, YearMonth yearMonth) {
			this.id = id;
			this.yearMonth = yearMonth;
		}

		public Long getId() {
			return id;
		}

		public YearMonth getYearMonth() {
			return yearMonth;
		}
	}

	@Converter( autoApply = true )
	public static class YearMonthConverter implements AttributeConverter<YearMonth, Integer> {
		@Override
		public Integer convertToDatabaseColumn(YearMonth attribute) {
			return attribute == null ? null : ( attribute.getYear() * 100 ) + attribute.getMonth().getValue();
		}

		@Override
		public YearMonth convertToEntityAttribute(Integer dbData) {
			return dbData == null ? null : YearMonth.of( dbData / 100, dbData % 100 );
		}
	}
}
