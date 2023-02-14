package org.hibernate.bugs;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	private static final long THING1_ID = 1000L;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		Thing thing = new Thing();
		thing.setPk(THING1_ID);

		ThingStats stats = new ThingStats();
		stats.setThingPk(thing.getPk());
		stats.setCount(10);
		stats.setRejectedCount(5);

		thing.setThingStats(stats);

		em.persist(thing);
		em.persist(stats);
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
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		// jakarta.persistence.PersistenceException: Converting `org.hibernate.sql.ast.tree.from.UnknownTableReferenceException` to JPA `PersistenceException` : Unable to determine TableReference (`Thing`) for `org.hibernate.bugs.entities.Thing(thing).thingStats(thingStats)`
		String ql = "select thing from Thing thing"
				+ " left join thing.thingStats thingStats "
				+ " where thingStats is null or thingStats.rejectedCount = 0";

		TypedQuery<Thing> q = entityManager.createQuery( ql, Thing.class);
		List<Thing> results = q.getResultList();  // <<---  Throws PersistenceException

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity(name="Thing")
	public static class Thing {
		@Id
		@Column(name = "THING_PK")
		Long pk;

		@OneToOne(targetEntity = ThingStats.class, fetch = FetchType.EAGER, optional = true)
		@JoinColumn(name = "THING_PK")
		private ThingStats thingStats;

		public Long getPk() {
			return pk;
		}

		public void setPk(Long pk) {
			this.pk = pk;
		}

		public void setThingStats(ThingStats thingStats) {
			this.thingStats = thingStats;
		}

		public ThingStats getThingStats() {
			return thingStats;
		}
	}

	@Entity(name="ThingStats")
	public static class ThingStats {
		@Id
		@Column(name = "THING_FK", nullable = false)
		private Long thingPk;

		@Column(name = "COUNT", nullable = false)
		private long count;

		@Column(name = "REJECTED_COUNT", nullable = false)
		private long rejectedCount;

		public Long getThingPk() {
			return thingPk;
		}

		public void setThingPk(Long thingPk) {
			this.thingPk = thingPk;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long sopCount) {
			this.count = sopCount;
		}

		public long getRejectedCount() {
			return rejectedCount;
		}

		public void setRejectedCount(long rejectedCount) {
			this.rejectedCount = rejectedCount;
		}

	}
}
