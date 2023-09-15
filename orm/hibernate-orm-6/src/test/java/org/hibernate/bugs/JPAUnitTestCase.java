package org.hibernate.bugs;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaType;
import org.hibernate.type.descriptor.java.LongJavaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Persistence;

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

		final DocClientEntity docClientEntity = new DocClientEntity( 1L, "test_client" );
		entityManager.persist( docClientEntity );
		entityManager.flush();

		final DocumentEntity documentEntity = new DocumentEntity( 2L, "test_doc" );
		documentEntity.setParent( docClientEntity );
		entityManager.persist( documentEntity );
		entityManager.flush();

		final int count = entityManager.createQuery(
				"insert into DocumentEntity(name, parent) select name, parent from DocumentEntity"
		).executeUpdate();

		assertThat( count ).isEqualTo( 1 );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public interface IDocumentEntity {
		Long getId();

		String getName();
	}

	@Entity( name = "DocumentEntity" )
	public static class DocumentEntity implements IDocumentEntity {
		@Id
		private Long id;

		private String name;

		@Any( fetch = FetchType.LAZY )
		@JoinColumn( name = "parent_id" )
		@Column( name = "parent_type" )
		@AnyKeyJavaType( value = LongJavaType.class )
		@AnyDiscriminatorValue( discriminator = "document", entity = DocumentEntity.class )
		@AnyDiscriminatorValue( discriminator = "doc_client", entity = DocClientEntity.class )
		private IDocumentEntity parent;

		public DocumentEntity() {
		}

		public DocumentEntity(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		public IDocumentEntity getParent() {
			return parent;
		}

		public void setParent(IDocumentEntity parent) {
			this.parent = parent;
		}
	}

	@Entity( name = "DocClientEntity" )
	public static class DocClientEntity implements IDocumentEntity {
		@Id
		private Long id;

		private String name;

		public DocClientEntity() {
		}

		public DocClientEntity(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
