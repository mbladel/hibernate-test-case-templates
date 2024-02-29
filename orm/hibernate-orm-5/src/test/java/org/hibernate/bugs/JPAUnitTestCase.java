package org.hibernate.bugs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

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

		final TestEntity entity = new TestEntity();
		entity.descriptions = new HashSet<>( Arrays.asList( "P_1", "P_2", "P_3" ) );
		entityManager.persist( entity );

		final List<TestEntity> results = entityManager.createQuery(
						"select e from TestEntity e where e.descriptions like :text",
						TestEntity.class
				)
				.setParameter( "text", "%,P_2,%" )
				.getResultList();

		Assertions.assertEquals( 1, results.size() );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "TestEntity" )
	public static class TestEntity {
		@Converter
		public static class SetConverter implements AttributeConverter<Set<String>, String> {

			@Override
			public String convertToDatabaseColumn(final Set<String> attribute) {
				if ( attribute != null && !attribute.isEmpty() ) {
					return String.join( ",", attribute );
				}
				return null;
			}

			@Override
			public Set<String> convertToEntityAttribute(final String dbData) {
				if ( dbData != null ) {
					return Arrays.stream( dbData.split( "," ) ).collect( Collectors.toSet() );
				}
				return null;
			}
		}

		@Id
		@GeneratedValue
		public Long id;

		@Convert( converter = SetConverter.class )
		public Set<String> descriptions;
	}
}
