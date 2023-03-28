package org.hibernate.bugs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	public void hhh123Test() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		PartyEntity partyEntity1 = new PartyEntity();
		partyEntity1.setPartyCode( "party_01" );
		partyEntity1.setSeqNo( "seq_01" );
		partyEntity1.setRefNo( "ref_01" );

		GuaranteeEntity guaranteeEntity = new GuaranteeEntity();
		guaranteeEntity.setRefNo( "ref_01" );
		guaranteeEntity.setSeqNo( "seq_01" );
		Set<PartyEntity> setOfParties = new HashSet<PartyEntity>();
		setOfParties.add( partyEntity1 );
		guaranteeEntity.setParties( setOfParties );

		entityManager.persist( guaranteeEntity );

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static class InProcessPartyId implements Serializable {
		private String refNo;
		private String seqNo;
		private String partyCode;

		@Column( name = "REF_NO_AAA" )
		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		@Column( name = "SEQ_NO" )
		public String getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(String seqNo) {
			this.seqNo = seqNo;
		}

		@Column( name = "PARTY_CODE" )
		public String getPartyCode() {
			return partyCode;
		}

		public void setPartyCode(String partyCode) {
			this.partyCode = partyCode;
		}
	}


	@Entity
	@Table( name = "parties" )
	@IdClass( InProcessPartyId.class )
	public static class PartyEntity {
		private String refNo;
		private String seqNo;
		private String partyCode;

//		private InProcessGuaranteeEntity guarantee;

		@Id
		@Column( name = "REF_NO_AAA" )
		public String getRefNo() {
			return refNo;
		}


		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		@Id
		@Column( name = "SEQ_NO" )
		public String getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(String seqNo) {
			this.seqNo = seqNo;
		}

		@Id
		@Column( name = "PARTY_CODE" )
		public String getPartyCode() {
			return partyCode;
		}

		public void setPartyCode(String partyCode) {
			this.partyCode = partyCode;
		}
	}

	public static class InProcessRefSeqNoId implements Serializable {
		private String refNo;
		private String seqNo;

		@Column( name = "REF_NO" )
		@Id
		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		@Column( name = "SEQ_NO" )
		@Id
		public String getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(String seqNo) {
			this.seqNo = seqNo;
		}
	}


	@Entity
	@Table( name = "guarantees" )
	@IdClass( InProcessRefSeqNoId.class )
	public static class GuaranteeEntity {
		private String refNo;
		private String seqNo;
		private Set<PartyEntity> parties;

		@Id
		@Column( name = "REF_NO" )
		public String getRefNo() {
			return refNo;
		}

		public void setRefNo(String refNo) {
			this.refNo = refNo;
		}

		@Id
		@Column( name = "SEQ_NO" )
		public String getSeqNo() {
			return seqNo;
		}

		public void setSeqNo(String seqNo) {
			this.seqNo = seqNo;
		}

		@OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
		@JoinColumns( {
				@JoinColumn( name = "REF_NO", referencedColumnName = "REF_NO" ),
				@JoinColumn( name = "SEQ_NO", referencedColumnName = "SEQ_NO" )
		} )
		public Set<PartyEntity> getParties() {
			return parties;
		}

		public void setParties(Set<PartyEntity> parties) {
			this.parties = parties;
		}
	}
}
