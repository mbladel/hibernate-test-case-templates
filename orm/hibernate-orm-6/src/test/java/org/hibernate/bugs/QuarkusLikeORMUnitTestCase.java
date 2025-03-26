/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;

import org.hibernate.testing.bytecode.enhancement.CustomEnhancementContext;
import org.hibernate.testing.bytecode.enhancement.extension.BytecodeEnhanced;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * <p>
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
@DomainModel(
		annotatedClasses = {
				QuarkusLikeORMUnitTestCase.User.class,
				QuarkusLikeORMUnitTestCase.Book.class,
				QuarkusLikeORMUnitTestCase.Country.class,
				QuarkusLikeORMUnitTestCase.UserFavoriteBook.class,
		}
)
@ServiceRegistry(
		// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
		settings = {
				// For your own convenience to see generated queries:
				@Setting(name = AvailableSettings.SHOW_SQL, value = "true"),
				@Setting(name = AvailableSettings.FORMAT_SQL, value = "true"),
				// @Setting( name = AvailableSettings.GENERATE_STATISTICS, value = "true" ),

				// Other settings that will make your test case run under similar configuration that Quarkus is using by default:
				@Setting(name = AvailableSettings.PREFERRED_POOLED_OPTIMIZER, value = "pooled-lo"),
				@Setting(name = AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, value = "16"),
				@Setting(name = AvailableSettings.BATCH_FETCH_STYLE, value = "PADDED"),
				@Setting(name = AvailableSettings.QUERY_PLAN_CACHE_MAX_SIZE, value = "2048"),
				@Setting(name = AvailableSettings.DEFAULT_NULL_ORDERING, value = "none"),
				@Setting(name = AvailableSettings.IN_CLAUSE_PARAMETER_PADDING, value = "true"),
				@Setting(name = AvailableSettings.SEQUENCE_INCREMENT_SIZE_MISMATCH_STRATEGY, value = "none"),
				@Setting(name = AvailableSettings.ORDER_UPDATES, value = "true"),

				// Add your own settings that are a part of your quarkus configuration:
				// @Setting( name = AvailableSettings.SOME_CONFIGURATION_PROPERTY, value = "SOME_VALUE" ),
		}
)
@SessionFactory
@BytecodeEnhanced
@CustomEnhancementContext(QuarkusLikeEnhancementContext.class)
class QuarkusLikeORMUnitTestCase {

	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test(SessionFactoryScope scope) throws Exception {
		scope.inTransaction( session -> {
			initData( session );

			var user = new User();
			user.setId( 1L );
			var book = new Book();
			book.setId( 1L );

			var favorite = new UserFavoriteBook();
			favorite.setBook( book );
			favorite.setUser( user );
			session.persist( favorite );

			// exception here
			var fav = session.createQuery(
					"from UserFavoriteBook ufb" +
							" join fetch ufb.book b" +
							" where b.id = ?1 and ufb.user.id = ?2",
					UserFavoriteBook.class
			).setParameter( 1, 1L ).setParameter( 2, 1L ).getSingleResult();
			assertThat( fav ).isNotNull();
			System.out.println( "Favorite is:" );
			System.out.println( fav );
		} );
	}

	void initData(Session session) {
		var user = new User();
		user.setId( 1L );
		user.setName( "Test" );
		session.persist( user );

		var book = new Book();
		book.setId( 2L );
		book.setTitle( "A Book" );
		session.persist( book );
	}

	@Entity(name = "UserEntity")
	static class User {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity(name = "BookEntity")
	static class Book {
		@Id
		@GeneratedValue
		private Long id;

		private String title;

		@OneToOne
		@JoinColumn(name = "origin_country")
		private Country origin;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	@Entity(name = "CountryEntity")
	static class Country {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity(name = "UserFavoriteBook")
	static class UserFavoriteBook {
		@Id
		@GeneratedValue
		private Long id;

		@ManyToOne
		@JoinColumn(name = "user_id")
		private User user;

		@ManyToOne
		@JoinColumn(name = "book_id")
		private Book book;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}
	}
}
