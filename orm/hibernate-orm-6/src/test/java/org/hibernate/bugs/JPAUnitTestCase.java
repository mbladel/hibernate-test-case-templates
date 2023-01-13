package org.hibernate.bugs;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

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

//		InvoicingAccountingVehicle vehicle = new InvoicingAccountingVehicle();
//		vehicle.setVehicleId( 1L );
//		vehicle.setField1( "V" );
//		vehicle.setField2( "2020" );
//		entityManager.persist( vehicle );

		VehicleInvoice invoice = new VehicleInvoice();
		invoice.setId( new VehicleInvoiceId( "V".toCharArray(), "2020".toCharArray(), 1 ) );
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

		em2.getTransaction().commit();
		em2.close();
	}

	@Embeddable
	public static class VehicleInvoiceId implements Serializable {
		@Column(name = "INVOICE_FIELD1", length = 2, nullable = false)
		private char[] field1;

		@Column(name = "INVOICE_FIELD2", length = 4, nullable = false)
		private char[] field2;

		@Column(name = "ADDITIONAL_FIELD", nullable = false)
		private Integer additionalField;

		public VehicleInvoiceId() {
		}

		public VehicleInvoiceId(char[] field1, char[] field2, Integer additionalField) {
			this.field1 = field1;
			this.field2 = field2;
			this.additionalField = additionalField;
		}

		public char[] getField1() {
			return field1;
		}

		public void setField1(char[] field1) {
			this.field1 = field1;
		}

		public char[] getField2() {
			return field2;
		}

		public void setField2(char[] field2) {
			this.field2 = field2;
		}

		public Integer getAdditionalField() {
			return additionalField;
		}

		public void setAdditionalField(Integer additionalField) {
			this.additionalField = additionalField;
		}
	}

	@Entity(name = "VehicleInvoice")
	@Table(name = "INVOICE")
	public static class VehicleInvoice {
		@EmbeddedId
		private VehicleInvoiceId id;

		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumnOrFormula(formula = @JoinFormula(value = "trim(INVOICE_FIELD1)", referencedColumnName = "FIELD_1"))
		@JoinColumnOrFormula(column = @JoinColumn(name = "INVOICE_FIELD2", referencedColumnName = "FIELD_2", insertable = false, updatable = false))
		private InvoicingAccountingVehicle vehicle;

		public VehicleInvoiceId getId() {
			return id;
		}

		public void setId(VehicleInvoiceId id) {
			this.id = id;
		}

		public InvoicingAccountingVehicle getVehicle() {
			return vehicle;
		}

		public void setVehicle(InvoicingAccountingVehicle vehicle) {
			this.vehicle = vehicle;
		}
	}

	@Entity(name = "InvoicingAccountingVehicle")
	@Table(name = "INV_VEHICLE")
	public static class InvoicingAccountingVehicle implements Serializable {
		@Id
		@Column(name = "ID", nullable = false)
		private Long vehicleId;

		@Column(name = "FIELD_1", nullable = false, length = 2)
		private String field1;

		@Column(name = "FIELD_2", nullable = false, length = 4)
		private String field2;

		public Long getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(Long vehicleId) {
			this.vehicleId = vehicleId;
		}

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}
	}
}
