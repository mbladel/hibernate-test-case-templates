package org.hibernate.bugs;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.type.descriptor.java.LongJavaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

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

		final DocClientEntity docClientEntity = new DocClientEntity();
		docClientEntity.setName( "test doc client" );
		entityManager.persist( docClientEntity );
		entityManager.flush();

		final DocumentEntity documentEntity = new DocumentEntity();
		documentEntity.setName( "test doc" );
		documentEntity.setParent( docClientEntity );
		entityManager.persist( documentEntity );
		entityManager.flush();

		Query query = entityManager.createQuery( " select id, name, parent from DocumentEntity " );

		query.getResultList();


		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public interface IDocumentEntity {
		Long getId();

		String getName();
	}

	@Entity( name = "DocumentEntity" )
	@Table( name = "`DOCUMENT`" )
	@DynamicUpdate()
	public static class DocumentEntity implements IDocumentEntity {

		@Id
		@Column( name = "`ID`", nullable = false )
		@Access( AccessType.PROPERTY )
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@Column( name = "`NAME`", nullable = false )
		private String name;

		@Any( fetch = FetchType.LAZY )
		@JoinColumn( name = "`PARENTID`" )
		@Column( name = "PARENTTABLE" )
		@AnyKeyJavaType( value = LongJavaType.class )
		@AnyDiscriminatorValue( discriminator = "DOCUMENT", entity = DocumentEntity.class )
		@AnyDiscriminatorValue( discriminator = "DOCCLIENT", entity = DocClientEntity.class )
		private IDocumentEntity parent;

		@Override
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public IDocumentEntity getParent() {
			return parent;
		}

		public void setParent(IDocumentEntity parent) {
			this.parent = parent;
		}
	}

	@Entity( name = "DocClientEntity" )
	@Table( name = "`DOCCLIENT`" )
	@DynamicUpdate()
	public static class DocClientEntity implements IDocumentEntity {

		@Id
		@Column( name = "`ID`", nullable = false )
		@Access( AccessType.PROPERTY )
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@Column( name = "`NAME`", nullable = false )
		private String name;

		@Override
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
