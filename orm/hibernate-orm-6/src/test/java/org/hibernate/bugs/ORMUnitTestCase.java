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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

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
				Parent.class,
				ParentId.class,
				Child1Id.class,
				Child1.class,
				Child2.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}

	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.ORDER_INSERTS, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.ORDER_UPDATES, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		var parentId = new ParentId( "id1" );
		var child1Id1 = new Child1Id( parentId, 1 );
		var child1Id2 = new Child1Id( parentId, 2 );

		var parent = new Parent(
				parentId,
				List.of(
						new Child1( child1Id1, List.of() ),
						new Child1( child1Id2, List.of() )
				)
		);

		s.persist( parent );

		tx.commit();
		s.close();
	}

	@Embeddable
	public static class ParentId implements Comparable<Object> {
		private String id;

		public ParentId() {
		}

		public ParentId(String id) {
			this.id = id;
		}

		@Override
		public int compareTo(Object o) {
			if ( o instanceof ParentId ) {
				return id.compareTo( ( (ParentId) o ).id );
			}

			return 0;
		}
	}

	@Entity( name = "Parent" )
	public static class Parent {
		@EmbeddedId
		private ParentId parentId;

		@OneToMany( cascade = CascadeType.ALL )
		@JoinColumn( name = "id", referencedColumnName = "id" )
		private List<Child1> child1s;

		public Parent() {
		}

		public Parent(ParentId parentId, List<Child1> child1s) {
			this.parentId = parentId;
			this.child1s = child1s;
		}
	}

	@Embeddable
	public static class Child1Id {
		@Embedded
		private ParentId parentId;
		private Integer version;

		public Child1Id() {
		}

		public Child1Id(ParentId parentId, Integer version) {
			this.parentId = parentId;
			this.version = version;
		}
	}

	@Entity( name = "Child1" )
	public static class Child1 {
		@EmbeddedId
		private Child1Id child1Id;

		@OneToMany( cascade = CascadeType.ALL )
		@JoinColumn( name = "child1_id", referencedColumnName = "id" )
		@JoinColumn( name = "child1_version", referencedColumnName = "version" )
		private List<Child2> child2s;

		public Child1() {
		}

		public Child1(Child1Id child1Id, List<Child2> child2s) {
			this.child1Id = child1Id;
			this.child2s = child2s;
		}
	}

	@Entity( name = "Child2" )
	public static class Child2 {
		@Id
		@GeneratedValue
		private Long id;
	}
}
