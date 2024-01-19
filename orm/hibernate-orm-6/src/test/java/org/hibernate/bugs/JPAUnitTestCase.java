package org.hibernate.bugs;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {
	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );

		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		int id = 0;
		insertMyAssociatedEntity( em, ++id );
		insertMyAssociatedEntity( em, ++id );

		id = 0;
		insertMyEntity( em, ++id, 1L, null );
		insertMyEntity( em, ++id, 1L, 1L );
		insertMyEntity( em, ++id, 1L, 2L );
		insertMyEntity( em, ++id, 2L, null );
		insertMyEntity( em, ++id, 2L, 1L );
		insertMyEntity( em, ++id, 2L, 2L );
		insertMyEntity( em, ++id, null, null );
		insertMyEntity( em, ++id, null, 1L );
		insertMyEntity( em, ++id, null, 2L );

		em.getTransaction().commit();
		em.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh17638Test() throws Exception {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		List<MyEntity> selectResult = select( em );
		long countResult = count( em );

		em.getTransaction().commit();
		em.close();

		assertEquals( selectResult.size(), countResult, "count result doesn't match select result size" );
	}

	private static void insertMyAssociatedEntity(final EntityManager em, final long id) {
		em.createNativeQuery( String.format( "insert into MYASSOCIATEDENTITY (ID) values (%d)", id ) ).executeUpdate();
	}

	private static void insertMyEntity(
			final EntityManager em, final long id, final Long primaryId, final Long secondaryId) {
		em.createNativeQuery( String.format(
				"insert into MYENTITY (ID, PRIMARY_ASSOCIATED_ENTITY_ID, SECONDARY_ASSOCIATED_ENTITY_ID) values(%d, %d, %d)",
				id,
				primaryId,
				secondaryId
		) ).executeUpdate();
	}

	/*
	SELECT e FROM MyEntity e
		WHERE e.primaryEntity IS NULL OR e.secondaryEntity = :secondary

	Hibernate:
		select
			me1_0.ID,
			me1_0.PRIMARY_ASSOCIATED_ENTITY_ID,
			me1_0.SECONDARY_ASSOCIATED_ENTITY_ID
		from
			MYENTITY me1_0
		left join
			MYASSOCIATEDENTITY pe1_0
				on pe1_0.ID=me1_0.PRIMARY_ASSOCIATED_ENTITY_ID
		left join
			MYASSOCIATEDENTITY se1_0
				on se1_0.ID=me1_0.SECONDARY_ASSOCIATED_ENTITY_ID
		where
			pe1_0.ID is null
			or se1_0.ID=?
	 */
	private static List<MyEntity> select(final EntityManager em) {
		return query( em, "e", MyEntity.class ).getResultList();
	}

	/*
	SELECT COUNT(e) FROM MyEntity e
		WHERE e.primaryEntity IS NULL OR e.secondaryEntity = :secondary

	Hibernate:
		select
			count(me1_0.ID)
		from
			MYENTITY me1_0
		left join
			MYASSOCIATEDENTITY pe1_0
				on pe1_0.ID=me1_0.PRIMARY_ASSOCIATED_ENTITY_ID
		join
			MYASSOCIATEDENTITY se1_0
				on se1_0.ID=me1_0.SECONDARY_ASSOCIATED_ENTITY_ID
		where
			pe1_0.ID is null
			or se1_0.ID=?
	 */
	private static long count(final EntityManager em) {
		return query( em, "COUNT(e)", Long.class ).getSingleResult();
	}

	private static <T> TypedQuery<T> query(
			final EntityManager em, final String selectClause, final Class<T> returnType) {
		String query = String.format(
				"SELECT %s FROM MyEntity e " + "WHERE e.primary IN :primary OR e.secondary IS NULL",
				selectClause
		);
		return em.createQuery( query, returnType ).setParameter(
				"primary",
				Collections.singletonList( em.find(
						MyAssociatedEntity.class,
						1L
				) )
		);

	}

}
