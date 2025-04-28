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

import org.hibernate.cfg.AvailableSettings;

import org.hibernate.testing.bytecode.enhancement.CustomEnhancementContext;
import org.hibernate.testing.bytecode.enhancement.extension.BytecodeEnhanced;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * <p>
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
@DomainModel(
		annotatedClasses = {
				QuarkusLikeORMUnitTestCase.Parent.class,
				QuarkusLikeORMUnitTestCase.Child.class,
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

	@BeforeAll
	public void initialiseDb(SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			Parent a = new Parent();
			a.setId( 1L );
			a.setName( "A" );
			Child y = new Child();
			y.setName( "Child 1" );
			y.setParent( a );
			session.persist( y );
			Parent b = new Parent();
			b.setId( 2L );
			b.setName( "B" );
			session.persist( a );
			session.persist( b );
		} );
	}

	@Test
	public void testSwapChild(SessionFactoryScope scope) {
		// swap from A to B
		swapChild( 1L, 2L, scope );
		scope.inSession( session -> {
			final Parent a = session.find( Parent.class, 1L );
			assertThat( a.getName() ).isEqualTo( "A" );
			assertThat( a.getChildren() ).hasSize( 0 );
			final Parent b = session.find( Parent.class, 2L );
			assertThat( b.getName() ).isEqualTo( "B" );
			assertThat( b.getChildren() ).hasSize( 1 );
		} );

		// swap back from B to A
		swapChild( 1L, 1L, scope );
		scope.inSession( session -> {
			final Parent a = session.find( Parent.class, 1L );
			assertThat( a.getName() ).isEqualTo( "A" );
			assertThat( a.getChildren() ).hasSize( 1 );
			final Parent b = session.find( Parent.class, 2L );
			assertThat( b.getName() ).isEqualTo( "B" );
			assertThat( b.getChildren() ).hasSize( 0 );
		} );
	}


	private static void swapChild(Long childId, Long newParentId, SessionFactoryScope scope) {
		scope.inTransaction( session -> {
			var newParent = session.find( Parent.class, newParentId );
			var child = session.find( Child.class, childId );
			child.setParent( newParent );
			session.merge( child );
		} );
	}

	@Entity(name = "Child")
	static class Child {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		@ManyToOne
		private Parent parent;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Parent getParent() {
			return parent;
		}

		public void setParent(Parent parent) {
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity(name = "Parent")
	static class Parent {
		@Id
		private Long id;

		private String name;

		@OneToMany(mappedBy = "parent")
		private List<Child> children;

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

		public List<Child> getChildren() {
			return children;
		}

		public void setChildren(List<Child> children) {
			this.children = children;
		}
	}

}
