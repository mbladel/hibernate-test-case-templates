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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.Assert.assertEquals;

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
				Post.class
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
	public void hhh123Test() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		Post entity = new Post();
		entity.setId( 1L );
		entity.setTags( new LinkedHashSet<>( Arrays.asList( "foo", "bar" ) ) );
		s.persist( entity );

		CriteriaBuilder cb = s.getCriteriaBuilder();
		CriteriaQuery<Post> query = cb.createQuery( Post.class );
		Root<Post> root = query.from( Post.class );

		query.select( root ).where(
				cb.like(
						cb.concat(
								cb.concat( ",", root.get( "tags" ) ),
								","
						),
						"%foo%"
				) );

		List<Post> resultList = s.createQuery( query ).getResultList();
		assertEquals( 1, resultList.size() );

		tx.commit();
		s.close();
	}

	@Entity
	public static class Post {
		@Id
		private Long id;

		@Convert(converter = StringSetConverter.class)
		private Set<String> tags;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Set<String> getTags() {
			return tags;
		}

		public void setTags(Set<String> tags) {
			this.tags = tags;
		}
	}

	@Converter
	public static class StringSetConverter implements AttributeConverter<Set<String>, String> {
		@Override
		public String convertToDatabaseColumn(Set<String> attribute) {
			return attribute == null ? null : String.join( ",", attribute );
		}

		@Override
		public Set<String> convertToEntityAttribute(String dbData) {
			return dbData == null ? null : new LinkedHashSet<>( Arrays.asList( dbData.split( "," ) ) );
		}
	}
}
