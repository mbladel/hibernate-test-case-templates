package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;

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

		final EntityC entityC1 = new EntityC( "entityc_1" );
		final EntityC entityC2 = new EntityC( "entityc_2" );
		final EntityC entityC3 = new EntityC( "entityc_3" );
		final EntityA entityA1 = new EntityA( 1, new EmbeddedValue( "1", Arrays.asList( entityC1, entityC2 ) ) );
		final EntityA entityA2 = new EntityA( 2, new EmbeddedValue( "2", Arrays.asList( entityC3 ) ) );
		entityManager.persist( entityA1 );
		entityManager.persist( entityA2 );

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

		final List<EntityC> result = entityManager.createQuery(
				"select a.entityCList from EntityA a where a.id = 2",
				EntityC.class
		).getResultList();
//		assertThat( result.getEmbedded().getEntityCList() ).hasSize( 1 );
//		assertThat( embedded.getEntityCList() ).hasSize( 1 );
//		assertThat( embedded.getEntityCList().stream().map( EntityC::getName ) ).containsOnly( "entityc_3" );
		// test orphan removal
//		embedded.getEntityCList().clear();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Entity( name = "EntityA" )
	public static class EntityA {
		@Id
		private Integer id;

		@ManyToMany( cascade = CascadeType.PERSIST )
		private List<EntityC> entityCList;

		public EntityA() {
		}

		private EntityA(Integer id, EmbeddedValue embedded) {
			this.id = id;
//			this.name = name;
			this.entityCList = embedded.getEntityCList();
		}

		public Integer getId() {
			return id;
		}

		public List<EntityC> getEntityCList() {
			return entityCList;
		}
	}

	@Embeddable
	public static class EmbeddedValue implements Serializable {
		private String embeddedProp;

		@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
		@JoinColumn( name = "entityA_id" )
		private List<EntityC> entityCList;

		public EmbeddedValue() {
		}

		public EmbeddedValue(String embeddedProp, List<EntityC> entityCList) {
			this.embeddedProp = embeddedProp;
			this.entityCList = entityCList;
		}

		public List<EntityC> getEntityCList() {
			return entityCList;
		}
	}

	@Entity( name = "EntityC" )
	public static class EntityC {
		@Id
		@GeneratedValue
		private Integer id;

		private String name;

		public EntityC() {
		}

		public EntityC(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
