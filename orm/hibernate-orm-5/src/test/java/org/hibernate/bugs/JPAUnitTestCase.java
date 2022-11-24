package org.hibernate.bugs;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;

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

		// doing stuff

		Employee e = new Employee();
		e.put( "wash", "the dishes" );
		e.put( "clean", "the kitchen" );
		entityManager.persist( e );

		int id = e.id;

		entityManager.flush();
		entityManager.clear();

		Employee e2 = entityManager.find( Employee.class, id );
		assertEquals( "the dishes", e2.get( "wash" ) );
		assertEquals( "the kitchen", e2.get( "clean" ) );

		// done stuff

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity
	public static class Employee {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		public int id;


		@ElementCollection
		@Convert(converter = MyStringConverter.class /*, attributeName = "value"*/)
		private final Map<String, String> responsibilities = new HashMap<>();

		void put(String key, String value) {
			responsibilities.put( key, value );
		}

		String get(String key) {
			return responsibilities.get( key );
		}
	}


	public static class MyStringConverter implements AttributeConverter<String, String> {

		@Override
		public String convertToDatabaseColumn(String attribute) {
			return attribute;
		}

		@Override
		public String convertToEntityAttribute(String dbData) {
			return dbData;
		}
	}

	@Converter
	public static class MyMapConverter implements AttributeConverter<Map<String, String>, String> {

		@Override
		public String convertToDatabaseColumn(Map<String, String> map) {
			StringBuilder result = new StringBuilder();
			for ( String s : map.keySet() ) {
				result.append( s ).append( "=" ).append( map.get( s ) ).append( "," );
			}
			String r = result.toString();
			return r.substring( 0, r.length() - 1 );
		}

		@Override
		public Map<String, String> convertToEntityAttribute(String s) {
			Map<String, String> result = new HashMap<>();
			for ( String value : s.split( "," ) ) {
				String[] entry = value.trim().split( "=" );
				result.put( entry[0], entry[1] );
			}
			return result;
		}
	}
}
