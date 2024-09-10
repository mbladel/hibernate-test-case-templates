package org.hibernate.bugs;

import java.util.Arrays;
import java.util.List;

import org.hibernate.query.criteria.JpaRoot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@BeforeEach
	void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}


	private void executeQuery(EntityManager em, String queryStr, String[] expectedNames) {
		executeQuery( em.createQuery( queryStr ), expectedNames );
	}

	private void executeQuery(EntityManager em, CriteriaQuery<AbstractObject> query, String[] expectedNames) {
		executeQuery( em.createQuery( query ), expectedNames );
	}

	private void executeQuery(Query query, String[] expectedNames) {
		List<AbstractObject> result = query.getResultList();
		Assertions.assertArrayEquals(
				Arrays.stream( expectedNames ).sorted().toArray( String[]::new ),
				result.stream().map( AbstractObject::getName ).sorted().toArray( String[]::new )
		);
	}

	private void createData(EntityManager em) {
		em.getTransaction().begin();
		ConcreteObject1 co1 = new ConcreteObject1();
		co1.setName( "Object1" );
		co1.setLocation( "Here" );
		em.persist( co1 );

		ConcreteObject11 co11 = new ConcreteObject11();
		co11.setName( "Object11" );
		co11.setLocation( "Anywhere" );
		em.persist( co11 );

		ConcreteObject2 co2 = new ConcreteObject2();
		co2.setName( "Object2" );
		co2.setNickname( "Yet another object" );
		em.persist( co2 );

		em.getTransaction().commit();
	}

	@Test
	void hhh18583TestJpql() {
		try (EntityManager em = entityManagerFactory.createEntityManager()) {
			createData( em );

			// just to check, it works without type filtering
//			executeQuery(
//					em,
//					"select ao from AbstractObject ao where name is not null",
//					new String[] {
//							"Object1",
//							"Object11",
//							"Object2"
//					}
//			);

			// restrict to ConcreteObject1 directly
//			executeQuery(
//					em,
//					"select treat(ao as ConcreteObject1) from AbstractObject ao",
//					new String[] {
//							"Object1",
//							"Object11"
//					}
//			);

			// restrict to ConcreteObject1 indirectly
			executeQuery(
					em,
					"select ao from AbstractObject ao where treat(ao as ConcreteObject1).name is not null",
					new String[] {
							"Object1",
							"Object11"
					}
			);
		}
	}

	@Test
	void hhh18583TestCriteriaQuery() {
		try (EntityManager em = entityManagerFactory.createEntityManager()) {
			createData( em );

			// just to check, it works without type filtering
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AbstractObject> cq = cb.createQuery( AbstractObject.class );
			JpaRoot<AbstractObject> root = (JpaRoot<AbstractObject>) cq.from( AbstractObject.class );
			cq.select( root );
			cq.where( cb.isNotNull( cq.from( AbstractObject.class ).get( "name" ) ) );
			executeQuery(
					em,
					cq,
					new String[] {
							"Object1",
							"Object11",
							"Object2"
					}
			);

			// restrict to ConcreteObject1 directly
			cb = em.getCriteriaBuilder();
			cq = cb.createQuery( AbstractObject.class );
			root = (JpaRoot<AbstractObject>) cq.from( AbstractObject.class );
			cq.select( root.treatAs( ConcreteObject1.class ) );
			executeQuery(
					em,
					cq,
					new String[] {
							"Object1",
							"Object11"
					}
			);

			// restrict to ConcreteObject1 indirectly
			cb = em.getCriteriaBuilder();
			cq = cb.createQuery( AbstractObject.class );
			root = (JpaRoot<AbstractObject>) cq.from( AbstractObject.class );
			cq.select( root );
			cq.where( cb.isNotNull( root.treatAs( ConcreteObject1.class ).get( "name" ) ) );
			executeQuery(
					em,
					cq,
					new String[] {
							"Object1",
							"Object11"
					}
			);
		}
	}

	@Entity( name = "AbstractObject" )
	@Inheritance( strategy = InheritanceType.JOINED )
	@DiscriminatorColumn( name = "entityType" )
	public static abstract class AbstractObject {
		@Id
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Entity( name = "ConcreteObject1" )
	@DiscriminatorValue( "concreteObject1" )
	public static class ConcreteObject1 extends AbstractObject {
		private String location;

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
	}

	@Entity( name = "ConcreteObject2" )
	@DiscriminatorValue( "concreteObject2" )
	public static class ConcreteObject2 extends AbstractObject {
		private String nickname;

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
	}

	@Entity( name = "ConcreteObject11" )
	@DiscriminatorValue( "concretObject11" )
	public static class ConcreteObject11 extends ConcreteObject1 {
	}
}
