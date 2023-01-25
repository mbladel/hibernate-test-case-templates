package org.hibernate.bugs;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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

		Item item1 = new Item();
		item1.setName( "Item 1" );
		entityManager.persist( item1 );

		ItemSale itemSale11 = new ItemSale();
		itemSale11.setItem( item1 );
		itemSale11.setTotal( new BigDecimal( 1 ) );
		entityManager.persist( itemSale11 );

		ItemSale itemSale12 = new ItemSale();
		itemSale12.setItem( item1 );
		itemSale12.setTotal( new BigDecimal( 2 ) );
		entityManager.persist( itemSale12 );

		Item item2 = new Item();
		item2.setName( "Item 2" );
		entityManager.persist( item2 );

		ItemSale itemSale21 = new ItemSale();
		itemSale21.setItem( item1 );
		itemSale21.setTotal( new BigDecimal( 3 ) );
		entityManager.persist( itemSale21 );

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

		TypedQuery<Sumary> query = entityManager.createQuery(
				"SELECT NEW org.hibernate.bugs.Sumary(i, SUM(item_sale.total)) " +
						"FROM ItemSale item_sale " +
						"JOIN item_sale.item i " +
						"GROUP BY i", Sumary.class );
		for ( Sumary s : query.getResultList() ) {
			System.out.println( "Id: " + s.getItem().getId() );
			System.out.println( "Name: " + s.getItem().getName() );
			System.out.println( "Sum: " + s.getTotal() );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
