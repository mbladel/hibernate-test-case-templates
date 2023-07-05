package org.hibernate.bugs;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.bugs.domain.C1;
import org.hibernate.bugs.domain.C1Set;
import org.hibernate.bugs.domain.Ca;
import org.hibernate.bugs.domain.Cr;
import org.hibernate.bugs.domain.Kp;
import org.hibernate.bugs.domain.KpSet;
import org.hibernate.bugs.domain.Pk;
import org.hibernate.bugs.domain.Ra;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private static final int NO_OF_C1SETS_TO_CREATE = 200;
	private final static SecureRandom secureRandom = new SecureRandom();

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final long start = System.nanoTime();
		Ca[] cas = insertCas( entityManager );
		Ra[] ras = insertRas( entityManager, cas );
		KpSet[] kpSets = insertKpSets( entityManager );
		insertC1Sets( entityManager, cas, ras, kpSets );
		System.out.println( "Persisting took: " + ( ( System.nanoTime() - start ) / 1e6 ) + " ms" );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private Ca[] insertCas(EntityManager entityManager) {
		Ca[] cas = new Ca[10];

		for ( int i = 0; i < cas.length; ++i ) {
			byte[] bytes = new byte[1000];

			secureRandom.nextBytes( bytes );

			Ca ca = new Ca();

			ca.setName( "ca" + i );
			ca.setLob( Base64.getEncoder().encodeToString( bytes ) );

			entityManager.persist( ca );

			cas[i] = ca;
		}

		return cas;
	}

	private Ra[] insertRas(EntityManager entityManager, Ca[] cas) {
		Ra[] ras = new Ra[5];

		for ( int i = 0; i < ras.length; ++i ) {
			Ra ra = new Ra();

			ra.setName( "ra" + i );
			ra.setCa( new HashSet<>( 2 ) );

			ra.getCa().add( cas[2 * i] );
			ra.getCa().add( cas[2 * i + 1] );

			entityManager.persist( ra );

			ras[i] = ra;
		}

		return ras;
	}

	private KpSet[] insertKpSets(EntityManager entityManager) {
		KpSet[] kpSets = new KpSet[NO_OF_C1SETS_TO_CREATE];

		for ( int i = 0; i < kpSets.length; ++i ) {
			KpSet kpSet = new KpSet();

			kpSet.setName( "kpSet" + i );
			kpSet.setKps( new HashSet<>( 3 ) );

			for ( int j = 0; j < 3; ++j ) {
				byte[] bytes = new byte[200];

				secureRandom.nextBytes( bytes );

				Kp kp = new Kp();

				kp.setName( "kpSet" + i + "kp" + j );
				kp.setPk( new Pk() );

				kp.getPk().setName( "kpSet" + i + "pk" + j );
				kp.getPk().setLob( Base64.getEncoder().encodeToString( bytes ) );
				kp.getPk().setKp( kp );

				kp.setKpSet( kpSet );

				kpSet.getKps().add( kp );
			}

			entityManager.persist( kpSet );

			kpSets[i] = kpSet;
		}

		return kpSets;
	}

	private void insertC1Sets(EntityManager entityManager, Ca[] cas, Ra[] ras, KpSet[] kpSets) {
		for ( int i = 0; i < kpSets.length; ++i ) {
			KpSet kpSet = kpSets[i];

			Kp[] kps = kpSet.getKps().toArray( new Kp[0] );

			C1Set c1Set = new C1Set();

			c1Set.setName( "c1Set" + i );
			c1Set.setCa( cas[i % cas.length] );
			c1Set.setKpSet( kpSet );
			c1Set.setC1s( new HashSet<>( 3 ) );

			byte[] bytes = new byte[3000];

			secureRandom.nextBytes( bytes );

			Cr cr = new Cr();

			cr.setRa( ras[i % ras.length] );
			cr.setLob( Base64.getEncoder().encodeToString( bytes ) );

			entityManager.persist( cr );

			for ( Kp kp : kps ) {
				bytes = new byte[200];

				secureRandom.nextBytes( bytes );

				C1 c1 = new C1();

				c1.setLob( Base64.getEncoder().encodeToString( bytes ) );
				c1.setPk( kp.getPk() );

				c1.getPk().setC1( c1 );

				c1.getPk().setCr( cr );

				c1.setC1Set( c1Set );

				c1Set.getC1s().add( c1 );
			}

			kpSet.setC1Set( c1Set );

			entityManager.persist( c1Set );
		}
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final long start = System.nanoTime();

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<C1Set> cq = cb.createQuery( C1Set.class );
		final Root<C1Set> root = cq.from( C1Set.class );
		final List<C1Set> resultList = entityManager.createQuery( cq.select( root ) ).getResultList();

		System.out.printf( "Loading %d sets took: %f ms%n", resultList.size(), ( System.nanoTime() - start ) / 1e6 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
