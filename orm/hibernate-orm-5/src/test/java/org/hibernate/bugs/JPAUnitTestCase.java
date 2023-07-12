package org.hibernate.bugs;

import java.io.Serializable;
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

	@Entity(name = "Operator")
	public static class Operator {
		@Id
		private String operatorId;
	}

	@Embeddable
	public static class OperatorPK implements Serializable {
		private String operatorId;
	}

	@Entity(name = "Price")
	@IdClass(PricePK.class)
	public static class Price {
		@ManyToOne
		private Operator operator;

		@Id
		private String wholesalePrice;

		@OneToOne
		private Product product;
	}

	@Embeddable
	public static class PricePK implements Serializable {
		@Embedded
		private OperatorPK operator;

		private String wholesalePrice;
	}

	@Entity(name = "Product")
	@IdClass(ProductPK.class)
	public static class Product {
		@Id
		private String productId;

		@OneToOne(optional = false, mappedBy = "product")
		private Price wholesalePrice;
	}

	// Product.{id}: productId, operatorId, wholesalePrice
	// Why do we return the virtual embedded instead of the idClass as mappedType()?

	@Embeddable
	public static class ProductPK implements Serializable {
		private String productId;

		@Embedded
		private PricePK wholesalePrice;
	}
}
