package org.hibernate.bugs;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager hibSession = entityManagerFactory.createEntityManager();
		hibSession.getTransaction().begin();

		{
			// Create two majors
			Major m1 = new Major();
			m1.setName( "Biology" );
			hibSession.persist( m1 );
			Major m2 = new Major();
			m2.setName( "Computer Science" );
			hibSession.persist( m2 );

			// Create three students
			Student s1 = new Student();
			s1.setName( "Andrew" );
			StudentMajor sm1 = new StudentMajor();
			sm1.setStudent( s1 );
			sm1.setMajor( m1 );
			sm1.setClassification( "01" );
			s1.addToMajors( sm1 );
			hibSession.persist( s1 );

			Student s2 = new Student();
			s2.setName( "Brian" );
			StudentMajor sm2 = new StudentMajor();
			sm2.setStudent( s2 );
			sm2.setMajor( m1 );
			sm2.setClassification( "02" );
			s2.addToMajors( sm2 );
			hibSession.persist( s2 );

			Student s3 = new Student();
			s3.setName( "Charlie" );
			StudentMajor sm3 = new StudentMajor();
			sm3.setStudent( s3 );
			sm3.setMajor( m1 );
			sm3.setClassification( "01" );
			s3.addToMajors( sm3 );
			StudentMajor sm4 = new StudentMajor();
			sm4.setStudent( s3 );
			sm4.setMajor( m2 );
			sm4.setClassification( "02" );
			s3.addToMajors( sm4 );
			hibSession.persist( s3 );

			// Create two subjects
			Subject math = new Subject();
			math.setName( "MATH" );
			hibSession.persist( math );
			Subject biology = new Subject();
			biology.setName( "BIOL" );
			hibSession.persist( biology );
			Subject cs = new Subject();
			cs.setName( "CS" );
			hibSession.persist( cs );

			// Create a few courses
			Course c1 = new Course();
			c1.setSubject( math );
			c1.setNumber( "101" );
			hibSession.persist( c1 );
			Course c2 = new Course();
			c2.setSubject( math );
			c2.setNumber( "201" );
			hibSession.persist( c2 );
			Course c3 = new Course();
			c3.setSubject( biology );
			c3.setNumber( "101" );
			hibSession.persist( c3 );
			Course c4 = new Course();
			c4.setSubject( biology );
			c4.setNumber( "201" );
			hibSession.persist( c4 );
			Course c5 = new Course();
			c5.setSubject( cs );
			c5.setNumber( "101" );
			hibSession.persist( c5 );
			Course c6 = new Course();
			c6.setSubject( cs );
			c6.setNumber( "201" );
			hibSession.persist( c6 );

			// Create some course demands
			Demand d1 = new Demand();
			d1.setCourse( c1 );
			d1.setStudent( s1 );
			hibSession.persist( d1 );
			Demand d2 = new Demand();
			d2.setCourse( c1 );
			d2.setStudent( s2 );
			hibSession.persist( d2 );
			Demand d3 = new Demand();
			d3.setCourse( c2 );
			d3.setStudent( s2 );
			hibSession.persist( d3 );
			Demand d4 = new Demand();
			d4.setCourse( c2 );
			d4.setStudent( s3 );
			hibSession.persist( d4 );
			Demand d5 = new Demand();
			d5.setCourse( c3 );
			d5.setStudent( s1 );
			hibSession.persist( d5 );
			Demand d6 = new Demand();
			d6.setCourse( c3 );
			d6.setStudent( s3 );
			hibSession.persist( d6 );
			Demand d7 = new Demand();
			d7.setCourse( c4 );
			d7.setStudent( s1 );
			hibSession.persist( d7 );
			Demand d8 = new Demand();
			d8.setCourse( c5 );
			d8.setStudent( s2 );
			hibSession.persist( d8 );
			Demand d9 = new Demand();
			d9.setCourse( c6 );
			d9.setStudent( s2 );
			hibSession.persist( d9 );
			Demand d0 = new Demand();
			d0.setCourse( c6 );
			d0.setStudent( s3 );
			hibSession.persist( d0 );
		}

		hibSession.getTransaction().commit();
		hibSession.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Session hibSession = entityManager.unwrap( Session.class );

		for ( String subject : new String[] { "MATH", "BIOL", "CS" } ) {
			List<Object[]> list = hibSession.createQuery(
							"select d.id, d.course, s " +
									"from Demand d inner join d.student s left join fetch s.majors " +
									"where d.course.subject.name = :subject",
							Object[].class
					)
					.setParameter( "subject", subject )
					.setCacheable( true )
					.list();
			print( list );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public void print(List<Object[]> list) {
		System.out.println( "Returned " + list.size() + " rows:" );
		for ( Object[] o : list ) {
			System.out.println( "  " + Arrays.toString( o ) );
		}
	}
}
