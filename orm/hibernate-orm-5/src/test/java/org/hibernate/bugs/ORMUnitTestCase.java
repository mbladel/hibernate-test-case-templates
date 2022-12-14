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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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
				Credential.class,
				Person.class,
				NaturalPerson.class,
				LegalPerson.class
		};
	}

	@Test
	public void hhh123Test() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		CriteriaBuilder cb = s.getCriteriaBuilder();
		CriteriaQuery<Credential> credentialQuery = cb.createQuery( Credential.class );
		Root<Credential> from = credentialQuery.from( Credential.class );

		// user version
//		Path<Person> personPath = from.get("person");
//		Path<LegalPerson> legalPersonPath = cb.treat(personPath, LegalPerson.class);
//		Path<NaturalPerson> naturalPersonPath = cb.treat(personPath, NaturalPerson.class);

		// working version
		Join<Credential, Person> personPath = from.join( "person" );
		Join<Credential, LegalPerson> legalPersonPath = cb.treat( personPath, LegalPerson.class );
		Join<Credential, NaturalPerson> naturalPersonPath = cb.treat( personPath, NaturalPerson.class );


		credentialQuery.select( from ).where(
				cb.or(
						cb.equal( legalPersonPath.get( "name" ), "ROOT" ),
						cb.equal( naturalPersonPath.get( "surname" ), "MOSCATELLI" )
				)
		);
		TypedQuery<Credential> credentialCreateQuery = s.createQuery( credentialQuery );
		List<Credential> credentialList = credentialCreateQuery.getResultList();
		assertNotNull( credentialList );

		tx.commit();
		s.close();
	}

	@Entity
	public static class Credential {
		@Id
		@GeneratedValue
		private Long id;

		@OneToOne
		private Person person;
	}

	@Entity
	@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	public static class Person {
		@Id
		@GeneratedValue
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	@Entity
	public static class LegalPerson extends Person {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity
	public static class NaturalPerson extends Person {
		private String surname;

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
}
