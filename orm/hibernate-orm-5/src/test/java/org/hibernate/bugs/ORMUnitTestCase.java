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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
				EntityA.class,
				EntityB.class
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

	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );

		configuration.setProperty( AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, "2" );
	}

	@Test
	public void hhhXXXXTest() throws Exception {
		try (Session s = openSession()) {
			Transaction tx = s.beginTransaction();
			EntityB entityB = new EntityB();
			entityB.foo = 123;
			s.persist( entityB );

			EntityB entityB2 = new EntityB();
			entityB2.foo = 321;
			s.persist( entityB2 );

			EntityA entityA1 = new EntityA();
			entityA1.entityB = entityB;
			s.persist( entityA1 );

			EntityA entityA2 = new EntityA();
			entityA2.entityB = entityB2;
			s.persist( entityA2 );

			s.flush();
			s.clear();
			tx.commit();

			Query<MyPojo> query2 = s.createQuery(
					"select new " + MyPojo.class.getName() + "(t) from EntityA t",
					MyPojo.class
			);
			List<MyPojo> pojo = query2.list();
			assertThat(pojo).isNotNull();
//			assertThat( pojo.getFoo() ).isEqualTo( 123 );
		}
	}

	@Test
	public void hhhXXXXTest2() throws Exception {
		try (Session s = openSession()) {
			Transaction tx = s.beginTransaction();
			EntityB entityB = new EntityB();
			entityB.foo = 123;
			s.persist( entityB );

			EntityB entityB2 = new EntityB();
			entityB2.foo = 321;
			s.persist( entityB2 );

			EntityA entityA1 = new EntityA();
			entityA1.entityB = entityB;
			s.persist( entityA1 );

			EntityA entityA2 = new EntityA();
			entityA2.entityB = entityB2;
			s.persist( entityA2 );

			s.flush();
			s.clear();
			tx.commit();

			Query<EntityA> query2 = s.createQuery(
					"select t from EntityA t",
					EntityA.class
			);
			List<EntityA> pojo = query2.list();
			assertThat(pojo).isNotNull();
//			assertThat( pojo.getFoo() ).isEqualTo( 123 );
		}
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		@GeneratedValue
		private Integer id;

		@JoinColumn( name = "entityb_id" )
		@ManyToOne
		// @Fetch( FetchMode.SELECT )
		private EntityB entityB;
	}

	@Entity( name = "EntityB" )
	public static class EntityB {
		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		@Column( name = "ID" )
		private Integer id;

		@Column( name = "FOO" )
		private Integer foo;

		@OneToMany( mappedBy = "entityB" )
		@Fetch( FetchMode.SUBSELECT )
		private List<EntityA> listOfEntitiesA = new ArrayList<>();
	}

	public static class MyPojo {
		private final Integer foo;

		public MyPojo(EntityA a) {
			foo = a.entityB.foo;
		}

		public Integer getFoo() {
			return foo;
		}
	}
}
