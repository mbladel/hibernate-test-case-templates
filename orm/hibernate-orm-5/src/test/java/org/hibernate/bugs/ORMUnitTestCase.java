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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

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
				TreeNode.class,
				ReferencedEntity.class
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
		// Add custom listener
		configuration.setProperty( AvailableSettings.EVENT_LISTENER_PREFIX + ".pre-update", Listener.class.getName() );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		// first session - create entities
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		TreeNode first = new TreeNode();
		s.persist( first );

		TreeNode second = new TreeNode();
		s.persist( second );

		final ReferencedEntity referenced = new ReferencedEntity();
		s.persist( referenced );

		s.persist( first );

		second.parent = first;
		second.someSet.add( referenced );
		s.persist( second );

		tx.commit();
		s.close();

		// second session - perform delete
		s = openSession();
		tx = s.beginTransaction();

		first = s.byId( TreeNode.class ).load( first.id );
		second = s.byId( TreeNode.class ).load( second.id );

		s.remove( first );
		s.remove( second );

		tx.commit();
		s.close();
	}

	@Entity( name = "TreeNode" )
	public static class TreeNode {

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@ManyToOne( optional = true )
		@JoinColumn( name = "parent_id" )
		private TreeNode parent;

		@OneToMany( mappedBy = "treeNode", fetch = FetchType.LAZY )
		private Set<ReferencedEntity> someSet = new HashSet<>();

	}

	@Entity( name = "ReferencedEntity" )
	public static class ReferencedEntity {

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@ManyToOne
		private TreeNode treeNode;
	}

	public static class Listener implements PreUpdateEventListener {

		@Override
		public boolean onPreUpdate(PreUpdateEvent event) {
			final Object entity = event.getEntity();
			if ( entity instanceof TreeNode ) {
				final TreeNode treeNode = (TreeNode) entity;
				treeNode.someSet.forEach( entry -> {
				} );
			}
			return false;
		}
	}
}
