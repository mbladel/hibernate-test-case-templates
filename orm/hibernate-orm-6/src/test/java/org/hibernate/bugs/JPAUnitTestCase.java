package org.hibernate.bugs;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

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

		entityManager.persist( new Product( (short) 1, "product_1" ) );

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

		Query query = entityManager.createQuery( String.format(
				"select new %s(product.productNo,  product.name) from Product product",
				KeyValue.class.getName()
		) );

		final List resultList = query.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Product" )
	@Table( name = "t_product_main" )
	public static class Product {
		@Id
		@GeneratedValue
		private Integer uniqid;

		private Short productNo;

		private String name;

		public Product() {
		}

		public Product(Short productNo, String name) {
			this.productNo = productNo;
			this.name = name;
		}
	}

	public static class KeyValue {
		private Integer key;

		private String value;

		public KeyValue() {
		}

		public KeyValue(Short k) {
			System.out.println( "inside 1" );
			key = k == null ? null : k.intValue();
		}

		public KeyValue(Integer k, String val) {
			key = k;
			value = val;
		}

		public KeyValue(Short k, String val) {
			System.out.println( "inside 2" );
			key = k == null ? null : k.intValue();
			value = val;
		}

		public KeyValue(Short k, Short val) {
			key = k == null ? null : k.intValue();
			value = String.valueOf( val );
		}

		public KeyValue(Integer k, Short val) {
			key = k == null ? null : k.intValue();
			value = String.valueOf( val );
		}

		public KeyValue(Short k, Integer val) {
			key = k == null ? null : k.intValue();
			value = String.valueOf( val );
		}

		public KeyValue(Integer k, Integer val) {
			key = k == null ? null : k.intValue();
			value = String.valueOf( val );
		}

		public KeyValue(Short k, BigDecimal val) {
			key = k == null ? null : k.intValue();
			value = String.valueOf( val.setScale( 2, BigDecimal.ROUND_HALF_DOWN ) );
		}

		public KeyValue(Short k, Double val, Integer convertType) {
			key = k == null ? null : k.intValue();
			if ( convertType == 1 ) {
				value = String.format( "%10.0f", val );
			}
		}
	}
}
