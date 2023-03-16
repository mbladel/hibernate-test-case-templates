package org.hibernate.bugs;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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

	//		private static final ObjectMapper objectMapper = new XmlMapper();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		objectMapper.findAndRegisterModules();
		objectMapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );

		LocalDate[] array = new LocalDate[] { LocalDate.now() };

		System.out.println( objectMapper.writeValueAsString( array ) );

		System.out.println( objectMapper.writeValueAsString( LocalDate.now() ) );

		System.out.println( objectMapper.writeValueAsString( new String[] { "", "test", null, "test2" } ) );

		System.out.println( objectMapper.writeValueAsString( new MyEnum[] {
				MyEnum.FALSE,
				MyEnum.FALSE,
				null,
				MyEnum.TRUE
		} ) );


//		LocalDateTime test1 = LocalDateTime.now();
//		System.out.println( objectMapper.writeValueAsString( test1 ) );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public enum MyEnum {
		FALSE, TRUE
	}
}
