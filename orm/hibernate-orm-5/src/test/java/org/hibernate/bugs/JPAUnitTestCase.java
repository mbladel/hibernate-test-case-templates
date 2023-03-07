package org.hibernate.bugs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;

import org.hibernate.Session;

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
		// create some data
		Session session = entityManager.unwrap( Session.class );
		session.createNativeQuery( "insert into ITEM (id) values (100)" ).executeUpdate();
		session.createNativeQuery( "insert into ITEM (id) values (101)" ).executeUpdate();
		session.createNativeQuery( "insert into BUCKET (id) values (200)" ).executeUpdate();
		session.createNativeQuery( "insert into LINE (id, ITEM_ID, BUCKET_ID) values (300, 100, 200)" )
				.executeUpdate();

		// move line from item to other item
		final Item item100 = entityManager.find( Item.class, new PkItem().withId( 100L ) );
		final Item item101 = entityManager.find( Item.class, new PkItem().withId( 101L ) );
		item100.getLines().forEach( line -> line.setItem( item101 ) );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static class PkBucket implements Serializable {
		private Long id;

		public PkBucket withId(Long id) {
			this.id = id;
			return this;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity( name = "Bucket" )
	@IdClass( PkBucket.class )
	public static class Bucket {

		@Id
		@GeneratedValue
		private Long id;

		@OneToMany( cascade = CascadeType.ALL )
		@JoinColumn( name = "BUCKET_ID", referencedColumnName = "id", nullable = false )
		private List<Line> lines = new ArrayList<>();
	}

	public static class PkLine implements Serializable {
		private Long id;

		public PkLine withId(Long id) {
			this.id = id;
			return this;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity( name = "Line" )
	@IdClass( PkLine.class )
	public static class Line {

		@Id
		@GeneratedValue
		private Long id;

		@ManyToOne( targetEntity = Item.class )
		@JoinColumn( name = "ITEM_ID", referencedColumnName = "id", nullable = false )
		private Item item;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}
	}

	public static class PkItem implements Serializable {
		private Long id;

		public PkItem withId(Long id) {
			this.id = id;
			return this;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity( name = "Item" )
	@IdClass( PkItem.class )
	public static class Item {

		@Id
		@GeneratedValue
		private Long id;

		@OneToMany( mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true )
		private List<Line> lines = new ArrayList<>();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<Line> getLines() {
			return lines;
		}

		public void setLines(List<Line> lines) {
			this.lines = lines;
		}
	}
}
