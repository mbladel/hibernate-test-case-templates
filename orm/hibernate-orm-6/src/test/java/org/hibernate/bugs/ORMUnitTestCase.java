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

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 * <p>
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				TestEntity.class
		};
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void persisterTest() throws Exception {
		// persist entity
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		TestEntity entity = new TestEntity();
		entity.setName( "test" );
		s.persist( entity );

		CriteriaBuilder cb = s.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery( Long.class );
		Root<TestEntity> root = query.from( TestEntity.class );

		query.select( cb.count( root ) ).where( cb.equal( root.get( "parent" ), entity ) );
		Long count = s.createQuery( query ).getSingleResult();

		assertEquals( 0L, count );

		tx.commit();
		s.close();
	}


	@Entity(name = "TestEntity")
	public static class TestEntity extends AbstractTreeableEntity<TestEntity> {
	}

	@MappedSuperclass
	public abstract static class AbstractTreeableEntity<T extends AbstractTreeableEntity<T>> {
		@Id
		@GeneratedValue
		private Long id;

		protected String name;

		@ManyToOne(fetch = FetchType.LAZY)
		protected T parent;

		@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
		protected Collection<T> children;

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

		public T getParent() {
			return parent;
		}

		public void setParent(T parent) {
			this.parent = parent;
		}

		public Collection<T> getChildren() {
			return children;
		}

		public void setChildren(Collection<T> children) {
			this.children = children;
		}
	}

}
