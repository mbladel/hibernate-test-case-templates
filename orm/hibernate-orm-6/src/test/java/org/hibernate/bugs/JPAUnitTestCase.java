package org.hibernate.bugs;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.engine.jdbc.BlobProxy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Persistence;

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

		entityManager.persist( new CategoryDetailEntity(
				1L,
				BlobProxy.generateProxy( new byte[] { 1, 2, 3, 4 } )
		) );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@AfterEach
	void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		final Session session = entityManager.unwrap( Session.class );
		// first execution should populate the cache
		executeQuery( session );

		executeQuery( session );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void executeQuery(Session session) {
		session.createSelectionQuery( "from CategoryDetailEntity c where c.id = :cid", CategoryDetailEntity.class )
				.setParameter( "cid", 1L )
				.setCacheable( true )
				.uniqueResultOptional()
				.ifPresent( categoryDetail -> {
					try (InputStream is = categoryDetail.getImage().getBinaryStream()) {
						System.out.println( is == null );
					}
					catch (SQLException e) {
						throw new RuntimeException( e );
					}
					catch (IOException e) {
						throw new RuntimeException( e );
					}
				} );
	}

	@Entity( name = "CategoryDetailEntity" )
	static class CategoryDetailEntity {
		@Id
		private Long id;

		@Lob
		private Blob image;

		public CategoryDetailEntity() {
		}

		public CategoryDetailEntity(Long id, Blob image) {
			this.id = id;
			this.image = image;
		}

		public Long getId() {
			return id;
		}

		public Blob getImage() {
			return image;
		}
	}
}
