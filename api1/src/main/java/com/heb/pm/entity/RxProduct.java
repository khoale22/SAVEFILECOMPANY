package com.heb.pm.entity;


import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The entity holds information for pharmacy products
 *
 * @author s753601
 * @version 2.12.0
 */
@Entity
@Table(name = "RX_PROD")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class RxProduct implements Serializable{

	@Id
	@Column(name = "PROD_ID")
	private Long id;

	@Column(name = "DRUG_SCH_TYP_CD")
	private String drugScheduleTypeCode;

	@Column(name = "NDC_ID")
	private String ndc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DRUG_SCH_TYP_CD", referencedColumnName = "DRUG_SCH_TYP_CD", updatable = false, insertable = false)
	private DrugScheduleType drugScheduleType;


	@Column(name="AVG_WHLSL_RX_CST")
	private Double avgWhlslRxCst;

	@Column(name="DIR_RX_CST")
	private Double dirRxCst;

	@Column(name="DRUG_NM_ABB")
	private String drugNmAbb;
	/**
	 * Gets the unique identifier for RxProduct
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Updates id
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * The code for the type of drug schedule
	 * @return drugScheduleTypeCode
	 */
	public String getDrugScheduleTypeCode() {
		return drugScheduleTypeCode;
	}

	/**
	 * Updates drugScheduleTypeCode
	 * @param drugScheduleTypeCode the new drugScheduleTypeCode
	 */
	public void setDrugScheduleTypeCode(String drugScheduleTypeCode) {
		this.drugScheduleTypeCode = drugScheduleTypeCode;
	}

	/**
	 * National Drug Code used in Pharmacy to identify products; equivalent to a UPC.
	 * @return ndc
	 */
	public String getNdc() {
		return ndc;
	}

	/**
	 * Updates the ndc
	 * @param ndc the new ndc
	 */
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	/**
	 * Pharmacy Values as set by DEA for controlling substances because of its abuse or potiential risk.  Schedule 1 are highest.
	 * L - Legend Item
	 * 5 - Scheduled 5 Drug
	 * 4 - Scheduled 4 Drug
	 * 3 - Scheduled 3 Drug
	 * 2 - Scheduled 2 Drug
	 * 1 - Scheduled 1 Drug
	 * @return drugScheduleType
	 */
	public DrugScheduleType getDrugScheduleType() {
		return drugScheduleType;
	}

	/**
	 * Updates the drugScheduleType
	 * @param drugScheduleType the new drugScheduleType
	 */
	public void setDrugScheduleType(DrugScheduleType drugScheduleType) {
		this.drugScheduleType = drugScheduleType;
	}
	public Double getAvgWhlslRxCst() {
		return avgWhlslRxCst;
	}

	public void setAvgWhlslRxCst(Double avgWhlslRxCst) {
		this.avgWhlslRxCst = avgWhlslRxCst;
	}

	public Double getDirRxCst() {
		return dirRxCst;
	}

	public void setDirRxCst(Double dirRxCst) {
		this.dirRxCst = dirRxCst;
	}

	public String getDrugNmAbb() {
		return drugNmAbb;
	}

	public void setDrugNmAbb(String drugNmAbb) {
		this.drugNmAbb = drugNmAbb;
	}
	/**
	 * Compares another object to this one. This only compares the keys.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RxProduct)) return false;

		RxProduct that = (RxProduct) o;

		return id == that.id;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}


	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "RxProduct{" +
				"id=" + id +
				", drugScheduleTypeCode=" + drugScheduleTypeCode +
				", drugScheduleType=" + drugScheduleType +
				", ndc=" + ndc +
				'}';
	}
}
