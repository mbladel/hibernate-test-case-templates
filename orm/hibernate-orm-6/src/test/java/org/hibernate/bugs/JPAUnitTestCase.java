package org.hibernate.bugs;

import java.math.BigDecimal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

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

//		TypedQuery<Sumary> query = entityManager.createQuery(
//				"SELECT NEW org.hibernate.bugs.Sumary(i, SUM(is.total)) " +
//						"FROM ItemSale is " +
//						"JOIN is.item i " +
//						"GROUP BY i", Sumary.class);
//		System.out.println(query.getResultList());

		entityManager.createNativeQuery( "select\n" +
												 "        i2_0.id,\n" +
												 "        i2_0.name,\n" +
												 "        sum(i1_0.total) \n" +
												 "    from\n" +
												 "        ItemSale i1_0 \n" +
												 "    join\n" +
												 "        Item i2_0 \n" +
												 "            on i2_0.id=i1_0.item_id \n" +
												 "    group by\n" +
				                                 "        i1_0.item_id"
//												 "        i2_0.id" // nb: this works
		).getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
