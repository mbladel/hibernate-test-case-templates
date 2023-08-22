package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

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
		// Do stuff...
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public interface Property<T> {
		String getName();

		T getValue();
	}

	@Entity( name = "IntegerProperty" )
	public static class IntegerProperty implements Property<Integer> {
		@Id
		private Long id;

		private String name;

		private Integer value;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}

	@Entity( name = "StringProperty" )
	public static class StringProperty implements Property<String> {
		@Id
		private Long id;

		private String name;

		private String value;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@Entity( name = "OptionalPropertyHolder" )
	public static class OptionalPropertyHolder {
		@Id
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Property<?> getProperty() {
			return property;
		}

		public void setProperty(Property<?> property) {
			this.property = property;
		}

		@Any( metaColumn = @Column( name = "property_type" ), optional = true )
		@AnyMetaDef( idType = "long", metaType = "string", metaValues = {
				@MetaValue( value = "S", targetEntity = StringProperty.class ),
				@MetaValue( value = "I", targetEntity = IntegerProperty.class ),
		} )
		@JoinColumn( name = "property_id" )
		private Property<?> property;
	}

	@Entity( name = "NonOptionalPropertyHolder" )
	public static class NonOptionalPropertyHolder {
		@Id
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Property<?> getProperty() {
			return property;
		}

		public void setProperty(Property<?> property) {
			this.property = property;
		}

		@Any( metaColumn = @Column( name = "property_type" ), optional = false )
		@AnyMetaDef( idType = "long", metaType = "string", metaValues = {
				@MetaValue( value = "S", targetEntity = StringProperty.class ),
				@MetaValue( value = "I", targetEntity = IntegerProperty.class ),
		} )
		@JoinColumn( name = "property_id" )
		private Property<?> property;
	}

	@Entity( name = "NonNullablePropertyHolder" )
	public static class NonNullablePropertyHolder {
		@Id
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Property<?> getProperty() {
			return property;
		}

		public void setProperty(Property<?> property) {
			this.property = property;
		}

		@Any( metaColumn = @Column( name = "property_type" ), optional = true )
		@AnyMetaDef( idType = "long", metaType = "string", metaValues = {
				@MetaValue( value = "S", targetEntity = StringProperty.class ),
				@MetaValue( value = "I", targetEntity = IntegerProperty.class ),
		} )
		@JoinColumn( name = "property_id", nullable = false )
		private Property<?> property;
	}
}
