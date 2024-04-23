package org.hibernate.bugs;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.PostLoad;

import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

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

		final Category[] categories = new Category[NUMBER_OF_CATEGORIES];
		for ( int i = 0; i < categories.length; i++ ) {
			categories[i] = new Category( i, "category_" + i );
			entityManager.persist( categories[i] );
		}

		// Chain-link the categories (#n points to #n+1)
		// Chain-link the categories (#n points to #n+1, last one points to #0)
		for ( int i = 0; i < categories.length - 1; i++ ) {
			categories[i].setNextCategory( categories[i + 1] );
		}
		// And chain the last category back to the first one
		categories[categories.length - 1].nextCategory = categories[0];

		final CategoryHolder holder1 = new CategoryHolder( 0 );
		holder1.leftCategory = categories[0];
		holder1.rightCategory = categories[3];
		entityManager.persist( holder1 );
		final CategoryHolder holder2 = new CategoryHolder( 1 );
		holder2.leftCategory = categories[0];
		holder2.rightCategory = categories[4];
		entityManager.persist( holder2 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	private static final int NUMBER_OF_CATEGORIES = 5;

	@Test
	public void recursiveBatchLoadingSameQueryTest() {
		entityManagerFactory.getCache().evict( Category.class );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		final CategoryHolder result = entityManager.find( CategoryHolder.class, 1 );
		Category category = result.getLeftCategory();
		for ( int i = 0; i < NUMBER_OF_CATEGORIES; i++ ) {
			assertThat( category ).matches( Hibernate::isInitialized, "Category was not initialized" )
					.extracting( Category::getId )
					.isEqualTo( i );
			if ( i == NUMBER_OF_CATEGORIES - 1 ) {
				assertThat( category ).isSameAs( result.getRightCategory() );
				assertThat( category.getNextCategory() ).isSameAs( result.getLeftCategory() );
			}
			category = category.getNextCategory();
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Proxy( lazy = false )
	@Entity( name = "Category" )
	@BatchSize( size = 10 )
	@Cacheable
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
	public static class Category {
		@Id
		private Integer id;

		private String name;

		@ManyToOne( fetch = FetchType.LAZY )
		@Fetch( value = FetchMode.SELECT )
		private Category nextCategory;

		public Category() {
		}

		public Category(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public Category getNextCategory() {
			return nextCategory;
		}

		public void setNextCategory(Category nextCategory) {
			this.nextCategory = nextCategory;
		}

		public String getName() {
			return name;
		}

		@PostLoad
		public void postLoad() {
			assertThat( name ).isNotNull().isEqualTo( "category_" + id );
			// note : nextCategory.name will be null here !
		}
	}

	@Entity( name = "CategoryHolder" )
	public static class CategoryHolder {
		@Id
		private Integer id;
		@ManyToOne( fetch = FetchType.LAZY )
		@Fetch( value = FetchMode.SELECT )
		private Category leftCategory;
		@ManyToOne( fetch = FetchType.LAZY )
		@Fetch( value = FetchMode.SELECT )
		private Category rightCategory;

		public CategoryHolder() {
		}

		public CategoryHolder(Integer id) {
			this.id = id;
		}

		public Integer getId() {
			return id;
		}

		public Category getLeftCategory() {
			return leftCategory;
		}

		public Category getRightCategory() {
			return rightCategory;
		}
	}
}
