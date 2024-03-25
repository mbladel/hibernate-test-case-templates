package org.hibernate.bugs;

import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final SalesHeaderKey primaryKey = new SalesHeaderKey();
		primaryKey.setDocumentType( 1 );
		primaryKey.setNo( "ME-24000029" );
		final SalesHeader header = new SalesHeader( primaryKey );
		entityManager.persist( header );

		final SalesHeaderExtensionKey extensionKey = new SalesHeaderExtensionKey();
		extensionKey.setDocumentType( 1 );
		extensionKey.setNo( "ME-24000029" );
		final SalesHeaderExtension extension = new SalesHeaderExtension( extensionKey, header );
//		final SalesHeaderExtension extension = new SalesHeaderExtension( 1L, header );
		entityManager.persist( extension );

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
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final SalesHeaderKey primaryKey = new SalesHeaderKey();
		primaryKey.setDocumentType( 1 );
		primaryKey.setNo( "ME-24000029" );
		final SalesHeader header = entityManager.find( SalesHeader.class, primaryKey );

		final SalesHeaderExtension extension = header.getExtension();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "SalesHeader" )
	static class SalesHeader {
		@EmbeddedId
		private SalesHeaderKey key;

		@OneToOne( mappedBy = "header", fetch = FetchType.LAZY )
		private SalesHeaderExtension extension;

		public SalesHeader() {
		}

		public SalesHeader(SalesHeaderKey key) {
			this.key = key;
			this.extension = extension;
		}

		public SalesHeaderExtension getExtension() {
			return extension;
		}
	}

	@Embeddable
	static class SalesHeaderKey implements Serializable {
		@Basic( optional = false )
		@Column( name = "doc_type", nullable = false )
		private int documentType;

		@Basic( optional = false )
		@Column( name = "No_", length = 20, nullable = false )
		private String no;

		public int getDocumentType() {
			return documentType;
		}

		public void setDocumentType(int documentType) {
			this.documentType = documentType;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}
	}

	@Entity( name = "SalesHeaderExtension" )
	static class SalesHeaderExtension {
		@EmbeddedId
		private SalesHeaderExtensionKey key;
//		@Id
//		private Long id;

		@OneToOne( optional = false, fetch = FetchType.LAZY )
		@JoinColumns( {
				@JoinColumn( name = "doc_type", referencedColumnName = "doc_type", nullable = false,
						insertable = false, updatable = false ),
				@JoinColumn( name = "No_", referencedColumnName = "No_", nullable = false,
						insertable = false, updatable = false )
		} )
		private SalesHeader header;

		public SalesHeaderExtension() {
		}

		public SalesHeaderExtension(SalesHeaderExtensionKey key, SalesHeader header) {
			this.key = key;
			this.header = header;
		}

//		public SalesHeaderExtension(Long id, SalesHeader header) {
//			this.id = id;
//			this.header = header;
//		}
	}

	@Embeddable
	static class SalesHeaderExtensionKey implements Serializable {
		@Basic( optional = false )
		@Column( name = "doc_type", nullable = false )
		private int documentType;

		@Basic( optional = false )
		@Column( name = "No_", length = 20, nullable = false )
		private String no;

		public int getDocumentType() {
			return documentType;
		}

		public void setDocumentType(int documentType) {
			this.documentType = documentType;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}
	}
}
