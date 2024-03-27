package org.hibernate.bugs;

import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

		final Author a1 = new Author( 1L );
		final Author a2 = new Author( 2L );
		entityManager.persist( new Book( 1L, a1 ) );
		entityManager.persist( new Book( 2L, a1 ) );
		entityManager.persist( new Book( 3L, a2 ) );
		entityManager.persist( new Book( 4L, a2 ) );

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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final EntityGraph<Author> graph = entityManager.createEntityGraph( Author.class );
		graph.addAttributeNodes( "books" );
		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Author> cq = cb.createQuery( Author.class );
		final Root<Author> root = cq.from( Author.class );
		final TypedQuery<Author> query = entityManager.createQuery(
				cq.select( root ).where( root.join( "books", JoinType.LEFT ).get( "id" ).in( Arrays.asList( 1L, 3L ) ) )
		).setHint( "javax.persistence.fetchgraph", graph );
		final List<Author> authors = query.getResultList();

		assertThat( authors ).hasSize( 2 );
		for ( final Author author : authors ) {
			assertThat( author.books ).hasSize( 2 );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "Author" )
	static class Author {
		@Id
		private Long id;
		@OneToMany( mappedBy = "author" )
		private List<Book> books;

		public Author() {
		}

		public Author(Long id) {
			this.id = id;
		}
	}

	@Entity( name = "Book" )
	static class Book {
		@Id
		private Long id;

		@ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
		@JoinColumn( name = "author_id" )
		private Author author;

		public Book() {
		}

		public Book(Long id, Author author) {
			this.id = id;
			this.author = author;
		}
	}
}
