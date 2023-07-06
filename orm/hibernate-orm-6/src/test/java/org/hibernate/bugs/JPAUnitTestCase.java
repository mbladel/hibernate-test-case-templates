package org.hibernate.bugs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

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

		final long start = System.nanoTime();
		// prepare a set of data, 1k books with 100 blank pages each
		for ( int i = 0; i < 100; i++ ) {
			entityManager.persist( Book.of( 1000 ) );
		}
		System.out.println( "Persisting took: " + ( ( System.nanoTime() - start ) / 1e6 ) + " ms" );

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

		final long start = System.nanoTime();
		// trigger load of all books and all associated pages
		entityManager.createQuery(
				"from Book",
				Book.class
		).getResultList().forEach( b -> b.getPages().size() );
		System.out.println( "Loading took: " + ( ( System.nanoTime() - start ) / 1e6 ) + " ms" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Book" )
	public static class Book {
		@Id
		@GeneratedValue( generator = "uuid" )
		@GenericGenerator( name = "uuid", strategy = "uuid2" )
		private String id;
		@OneToMany( fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.PERSIST )
		@Fetch( FetchMode.SUBSELECT )
		private List<Page> pages;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setPages(List<Page> pages) {
			this.pages = pages;
		}

		public List<Page> getPages() {
			return pages;
		}

		public static Book of(int nbPage) {
			Book book = new Book();
			book.pages = new ArrayList<>();
			for ( int i = 0; i < nbPage; i++ ) {
				Page page = new Page();
				page.setBook( book );
				book.pages.add( page );
			}
			return book;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			Book book = (Book) o;
			return Objects.equals( id, book.id );
		}

		@Override
		public int hashCode() {
			return Objects.hash( id );
		}
	}

	@Entity( name = "Page" )
	public static class Page {
		@Id
		@GeneratedValue( generator = "uuid" )
		@GenericGenerator( name = "uuid", strategy = "uuid2" )
		private String id;

		@ManyToOne( cascade = CascadeType.PERSIST )
		@JoinColumn( name = "book_id" )
		private Book book;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			Page page = (Page) o;
			return Objects.equals( id, page.id );
		}

		@Override
		public int hashCode() {
			return Objects.hash( id );
		}
	}
}