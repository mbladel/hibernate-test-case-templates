package org.hibernate.bugs;

import java.time.YearMonth;
import java.util.*;
import javax.persistence.*;

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

		entityManager.persist( new ConvertedIdEntity( YearMonth.of( 2022, 12 ) ) );
		entityManager.flush();
		entityManager.clear();

		final YearMonth max = entityManager.createQuery(
				"select max(e.convertedId) from ConvertedIdEntity e",
				YearMonth.class
		).getSingleResult();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "ConvertedIdEntity" )
	public static class ConvertedIdEntity {
		@Convert( converter = YearMonthConverter.class )
		@Id
		private YearMonth convertedId;

		public ConvertedIdEntity() {
		}

		public ConvertedIdEntity(YearMonth convertedId) {
			this.convertedId = convertedId;
		}

		public YearMonth getConvertedId() {
			return convertedId;
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
