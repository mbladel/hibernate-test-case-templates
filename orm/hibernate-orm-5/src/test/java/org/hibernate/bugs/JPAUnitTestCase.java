package org.hibernate.bugs;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static javax.persistence.CascadeType.ALL;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Poi poi = new Poi( 1L, "poi_1" );
		entityManager.persist( poi );

		final Date now = new Date();
		final Trip trip = new Trip( 1L, new Location( now, poi ), new Location( now, null ) );
		entityManager.persist( trip );

		final Report report = new Report( 1L );
		report.getReportTripList().add( new ReportTrip( new ReportTripId( report, trip ), "other" ) );
		entityManager.persist( report );

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

//		final Trip trip = entityManager.find( Trip.class, 1L );
//		assertThat( trip.getPosition1() ).isNotNull();
//		assertThat( trip.getPosition2() ).isNotNull();

		// When

		final Report report = entityManager.find( Report.class, 1L );
		assertThat( report.getReportTripList().get( 0 ).getCompositeKey().getTrip().getPosition1() ).isNotNull();
		assertThat( report.getReportTripList().get( 0 ).getCompositeKey().getTrip().getPosition2() ).isNotNull(); // ko

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Embeddable
	public static class Location {
		private Date date;
		@ManyToOne
		private Poi poi;

		public Location() {
		}

		public Location(Date date, Poi poi) {
			this.date = date;
			this.poi = poi;
		}
	}

	@Entity( name = "Poi" )
	public static class Poi {
		@Id
		private Long id;
		private String name;

		public Poi() {
		}

		public Poi(Long id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Entity( name = "Report" )
	public static class Report {
		@Id
		private Long id;

		@OneToMany( mappedBy = "compositeKey.report", cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER )
		private List<ReportTrip> reportTripList = new ArrayList<>();

		public Report() {
		}

		public Report(Long id) {
			this.id = id;
		}

		public List<ReportTrip> getReportTripList() {
			return reportTripList;
		}
	}

	@Embeddable
	public static class ReportTripId implements Serializable {
		@ManyToOne
		@JoinColumn( name = "report_id_fk" )
		private Report report;

		@ManyToOne
		@JoinColumn( name = "trip_id_fk" )
		private Trip trip;

		public ReportTripId() {
		}

		public ReportTripId(Report report, Trip trip) {
			this.report = report;
			this.trip = trip;
		}

		public Trip getTrip() {
			return trip;
		}
	}


	@Entity( name = "ReportTrip" )
	public static class ReportTrip {
		@EmbeddedId
		private ReportTripId compositeKey = new ReportTripId();

		private String other;

		public ReportTrip() {
		}

		public ReportTrip(ReportTripId compositeKey, String other) {
			this.compositeKey = compositeKey;
			this.other = other;
		}

		public ReportTripId getCompositeKey() {
			return compositeKey;
		}
	}

	@Entity( name = "Trip" )
	public static class Trip {
		@Id
		private Long id;

		@Embedded
		@AttributeOverrides( {
				@AttributeOverride( name = "date", column = @Column( name = "date_position1" ) ),
		} )
		@AssociationOverride( name = "poi", joinColumns = @JoinColumn( name = "poi1_id_fk" ) )
		private Location position1 = new Location();

		@Embedded
		@AttributeOverrides( {
				@AttributeOverride( name = "date", column = @Column( name = "date_position2" ) ),
		} )
		@AssociationOverride( name = "poi", joinColumns = @JoinColumn( name = "poi2_id_fk" ) )
		private Location position2 = new Location();

		public Trip() {
		}

		public Trip(Long id, Location position1, Location position2) {
			this.id = id;
			this.position1 = position1;
			this.position2 = position2;
		}

		public Location getPosition1() {
			return position1;
		}

		public Location getPosition2() {
			return position2;
		}
	}
}
