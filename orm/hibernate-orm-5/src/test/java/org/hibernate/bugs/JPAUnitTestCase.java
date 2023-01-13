package org.hibernate.bugs;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh15916Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId( 1L );
		vehicle.setStringProp( "2020" );
		entityManager.persist( vehicle );

		VehicleInvoice invoice = new VehicleInvoice();
		invoice.setId( "2020".toCharArray() );
		invoice.setVehicle( vehicle );
		entityManager.persist( invoice );

		entityManager.getTransaction().commit();
		entityManager.close();

		EntityManager em2 = entityManagerFactory.createEntityManager();
		em2.getTransaction().begin();

		List<VehicleInvoice> resultList = em2.createQuery(
				"from VehicleInvoice",
				VehicleInvoice.class
		).getResultList();
		assertEquals( 1, resultList.size() );
		assertEquals( 1L, resultList.get( 0 ).getVehicle().getVehicleId().longValue() );

		em2.getTransaction().commit();
		em2.close();
	}

	@Entity(name = "VehicleInvoice")
	public static class VehicleInvoice {
		@Id
		@Column(name = "char_array_col", length = 4, nullable = false)
		private char[] id;

		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "char_array_col", referencedColumnName = "string_col", insertable = false, updatable = false)
		private Vehicle vehicle;

		public char[] getId() {
			return id;
		}

		public void setId(char[] field2) {
			this.id = field2;
		}

		public Vehicle getVehicle() {
			return vehicle;
		}

		public void setVehicle(Vehicle vehicle) {
			this.vehicle = vehicle;
		}
	}

	@Entity(name = "Vehicle")
	public static class Vehicle implements Serializable {
		@Id
		private Long vehicleId;

		@Column(name = "string_col", nullable = false)
		private String stringProp;

		public Long getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(Long vehicleId) {
			this.vehicleId = vehicleId;
		}

		public String getStringProp() {
			return stringProp;
		}

		public void setStringProp(String field2) {
			this.stringProp = field2;
		}
	}
}
