package org.hibernate.bugs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.Assert.assertNotNull;

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

		final Form form = new Form();
		entityManager.persist( form );

		final FormVersion formVersion = new FormVersion( form, 1 );

		entityManager.persist( formVersion );

		stepConfiguration = new StepConfiguration();

		formOption = new FormOption();
		formOption.setConfiguration( stepConfiguration );
		formOption.setFormVersion( formVersion );
		Set<FormOption> formOptions = new HashSet<>();
		formOptions.add( formOption );
		stepConfiguration.setFormOptions( formOptions );

		entityManager.persist( stepConfiguration );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	private StepConfiguration stepConfiguration;

	private FormOption formOption;

	@Test
	public void formVersionLoadTest() {
		doInJPA( () -> entityManagerFactory, entityManager -> {
			Query fetchQuery = entityManager.createQuery( "SELECT s FROM StepConfiguration s WHERE s.id = :id" );
			fetchQuery.setParameter( "id", stepConfiguration.getId() );
			StepConfiguration configuration = (StepConfiguration) fetchQuery.getSingleResult();

			assertNotNull( configuration.getFormOptions() );
			Assert.assertEquals( 1, configuration.getFormOptions().size() );
			configuration.getFormOptions().forEach( formOption -> {
				FormVersion fv = formOption.getFormVersion();
				assertNotNull( fv );
			} );
		} );
	}

	@Test
	public void formOptionsLoadTest() {
		doInJPA( () -> entityManagerFactory, entityManager -> {
			Query fetchQuery = entityManager.createQuery( "SELECT fo FROM FormOption fo" );
			List<FormOption> fos = (List<FormOption>) fetchQuery.getResultList();
			Assert.assertEquals( 1, fos.size() );
			fos.forEach( formOption -> {
				FormVersion fv = formOption.getFormVersion();
				assertNotNull( fv );
			} );
		} );
	}

	@Test
	public void singleFormOptionLoadTest() {
		doInJPA( () -> entityManagerFactory, entityManager -> {
			Query fetchQuery = entityManager.createQuery( "SELECT fo FROM FormOption fo WHERE fo.id = :id" );
			fetchQuery.setParameter( "id", formOption.getId() );
			FormOption fo = (FormOption) fetchQuery.getSingleResult();
			FormVersion fv = fo.getFormVersion();
			assertNotNull( fv );
		} );
	}

	@Entity( name = "lForm" )
	public static class Form {
		@OneToMany( mappedBy = "id.form" )
		private List<FormVersion> versions;

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		protected Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public List<FormVersion> getVersions() {
			return versions;
		}

		public void setVersions(List<FormVersion> versions) {
			this.versions = versions;
		}
	}

	@Entity( name = "FormOption" )
	public static class FormOption {
		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		@ManyToOne( optional = false )
		@JoinColumn( name = "stepConfiguration_id", foreignKey = @ForeignKey( name = "FK_FormOption_StepConfiguration" ) )
		private StepConfiguration configuration;

		@ManyToOne( optional = false )
		@JoinColumns( foreignKey = @ForeignKey( name = "FK_FormOption_FormVersion" ), value = {
				@JoinColumn( name = "form_id", updatable = false ),
				@JoinColumn( name = "versionNumber", updatable = false )
		} )
		private FormVersion formVersion;

		public FormOption() {
		}

		public FormOption(FormVersion formVersion) {
			this.formVersion = formVersion;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public StepConfiguration getConfiguration() {
			return configuration;
		}

		public void setConfiguration(StepConfiguration configuration) {
			this.configuration = configuration;
		}

		public FormVersion getFormVersion() {
			return formVersion;
		}

		public void setFormVersion(FormVersion formVersion) {
			this.formVersion = formVersion;
		}
	}

	@Entity( name = "FormVersion" )
	public static class FormVersion {
		@EmbeddedId
		protected FormVersionId id;

		public FormVersionId getId() {
			return id;
		}

		public void setId(FormVersionId id) {
			this.id = id;
		}

		public FormVersion() {
			id = new FormVersionId();
		}

		public FormVersion(Form form, int version) {
			this();
			this.id.setForm( form );
			this.id.setVersionNumber( version );
		}
	}

	@Embeddable
	public static class FormVersionId implements Serializable {
		@ManyToOne
		@JoinColumn( name = "form_id" )
		private Form form;

		private Integer versionNumber;

		public Form getForm() {
			return form;
		}

		public void setForm(Form form) {
			this.form = form;
		}

		public Integer getVersionNumber() {
			return versionNumber;
		}

		public void setVersionNumber(Integer versionNumber) {
			this.versionNumber = versionNumber;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( !( o instanceof FormVersionId ) ) {
				return false;
			}
			FormVersionId that = (FormVersionId) o;
			return form.getId().equals( that.form.getId() ) && versionNumber.equals( that.versionNumber );
		}

		@Override
		public int hashCode() {
			return Objects.hash( form, versionNumber );
		}
	}

	@Entity( name = "StepConfiguration" )
	public static class StepConfiguration {
		@OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "configuration", fetch = FetchType.EAGER )
		private Set<FormOption> formOptions;

		@Id
		@GeneratedValue( strategy = GenerationType.IDENTITY )
		private Long id;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Set<FormOption> getFormOptions() {
			return formOptions;
		}

		public void setFormOptions(Set<FormOption> formOptions) {
			this.formOptions = formOptions;
		}
	}
}
