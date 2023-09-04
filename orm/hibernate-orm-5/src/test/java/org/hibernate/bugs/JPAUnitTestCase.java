package org.hibernate.bugs;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

		entityManager.persist( new City(
				"London",
				"England",
				new Country( "United Kingdom", "GBR" )
		) );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void testNullLiteralConstruct() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<CityProjection> query = cb.createQuery( CityProjection.class );
		final Root<City> cityRoot = query.from( City.class );
		query.select( cb.construct(
				CityProjection.class,
				cityRoot.get( "id" ),
				cityRoot.get( "name" ),
				cityRoot.get( "state" ),
				cb.nullLiteral( Country.class )
		) );
		final CityProjection result = entityManager.createQuery( query ).getSingleResult();
		assertThat( result.getName() ).isEqualTo( "London" );
		assertThat( result.getCountryName() ).isNull();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "City" )
	public static class City {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		private String state;

		@ManyToOne( cascade = CascadeType.PERSIST )
		@JoinColumn( name = "country_id" )
		private Country country;

		public City() {
		}

		public City(String name, String state, Country country) {
			this.name = name;
			this.state = state;
			this.country = country;
		}
	}

	public static class CityProjection {
		private Long id;
		private String name;
		private String state;
		private String countryName;

		public CityProjection(Long id, String name, String state, Country country) {
			this.id = id;
			this.name = name;
			this.state = state;
			if ( country != null ) {
				this.countryName = country.getName();
			}
		}

		public String getName() {
			return name;
		}

		public String getCountryName() {
			return countryName;
		}
	}


	@Entity( name = "Country" )
	public static class Country {
		@Id
		@GeneratedValue
		private Long id;

		private String name;

		private String isoCode;

		@OneToMany( mappedBy = "country" )
		private List<City> cities;

		public Country() {
		}

		public Country(String name, String isoCode) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}

