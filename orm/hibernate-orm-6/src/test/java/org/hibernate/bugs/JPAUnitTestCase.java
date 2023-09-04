package org.hibernate.bugs;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;


	private static final String NOW = "now";
	private static final String ACTIVE_START_DATE = "activeStartDate";
	private static final String ACTIVE_END_DATE = "activeEndDate";

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Instant now = Instant.now();
		final DateDriven entity = new DateDriven();
		entity.setActiveEndDate( now.minus( 5, ChronoUnit.DAYS ) );
		entity.setActiveStartDate( now.minus( 5, ChronoUnit.DAYS ) );
		entity.setId( "test" );
		entityManager.persist( entity );

		entityManager.getTransaction().commit();
		entityManager.close();
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

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<DateDriven> criteria = builder.createQuery( DateDriven.class );
		final Root<DateDriven> root = criteria.from( DateDriven.class );
		criteria.select( root );
		final List<Predicate> predicates = new ArrayList<>();
		final Map<String, Object> params = new HashMap<>();
		addInactiveDateRangePredicate( builder, root, predicates, params );
		criteria.where( predicates.toArray( new Predicate[0] ) );
		final TypedQuery<DateDriven> query = entityManager.createQuery( criteria );
		params.forEach( query::setParameter );
		final List<DateDriven> resultList = query.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void addInactiveDateRangePredicate(
			CriteriaBuilder builder,
			Root<DateDriven> root,
			List<Predicate> predicates,
			Map<String, Object> params) {
		params.put( NOW, Instant.now() );
//		final ParameterExpression<Instant> nowParameter = builder.parameter( Instant.class, NOW );
		predicates.add( builder.or(
				builder.greaterThanOrEqualTo(
						root.get( ACTIVE_START_DATE ),
						builder.parameter( Instant.class, NOW )
				),
				builder.and(
						builder.isNotNull( root.get( ACTIVE_END_DATE ) ),
						builder.lessThanOrEqualTo(
								root.get( ACTIVE_END_DATE ),
								builder.parameter( Instant.class, NOW )
						)
				)
		) );
	}

	@Entity( name = "DateDriven" )
//	@Inheritance( strategy = InheritanceType.JOINED )
	public static class DateDriven {
		@Id
		private String id;

		private Instant activeStartDate;

		private Instant activeEndDate;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Instant getActiveStartDate() {
			return activeStartDate;
		}

		public void setActiveStartDate(Instant activeStartDate) {
			this.activeStartDate = activeStartDate;
		}

		public Instant getActiveEndDate() {
			return activeEndDate;
		}

		public void setActiveEndDate(Instant activeEndDate) {
			this.activeEndDate = activeEndDate;
		}
	}

	@Converter( autoApply = true )
	public static class InstantConverter implements AttributeConverter<Instant, Timestamp> {
		@Override
		public Timestamp convertToDatabaseColumn(Instant instant) {
			return instant == null ? null : Timestamp.from( instant );
		}

		@Override
		public Instant convertToEntityAttribute(Timestamp timestamp) {
			return timestamp == null ? null : timestamp.toInstant();
		}

	}
}

