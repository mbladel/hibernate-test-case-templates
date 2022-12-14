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

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

		CriteriaBuilder cb = s.getCriteriaBuilder();
		CriteriaQuery<Credential> credentialQuery = cb.createQuery( Credential.class );
		Root<Credential> credential = credentialQuery.from( Credential.class );

		// user version
//		Path<Person> personPath = from.get("person");
//		Path<LegalPerson> legalPersonPath = cb.treat(personPath, LegalPerson.class);
//		Path<NaturalPerson> naturalPersonPath = cb.treat(personPath, NaturalPerson.class);

		Join<Credential, Person> personPath = credential.join( "person" );
		Join<Credential, LegalPerson> legalPersonPath = cb.treat( personPath, LegalPerson.class );
		Join<Credential, NaturalPerson> naturalPersonPath = cb.treat( personPath, NaturalPerson.class );

		credentialQuery.select( credential ).where(
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


	@Entity(name = "Credential")
	public static class Credential {
		@Id
		@GeneratedValue
		private Long id;

		@OneToOne
		private Person person;
	}

	@Entity(name = "Person")
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

	@Entity(name = "LegalPerson")
	public static class LegalPerson extends Person {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity(name = "NaturalPerson")
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
