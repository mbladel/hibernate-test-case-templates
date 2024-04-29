package org.hibernate.bugs;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

		final Query query = entityManager.createNamedQuery( "EdiCorpAction.findByEventType" )
				.setParameter( "eventCD", Arrays.asList( EdiActionType.LSTAT ) );
		query.getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public enum BbgActionType {
		DELIST,
		CHG_ID,
		CHG_TKR,
		STOCK_SPLT,
		CHG_NAME,
		CHG_LIST,
		CHG_RLOT,
		DVD_STOCK,
		MERG,
		SPIN,
		ACQUIS,
	}

	public enum EdiActionType {

		/* ***************************************
		 * Mapped EDI to BBG Actions
		 * **************************************/

		LSTAT( BbgActionType.DELIST ), TKOVR( BbgActionType.ACQUIS ), DMRGR( BbgActionType.SPIN ), MRGR( BbgActionType.MERG ),

		PRCHG( BbgActionType.CHG_LIST ), ISCHG( BbgActionType.CHG_NAME ), LTCHG( BbgActionType.CHG_RLOT ), LCC(
				BbgActionType.CHG_TKR ), ICC( BbgActionType.CHG_ID ),

		SD( BbgActionType.STOCK_SPLT, 1 ), CONSD( BbgActionType.STOCK_SPLT, 2 ),

		DIV( BbgActionType.DVD_STOCK, 0 ), DIST( BbgActionType.DVD_STOCK, 1 ), BON( BbgActionType.DVD_STOCK, 2 ),


		/* ***************************************
		 * Unmapped EDI Actions
		 * **************************************/
		ANN( null ), ARR( null ), ASSM( null ), BB( null ), BKRP( null ), BR( null ), CALL( null ), CAPRD( null ), AGM(
				null ), CONV( null ),
		CTX( null ), CURRD( null ), DRIP( null ), DVST( null ), ENT( null ), FRANK( null ), FTT( null ), FYCHG( null ), INCHG(
				null ),
		LAWST( null ), LIQ( null ), MKCHG( null ), NLIST( null ), ODDLT( null ), PID( null ), PO( null ), PRF( null ), PVRD(
				null ),
		RCAP( null ), REDEM( null ), RTS( null ), SCCHG( null ), SCSWP( null ), SECRC( null ), POFF( null ), PXOF( null ), PCON(
				null ),
		PRED( null ), PAMO( null ), IDIV( null ),

		;

		final BbgActionType bbgAction;
		final int seqNo;

		private EdiActionType(BbgActionType bbgAction) {
			this.bbgAction = bbgAction;
			this.seqNo = 0;
		}

		private EdiActionType(BbgActionType bbgAction, int seqNo) {
			this.bbgAction = bbgAction;
			this.seqNo = seqNo;
		}

		public BbgActionType getBbgType() {
			return bbgAction;
		}

		public int getSeqNo() {
			return seqNo;
		}

		/* ***************************************
		 * Utility Methods
		 * **************************************/
		public static Collection<EdiActionType> get(Collection<String> ediTypes) {
			return ediTypes.parallelStream().map( EdiActionType::valueOf ).collect( Collectors.toList() );
		}

	}

	@Entity( name = "EdiCorporateAction" )
	@Cacheable
	//@Table( name = "EDI_CORP_ACTION", schema = "SECMASTER" )
	@Cache( usage = CacheConcurrencyStrategy.TRANSACTIONAL )
	@NamedQueries( {

			@NamedQuery( name = "EdiCorpAction.find",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.eventCD=:eventCD AND o.eventID=:eventID AND o.optionID=:optionID "
							+ "AND o.serialID=:serialID AND o.scexhID=:scexhID "
							+ "AND o.bbcID=:bbcID AND o.bbeID=:bbeID" ),

			@NamedQuery( name = "EdiCorpAction.findByEventTypeAndDate",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN (:eventCD) AND o.date1>=:date1" ),

			@NamedQuery( name = "EdiCorpAction.findByEventType",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN (:eventCD)" ),

			@NamedQuery( name = "EdiCorpAction.findByEventTypeAndDateAndBbgFlag",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN (:eventCD)"
							+ " AND (o.date1>=:date1 AND o.id.eventCD IN (:effDateTypes)"
							+ "		 OR (o.id.eventCD IN ('MRGR','DMRGR') AND (o.date2>=:date1 OR o.date3>=:date1))"
							+ "		 OR (o.id.eventCD='TKOVR' AND (o.date3>=:date1 OR o.date5>=:date1))"
							+ "	)"
							+ " AND o.bbgFlag=:bbgFlag" ),

			@NamedQuery( name = "EdiCorpAction.findByEventTypeAndDateAndNullBbgFlag",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN (:eventCD)"
							+ " AND (o.date1>=:date1 AND o.id.eventCD IN (:effDateTypes)"
							+ "		 OR (o.id.eventCD IN ('MRGR','DMRGR') AND (o.date2>=:date1 OR o.date3>=:date1))"
							+ "		 OR (o.id.eventCD='TKOVR' AND (o.date3>=:date1 OR o.date5>=:date1))"
							+ "	)"
							+ " AND o.bbgFlag is null" ),

			@NamedQuery( name = "EdiCorpAction.findByEventTypeAndBetweenDatesAndBbgFlag",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN :eventCD"
							+ " AND (((o.date1>=:startDate AND o.date1<=:endDate) AND o.id.eventCD IN (:effDateTypes))"
							+ "		 OR (o.id.eventCD IN ('MRGR','DMRGR') "
							+ "			AND ((o.date2>=:startDate AND o.date2<=:endDate) OR (o.date3>=:startDate AND o.date3<=:endDate)))"
							+ "		 OR (o.id.eventCD='TKOVR'"
							+ "			AND ((o.date3>=:startDate AND o.date3<=:endDate) OR (o.date5>=:startDate AND o.date5<=:endDate)))"
							+ "	)"
							+ " AND o.bbgFlag=:bbgFlag" ),

			@NamedQuery( name = "EdiCorpAction.findByEventTypeAndBetweenDatesAndNullBbgFlag",
					query = "SELECT o FROM EdiCorporateAction o "
							+ "WHERE o.id.eventCD IN :eventCD"
							+ " AND (((o.date1>=:startDate AND o.date1<=:endDate) AND o.id.eventCD IN (:effDateTypes))"
							+ "		 OR (o.id.eventCD IN ('MRGR','DMRGR') "
							+ "			AND ((o.date2>=:startDate AND o.date2<=:endDate) OR (o.date3>=:startDate AND o.date3<=:endDate)))"
							+ "		 OR (o.id.eventCD='TKOVR'"
							+ "			AND ((o.date3>=:startDate AND o.date3<=:endDate) OR (o.date5>=:startDate AND o.date5<=:endDate)))"
							+ "	)"
							+ " AND o.bbgFlag is null" )


//	("LSTAT", "CONSD", "SD",
//			"ICC", "LCC", "ISCHG", "PRCHG", "LTCHG", "DIST", "BON");

	} )
	@IdClass( EdiCorporateAction.EdiCorpActionPk.class )
	static class EdiCorporateAction {

		@Override
		public String toString() {
			return "EdiCorporateAction [eventcd=" + eventcd + ", eventid=" + eventid + ", optionid=" + optionid
					+ ", serialid=" + serialid + ", scexhid=" + scexhid + ", bbcid=" + bbcid + ", bbeid=" + bbeid
					+ ", secid=" + secid + ", issid=" + issid + ", indusid=" + indusid + ", rdid=" + rdid + ", priority="
					+ priority + ", outturnsecid=" + outturnsecid + ", defaultopt=" + defaultopt + ", parvalue=" + parvalue
					+ ", ratioold=" + ratioold + ", rationew=" + rationew + ", rate1=" + rate1 + ", rate2=" + rate2
					+ ", actflag=" + actflag + ", changed=" + changed + ", created=" + created + ", isin=" + isin
					+ ", uscode=" + uscode + ", issuername=" + issuername + ", cntryofincorp=" + cntryofincorp + ", sic="
					+ sic + ", cik=" + cik + ", sectycd=" + sectycd + ", securitydesc=" + securitydesc + ", pvcurrency="
					+ pvcurrency + ", statusflag=" + statusflag + ", primaryexchgcd=" + primaryexchgcd + ", bbgcurrency="
					+ bbgcurrency + ", bbgcompositeglobalid=" + bbgcompositeglobalid + ", bbgcompositeticker="
					+ bbgcompositeticker + ", bbgglobalid=" + bbgglobalid + ", bbgexchangeticker=" + bbgexchangeticker
					+ ", structcd=" + structcd + ", exchgcntry=" + exchgcntry + ", exchgcd=" + exchgcd + ", mic=" + mic
					+ ", micseg=" + micseg + ", localcode=" + localcode + ", liststatus=" + liststatus + ", date1type="
					+ date1type + ", date1=" + date1 + ", date2type=" + date2type + ", date2=" + date2 + ", date3type="
					+ date3type + ", date3=" + date3 + ", date4type=" + date4type + ", date4=" + date4 + ", date5type="
					+ date5type + ", date5=" + date5 + ", date6type=" + date6type + ", date6=" + date6 + ", date7type="
					+ date7type + ", date7=" + date7 + ", date8type=" + date8type + ", date8=" + date8 + ", date9type="
					+ date9type + ", date9=" + date9 + ", date10type=" + date10type + ", date10=" + date10 + ", date11type="
					+ date11type + ", date11=" + date11 + ", date12type=" + date12type + ", date12=" + date12 + ", paytype="
					+ paytype + ", outturnisin=" + outturnisin + ", fractions=" + fractions + ", currency=" + currency
					+ ", rate1type=" + rate1type + ", rate2type=" + rate2type + ", field1name=" + field1name + ", field1="
					+ field1 + ", field2name=" + field2name + ", field2=" + field2 + ", field3name=" + field3name
					+ ", field3=" + field3 + ", field4name=" + field4name + ", field4=" + field4 + ", field5name="
					+ field5name + ", field5=" + field5 + ", field6name=" + field6name + ", field6=" + field6
					+ ", field7name=" + field7name + ", field7=" + field7 + ", field8name=" + field8name + ", field8="
					+ field8 + ", field9name=" + field9name + ", field9=" + field9 + ", field10name=" + field10name
					+ ", field10=" + field10 + ", field11name=" + field11name + ", field11=" + field11 + ", field12name="
					+ field12name + ", field12=" + field12 + ", field13name=" + field13name + ", field13=" + field13
					+ ", field14name=" + field14name + ", field14=" + field14 + ", field15name=" + field15name
					+ ", field15=" + field15 + ", field16name=" + field16name + ", field16=" + field16 + ", field17name="
					+ field17name + ", field17=" + field17 + ", field18name=" + field18name + ", field18=" + field18
					+ ", field19name=" + field19name + ", field19=" + field19 + ", field20name=" + field20name
					+ ", field20=" + field20 + ", field21name=" + field21name + ", field21=" + field21 + ", field22name="
					+ field22name + ", field22=" + field22 + ", field23name=" + field23name + ", field23=" + field23
					+ ", field24name=" + field24name + ", field24=" + field24 + "]";
		}

		static class EdiCorpActionPk implements Serializable {

			private static final long serialVersionUID = 3934822732297012646L;

			private EdiActionType eventcd;
			private Integer eventid;
			private Integer optionid;
			private Integer serialid;
			private Integer scexhid;
			private Integer bbcid;
			private Integer bbeid;

			@Override
			public String toString() {
				return "EdiCorpActionPk [eventcd=" + eventcd + ", eventid=" + eventid + ", optionid=" + optionid
						+ ", serialid=" + serialid + ", scexhid=" + scexhid + ", bbcid=" + bbcid + ", bbeid=" + bbeid + "]";
			}

			public EdiActionType getEventCD() {
				return eventcd;
			}

			public EdiCorpActionPk setEventCD(EdiActionType eventcd) {
				this.eventcd = eventcd;
				return this;
			}

			public Integer getEventID() {
				return eventid;
			}

			public EdiCorpActionPk setEventID(Integer eventid) {
				this.eventid = eventid;
				return this;
			}

			public Integer getOptionID() {
				return optionid;
			}

			public EdiCorpActionPk setOptionID(Integer optionid) {
				this.optionid = optionid;
				return this;
			}

			public Integer getSerialID() {
				return serialid;
			}

			public EdiCorpActionPk setSerialID(Integer serialid) {
				this.serialid = serialid;
				return this;
			}

			public Integer getScexhID() {
				return scexhid;
			}

			public EdiCorpActionPk setScexhID(Integer scexhid) {
				this.scexhid = scexhid;
				return this;
			}

			public Integer getBbcID() {
				return bbcid;
			}

			public EdiCorpActionPk setBbcID(Integer bbcid) {
				this.bbcid = bbcid;
				return this;
			}

			public Integer getBbeID() {
				return bbeid;
			}

			public EdiCorpActionPk setBbeID(Integer bbeid) {
				this.bbeid = bbeid;
				return this;
			}

			@Override
			public int hashCode() {
				final int prime = 59;
				int result = 43;
				long salt = Double.doubleToLongBits( bbcid + bbeid + eventid + optionid + scexhid + serialid );
				result = prime * result + ( (int) ( salt ^ ( salt >> 32 ) ) );
				result = prime * result + ( ( bbcid == null ) ? 0 : bbcid.hashCode() );
				result = prime * result + ( ( bbeid == null ) ? 0 : bbeid.hashCode() );
				result = prime * result + ( ( eventcd == null ) ? 0 : eventcd.hashCode() );
				result = prime * result + ( ( eventid == null ) ? 0 : eventid.hashCode() );
				result = prime * result + ( ( optionid == null ) ? 0 : optionid.hashCode() );
				result = prime * result + ( ( scexhid == null ) ? 0 : scexhid.hashCode() );
				result = prime * result + ( ( serialid == null ) ? 0 : serialid.hashCode() );
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if ( this == obj ) {
					return true;
				}
				if ( obj == null ) {
					return false;
				}
				if ( EdiCorporateAction.class == obj.getClass() ) {
					return this.equals( ( (EdiCorporateAction) obj ).getId() );
				}
				if ( getClass() != obj.getClass() ) {
					return false;
				}
				EdiCorpActionPk other = (EdiCorpActionPk) obj;
				if ( bbcid == null ) {
					if ( other.bbcid != null ) {
						return false;
					}
				}
				else if ( !bbcid.equals( other.bbcid ) ) {
					return false;
				}
				if ( bbeid == null ) {
					if ( other.bbeid != null ) {
						return false;
					}
				}
				else if ( !bbeid.equals( other.bbeid ) ) {
					return false;
				}
				if ( eventcd == null ) {
					if ( other.eventcd != null ) {
						return false;
					}
				}
				else if ( !eventcd.equals( other.eventcd ) ) {
					return false;
				}
				if ( eventid == null ) {
					if ( other.eventid != null ) {
						return false;
					}
				}
				else if ( !eventid.equals( other.eventid ) ) {
					return false;
				}
				if ( optionid == null ) {
					if ( other.optionid != null ) {
						return false;
					}
				}
				else if ( !optionid.equals( other.optionid ) ) {
					return false;
				}
				if ( scexhid == null ) {
					if ( other.scexhid != null ) {
						return false;
					}
				}
				else if ( !scexhid.equals( other.scexhid ) ) {
					return false;
				}
				if ( serialid == null ) {
					if ( other.serialid != null ) {
						return false;
					}
				}
				else if ( !serialid.equals( other.serialid ) ) {
					return false;
				}
				return true;
			}

		}

		private EdiActionType eventcd;
		private Integer eventid;
		private Integer optionid;
		private Integer serialid;
		private Integer scexhid;
		private Integer bbcid;
		private Integer bbeid;

		private Boolean bbgFlag;

		private Integer secid;
		private Integer issid;
		private Integer indusid;
		private Integer rdid;
		private Integer priority;
		private Integer outturnsecid;

		private Boolean defaultopt;

		private BigDecimal parvalue;
		private BigDecimal ratioold;
		private BigDecimal rationew;
		private BigDecimal rate1;
		private BigDecimal rate2;

		private String actflag;
		private String changed; // date
		private String created; // date
		private String isin;
		private String uscode;
		private String issuername;
		private String cntryofincorp;
		private String sic;
		private String cik;
		private String sectycd;
		private String securitydesc;
		private String pvcurrency;
		private String statusflag;
		private String primaryexchgcd;
		private String bbgcurrency;
		private String bbgcompositeglobalid;
		private String bbgcompositeticker;
		private String bbgglobalid;
		private String bbgexchangeticker;
		private String structcd;
		private String exchgcntry;
		private String exchgcd;
		private String mic;
		private String micseg;
		private String localcode;
		private String liststatus;
		private String date1type;
		private String date1; // date
		private String date2type;
		private String date2; // date
		private String date3type;
		private String date3; // date
		private String date4type;
		private String date4; // date
		private String date5type;
		private String date5; // date
		private String date6type;
		private String date6; // date
		private String date7type;
		private String date7; // date
		private String date8type;
		private String date8; // date
		private String date9type;
		private String date9; // date
		private String date10type;
		private String date10; // date
		private String date11type;
		private String date11; // date
		private String date12type;
		private String date12; // date
		private String paytype;
		private String outturnisin;
		private String fractions;
		private String currency;
		private String rate1type;
		private String rate2type;
		private String field1name;
		private String field1;
		private String field2name;
		private String field2;
		private String field3name;
		private String field3;
		private String field4name;
		private String field4;
		private String field5name;
		private String field5;
		private String field6name;
		private String field6;
		private String field7name;
		private String field7;
		private String field8name;
		private String field8;
		private String field9name;
		private String field9;
		private String field10name;
		private String field10;
		private String field11name;
		private String field11;
		private String field12name;
		private String field12;
		private String field13name;
		private String field13;
		private String field14name;
		private String field14;
		private String field15name;
		private String field15;
		private String field16name;
		private String field16;
		private String field17name;
		private String field17;
		private String field18name;
		private String field18;
		private String field19name;
		private String field19;
		private String field20name;
		private String field20;
		private String field21name;
		private String field21;
		private String field22name;
		private String field22;
		private String field23name;
		private String field23;
		private String field24name;
		private String field24;

		@Id
		@Enumerated( EnumType.STRING )
		public EdiActionType getEventCD() {
			return eventcd;
		}

		public void setEventCD(EdiActionType eventCD) {
			this.eventcd = eventCD;
		}

		@Id
		public Integer getEventID() {
			return eventid;
		}

		public void setEventID(Integer eventID) {
			this.eventid = eventID;
		}

		@Id
		public Integer getOptionID() {
			return optionid;
		}

		public void setOptionID(Integer optionID) {
			this.optionid = optionID;
		}

		@Id
		public Integer getSerialID() {
			return serialid;
		}

		public void setSerialID(Integer serialID) {
			this.serialid = serialID;
		}

		@Id
		public Integer getScexhID() {
			return scexhid;
		}

		public void setScexhID(Integer scexhID) {
			this.scexhid = scexhID;
		}

		@Id
		public Integer getBbcID() {
			return bbcid;
		}

		public void setBbcID(Integer bbcID) {
			this.bbcid = bbcID;
		}

		@Id
		public Integer getBbeID() {
			return bbeid;
		}

		public void setBbeID(Integer bbeID) {
			this.bbeid = bbeID;
		}

		@Column( name = "BBG_FLAG", precision = 1 )
		public Boolean getBbgFlag() {
			return bbgFlag;
		}

		public void setBbgFlag(Boolean bbgFlag) {
			this.bbgFlag = bbgFlag;
		}

		public String getActflag() {
			return actflag;
		}

		public void setActflag(String actflag) {
			this.actflag = actflag;
		}

		public String getChanged() {
			return changed;
		}

		public void setChanged(String changed) {
			this.changed = changed;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public Integer getSecID() {
			return secid;
		}

		public void setSecID(Integer secID) {
			this.secid = secID;
		}

		public Integer getIssID() {
			return issid;
		}

		public void setIssID(Integer issID) {
			this.issid = issID;
		}

		public String getISIN() {
			return isin;
		}

		public void setISIN(String iSIN) {
			this.isin = iSIN;
		}

		public String getUSCode() {
			return uscode;
		}

		public void setUSCode(String uSCode) {
			this.uscode = uSCode;
		}

		public String getIssuername() {
			return issuername;
		}

		public void setIssuername(String issuername) {
			this.issuername = issuername;
		}

		public String getCntryofIncorp() {
			return cntryofincorp;
		}

		public void setCntryofIncorp(String cntryofIncorp) {
			this.cntryofincorp = cntryofIncorp;
		}

		public String getSIC() {
			return sic;
		}

		public void setSIC(String sIC) {
			this.sic = sIC;
		}

		public String getCIK() {
			return cik;
		}

		public void setCIK(String cIK) {
			this.cik = cIK;
		}

		public Integer getIndusID() {
			return indusid;
		}

		public void setIndusID(Integer indusID) {
			this.indusid = indusID;
		}

		public String getSectyCD() {
			return sectycd;
		}

		public void setSectyCD(String sectyCD) {
			this.sectycd = sectyCD;
		}

		public String getSecurityDesc() {
			return securitydesc;
		}

		public void setSecurityDesc(String securityDesc) {
			this.securitydesc = securityDesc;
		}

		public BigDecimal getParValue() {
			return parvalue;
		}

		public void setParValue(BigDecimal parValue) {
			this.parvalue = parValue;
		}

		public String getPVCurrency() {
			return pvcurrency;
		}

		public void setPVCurrency(String pVCurrency) {
			this.pvcurrency = pVCurrency;
		}

		public String getStatusFlag() {
			return statusflag;
		}

		public void setStatusFlag(String statusFlag) {
			this.statusflag = statusFlag;
		}

		public String getPrimaryExchgCD() {
			return primaryexchgcd;
		}

		public void setPrimaryExchgCD(String primaryExchgCD) {
			this.primaryexchgcd = primaryExchgCD;
		}

		public String getBbgCurrency() {
			return bbgcurrency;
		}

		public void setBbgCurrency(String bbgCurrency) {
			this.bbgcurrency = bbgCurrency;
		}

		public String getBbgCompositeGlobalID() {
			return bbgcompositeglobalid;
		}

		public void setBbgCompositeGlobalID(String bbgCompositeGlobalID) {
			this.bbgcompositeglobalid = bbgCompositeGlobalID;
		}

		public String getBbgCompositeTicker() {
			return bbgcompositeticker;
		}

		public void setBbgCompositeTicker(String bbgCompositeTicker) {
			this.bbgcompositeticker = bbgCompositeTicker;
		}

		public String getBbgGlobalID() {
			return bbgglobalid;
		}

		public void setBbgGlobalID(String bbgGlobalID) {
			this.bbgglobalid = bbgGlobalID;
		}

		public String getBbgExchangeTicker() {
			return bbgexchangeticker;
		}

		public void setBbgExchangeTicker(String bbgExchangeTicker) {
			this.bbgexchangeticker = bbgExchangeTicker;
		}

		public String getStructCD() {
			return structcd;
		}

		public void setStructCD(String structCD) {
			this.structcd = structCD;
		}

		public String getExchgCntry() {
			return exchgcntry;
		}

		public void setExchgCntry(String exchgCntry) {
			this.exchgcntry = exchgCntry;
		}

		public String getExchgCD() {
			return exchgcd;
		}

		public void setExchgCD(String exchgCD) {
			this.exchgcd = exchgCD;
		}

		public String getMic() {
			return mic;
		}

		public void setMic(String mic) {
			this.mic = mic;
		}

		public String getMicseg() {
			return micseg;
		}

		public void setMicseg(String micseg) {
			this.micseg = micseg;
		}

		public String getLocalCode() {
			return localcode;
		}

		public void setLocalCode(String localCode) {
			this.localcode = localCode;
		}

		public String getListStatus() {
			return liststatus;
		}

		public void setListStatus(String listStatus) {
			this.liststatus = listStatus;
		}

		public String getDate1Type() {
			return date1type;
		}

		public void setDate1Type(String date1type) {
			this.date1type = date1type;
		}

		public String getDate1() {
			return date1;
		}

		public void setDate1(String date1) {
			this.date1 = date1;
		}

		public String getDate2Type() {
			return date2type;
		}

		public void setDate2Type(String date2type) {
			this.date2type = date2type;
		}

		public String getDate2() {
			return date2;
		}

		public void setDate2(String date2) {
			this.date2 = date2;
		}

		public String getDate3Type() {
			return date3type;
		}

		public void setDate3Type(String date3type) {
			this.date3type = date3type;
		}

		public String getDate3() {
			return date3;
		}

		public void setDate3(String date3) {
			this.date3 = date3;
		}

		public String getDate4Type() {
			return date4type;
		}

		public void setDate4Type(String date4type) {
			this.date4type = date4type;
		}

		public String getDate4() {
			return date4;
		}

		public void setDate4(String date4) {
			this.date4 = date4;
		}

		public String getDate5Type() {
			return date5type;
		}

		public void setDate5Type(String date5type) {
			this.date5type = date5type;
		}

		public String getDate5() {
			return date5;
		}

		public void setDate5(String date5) {
			this.date5 = date5;
		}

		public String getDate6Type() {
			return date6type;
		}

		public void setDate6Type(String date6type) {
			this.date6type = date6type;
		}

		public String getDate6() {
			return date6;
		}

		public void setDate6(String date6) {
			this.date6 = date6;
		}

		public String getDate7Type() {
			return date7type;
		}

		public void setDate7Type(String date7type) {
			this.date7type = date7type;
		}

		public String getDate7() {
			return date7;
		}

		public void setDate7(String date7) {
			this.date7 = date7;
		}

		public String getDate8Type() {
			return date8type;
		}

		public void setDate8Type(String date8type) {
			this.date8type = date8type;
		}

		public String getDate8() {
			return date8;
		}

		public void setDate8(String date8) {
			this.date8 = date8;
		}

		public String getDate9Type() {
			return date9type;
		}

		public void setDate9Type(String date9type) {
			this.date9type = date9type;
		}

		public String getDate9() {
			return date9;
		}

		public void setDate9(String date9) {
			this.date9 = date9;
		}

		public String getDate10Type() {
			return date10type;
		}

		public void setDate10Type(String date10type) {
			this.date10type = date10type;
		}

		public String getDate10() {
			return date10;
		}

		public void setDate10(String date10) {
			this.date10 = date10;
		}

		public String getDate11Type() {
			return date11type;
		}

		public void setDate11Type(String date11type) {
			this.date11type = date11type;
		}

		public String getDate11() {
			return date11;
		}

		public void setDate11(String date11) {
			this.date11 = date11;
		}

		public String getDate12Type() {
			return date12type;
		}

		public void setDate12Type(String date12type) {
			this.date12type = date12type;
		}

		public String getDate12() {
			return date12;
		}

		public void setDate12(String date12) {
			this.date12 = date12;
		}

		public String getPaytype() {
			return paytype;
		}

		public void setPaytype(String paytype) {
			this.paytype = paytype;
		}

		public Integer getRDID() {
			return rdid;
		}

		public void setRDID(Integer rDID) {
			this.rdid = rDID;
		}

		public Integer getPriority() {
			return priority;
		}

		public void setPriority(Integer Priority) {
			this.priority = Priority;
		}

		public Boolean getDefaultOpt() {
			return defaultopt;
		}

		public void setDefaultOpt(Boolean defaultOpt) {
			this.defaultopt = defaultOpt;
		}

		public Integer getOutturnSecID() {
			return outturnsecid;
		}

		public void setOutturnSecID(Integer outturnSecID) {
			this.outturnsecid = outturnSecID;
		}

		public String getOutturnIsin() {
			return outturnisin;
		}

		public void setOutturnIsin(String outturnIsin) {
			this.outturnisin = outturnIsin;
		}

		public BigDecimal getRatioOld() {
			return ratioold;
		}

		public void setRatioOld(BigDecimal ratioOld) {
			this.ratioold = ratioOld;
		}

		public BigDecimal getRatioNew() {
			return rationew;
		}

		public void setRatioNew(BigDecimal ratioNew) {
			this.rationew = ratioNew;
		}

		public String getFractions() {
			return fractions;
		}

		public void setFractions(String fractions) {
			this.fractions = fractions;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getRate1Type() {
			return rate1type;
		}

		public void setRate1Type(String rate1type) {
			this.rate1type = rate1type;
		}

		public BigDecimal getRate1() {
			return rate1;
		}

		public void setRate1(BigDecimal rate1) {
			this.rate1 = rate1;
		}

		public String getRate2Type() {
			return rate2type;
		}

		public void setRate2Type(String rate2type) {
			this.rate2type = rate2type;
		}

		public BigDecimal getRate2() {
			return rate2;
		}

		public void setRate2(BigDecimal rate2) {
			this.rate2 = rate2;
		}

		public String getField1Name() {
			return field1name;
		}

		public void setField1Name(String field1name) {
			this.field1name = field1name;
		}

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public String getField2Name() {
			return field2name;
		}

		public void setField2Name(String field2name) {
			this.field2name = field2name;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}

		public String getField3Name() {
			return field3name;
		}

		public void setField3Name(String field3name) {
			this.field3name = field3name;
		}

		public String getField3() {
			return field3;
		}

		public void setField3(String field3) {
			this.field3 = field3;
		}

		public String getField4Name() {
			return field4name;
		}

		public void setField4Name(String field4name) {
			this.field4name = field4name;
		}

		public String getField4() {
			return field4;
		}

		public void setField4(String field4) {
			this.field4 = field4;
		}

		public String getField5Name() {
			return field5name;
		}

		public void setField5Name(String field5name) {
			this.field5name = field5name;
		}

		public String getField5() {
			return field5;
		}

		public void setField5(String field5) {
			this.field5 = field5;
		}

		public String getField6Name() {
			return field6name;
		}

		public void setField6Name(String field6name) {
			this.field6name = field6name;
		}

		public String getField6() {
			return field6;
		}

		public void setField6(String field6) {
			this.field6 = field6;
		}

		public String getField7Name() {
			return field7name;
		}

		public void setField7Name(String field7name) {
			this.field7name = field7name;
		}

		public String getField7() {
			return field7;
		}

		public void setField7(String field7) {
			this.field7 = field7;
		}

		public String getField8Name() {
			return field8name;
		}

		public void setField8Name(String field8name) {
			this.field8name = field8name;
		}

		public String getField8() {
			return field8;
		}

		public void setField8(String field8) {
			this.field8 = field8;
		}

		public String getField9Name() {
			return field9name;
		}

		public void setField9Name(String field9name) {
			this.field9name = field9name;
		}

		public String getField9() {
			return field9;
		}

		public void setField9(String field9) {
			this.field9 = field9;
		}

		public String getField10Name() {
			return field10name;
		}

		public void setField10Name(String field10name) {
			this.field10name = field10name;
		}

		public String getField10() {
			return field10;
		}

		public void setField10(String field10) {
			this.field10 = field10;
		}

		public String getField11Name() {
			return field11name;
		}

		public void setField11Name(String field11name) {
			this.field11name = field11name;
		}

		public String getField11() {
			return field11;
		}

		public void setField11(String field11) {
			this.field11 = field11;
		}

		public String getField12Name() {
			return field12name;
		}

		public void setField12Name(String field12name) {
			this.field12name = field12name;
		}

		public String getField12() {
			return field12;
		}

		public void setField12(String field12) {
			this.field12 = field12;
		}

		public String getField13Name() {
			return field13name;
		}

		public void setField13Name(String field13name) {
			this.field13name = field13name;
		}

		public String getField13() {
			return field13;
		}

		public void setField13(String field13) {
			this.field13 = field13;
		}

		public String getField14Name() {
			return field14name;
		}

		public void setField14Name(String field14name) {
			this.field14name = field14name;
		}

		public String getField14() {
			return field14;
		}

		public void setField14(String field14) {
			this.field14 = field14;
		}

		public String getField15Name() {
			return field15name;
		}

		public void setField15Name(String field15name) {
			this.field15name = field15name;
		}

		public String getField15() {
			return field15;
		}

		public void setField15(String field15) {
			this.field15 = field15;
		}

		public String getField16Name() {
			return field16name;
		}

		public void setField16Name(String field16name) {
			this.field16name = field16name;
		}

		public String getField16() {
			return field16;
		}

		public void setField16(String field16) {
			this.field16 = field16;
		}

		public String getField17Name() {
			return field17name;
		}

		public void setField17Name(String field17name) {
			this.field17name = field17name;
		}

		public String getField17() {
			return field17;
		}

		public void setField17(String field17) {
			this.field17 = field17;
		}

		public String getField18Name() {
			return field18name;
		}

		public void setField18Name(String field18name) {
			this.field18name = field18name;
		}

		public String getField18() {
			return field18;
		}

		public void setField18(String field18) {
			this.field18 = field18;
		}

		public String getField19Name() {
			return field19name;
		}

		public void setField19Name(String field19name) {
			this.field19name = field19name;
		}

		public String getField19() {
			return field19;
		}

		public void setField19(String field19) {
			this.field19 = field19;
		}

		public String getField20Name() {
			return field20name;
		}

		public void setField20Name(String field20name) {
			this.field20name = field20name;
		}

		public String getField20() {
			return field20;
		}

		public void setField20(String field20) {
			this.field20 = field20;
		}

		public String getField21Name() {
			return field21name;
		}

		public void setField21Name(String field21name) {
			this.field21name = field21name;
		}

		public String getField21() {
			return field21;
		}

		public void setField21(String field21) {
			this.field21 = field21;
		}

		public String getField22Name() {
			return field22name;
		}

		public void setField22Name(String field22name) {
			this.field22name = field22name;
		}

		public String getField22() {
			return field22;
		}

		public void setField22(String field22) {
			this.field22 = field22;
		}

		public String getField23Name() {
			return field23name;
		}

		public void setField23Name(String field23name) {
			this.field23name = field23name;
		}

		public String getField23() {
			return field23;
		}

		public void setField23(String field23) {
			this.field23 = field23;
		}

		public String getField24Name() {
			return field24name;
		}

		public void setField24Name(String field24name) {
			this.field24name = field24name;
		}

		public String getField24() {
			return field24;
		}

		public void setField24(String field24) {
			this.field24 = field24;
		}

		@Transient
		public void setField(String fieldname, String value) throws Exception {
			if ( value == null || value.isEmpty() ) {
				return;
			}
			Object param = null;
			Field f = getClass().getDeclaredField( fieldname );
			switch ( fieldname ) {
				case "eventcd":
					param = EdiActionType.valueOf( value );
					break;
				case "eventid":
				case "optionid":
				case "serialid":
				case "scexhid":
				case "bbcid":
				case "bbeid":
				case "secid":
				case "issid":
				case "indusid":
				case "rdid":
				case "priority":
				case "outturnsecid":
					param = Integer.parseInt( value );
					break;
				case "parvalue":
				case "ratioold":
				case "rationew":
				case "rate1":
				case "rate2":
					param = new BigDecimal( value );
					break;
				case "defaultopt":
					param = value.equals( "1" );
					break;
				default:
					param = value;
			}
			f.set( this, param );
		}

		@Transient
		EdiCorpActionPk getId() {
			return new EdiCorpActionPk()
					.setEventCD( this.getEventCD() )
					.setEventID( this.getEventID() )
					.setOptionID( this.getOptionID() )
					.setSerialID( this.getSerialID() )
					.setScexhID( this.getScexhID() )
					.setBbcID( this.getBbcID() )
					.setBbeID( this.getBbcID() );
		}

		@Transient
		public String getIdString() {
			return getId().toString();
		}

		@Override
		public int hashCode() {
			return getId().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if ( this == obj ) {
				return true;
			}
			if ( obj == null ) {
				return false;
			}
			if ( obj instanceof EdiCorpActionPk ) {
				return this.getId().equals( obj );
			}
			if ( getClass() != obj.getClass() ) {
				return false;
			}
			EdiCorporateAction other = (EdiCorporateAction) obj;
			return this.getId().equals( other.getId() );

		}

		@Transient
		public EdiCorpActionPk getPk() {
			return ( eventid == null && eventcd == null ) ? null : getId();
		}

		@Transient
		public BigInteger getBbgActionId() {
			BigInteger salt = BigInteger.valueOf( ( (long) 1E+9 ) );
			switch ( getEventCD().getSeqNo() ) {
				case 1:
					salt = salt.add( BigInteger.valueOf( ( (long) 1E+8 ) ) );
					break;
				case 2:
					salt = salt.add( BigInteger.valueOf( ( (long) 2E+8 ) ) );
					break;
			}
			return salt.add( BigInteger.valueOf( getEventID() ) );
		}
	}
}
