/*
 *  CandidateNutrient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;

/**
 * The entity for the PS_NUTRIENT database table.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="PS_NUTRIENT")
public class CandidateNutrient implements Serializable {
	public static int VALUE_PREPRD_TYPE_CODE=0;
	public static String NUTRIENT_MEASR_EXACT="EXACT";
	public static String NUTRIENT_LBL_SWITCH="0";
	private static final long serialVersionUID = 1L;
	//The code for Nutrient Master
	public enum NutrientMaster {
		Calories (1),
		CaloriesFromFat(2),
		SaturatedFatCalories(3),
		TotalFat(4),
		SaturatedFat(5),
		PolyunsaturatedFat(6),
		MonounsaturatedFat(7),
		Cholesterol(8),
		Sodium(9),
		Potassium(10),
		TotalCarbohydrate(11),
		DietaryFiber(12),
		SolubleFiber(13),
		InsolubleFiber(14),
		Sugars(15),
		SugarAlcohol(16),
		OtherCarbohydrate(17),
		Protein(18),
		VitaminA(19),
		VitaminC(20),
		Calcium(21),
		Iron(22),
		Kosher(23),
		TransFat(7545);
		private int id;
		NutrientMaster(int id) {
			this.id = id;
		}

		/**
		 * Gets id.
		 *
		 * @return the id
		 */
		public int getId() {
			return this.id;
		}
	}
	@EmbeddedId
	private CandidateNutrientKey key;

	@Column(name="DALY_VAL_SRVNG_PCT")
	private Double dalyValSrvngPct;

	@Column(name="DCLR_ON_LBL_SW")
	private String dclrOnLblSw;

	@Column(name="NTRNT_MEASR_TXT")
	private String ntrntMeasrTxt;

	@Column(name="NTRNT_MST_ID")
	private Integer masterId;

	@Column(name="NTRNT_QTY")
	private Double nutrientQuantity;

	@Column(name="SRC_SYSTEM_ID")
	private Integer sourceSystem;

	@Column(name="SRVNG_SZ_UOM_CD")
	private String uomCode;

	@Column(name="VAL_PREPRD_TYP_CD")
	private Integer valPreprdTypCd;

	//bi-directional many-to-one association to PsWorkRqst
	@JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship", "candidateFulfillmentChannels"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PS_WORK_ID" , insertable = false, updatable = false, nullable = false)
	private CandidateWorkRequest candidateWorkRequest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ntrnt_mst_id", referencedColumnName = "ntrnt_mst_id", insertable = false, updatable = false, nullable = false)
	private com.heb.pm.entity.NutrientMaster nutrientMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "srvng_sz_uom_cd", referencedColumnName = "srvng_sz_uom_cd", insertable = false, updatable = false, nullable = false)
	private ServingSizeUOM servingSizeUOM;

	/**
	 * Get the nutrientMaster.
	 * 
	 * @return the nutrientMaster
	 */
	public com.heb.pm.entity.NutrientMaster getNutrientMaster() {
		return nutrientMaster;
	}

	/**
	 * Set the nutrientMaster.
	 * 
	 * @param nutrientMaster the nutrientMaster to set
	 */
	public void setNutrientMaster(com.heb.pm.entity.NutrientMaster nutrientMaster) {
		this.nutrientMaster = nutrientMaster;
	}

	/**
	 * Get the servingSizeUOM.
	 * 
	 * @return the servingSizeUOM
	 */
	public ServingSizeUOM getServingSizeUOM() {
		return servingSizeUOM;
	}

	/**
	 * Set the servingSizeUOM.
	 * 
	 * @param servingSizeUOM the servingSizeUOM to set
	 */
	public void setServingSizeUOM(ServingSizeUOM servingSizeUOM) {
		this.servingSizeUOM = servingSizeUOM;
	}
	
	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	public CandidateNutrient setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
		return this;
	}

	public CandidateNutrientKey getKey() {
		return key;
	}

	public CandidateNutrient setKey(CandidateNutrientKey key) {
		this.key = key;
		return this;
	}

	public Double getDalyValSrvngPct() {
		return dalyValSrvngPct;
	}

	public CandidateNutrient setDalyValSrvngPct(Double dalyValSrvngPct) {
		this.dalyValSrvngPct = dalyValSrvngPct;
		return this;
	}

	public String getDclrOnLblSw() {
		return dclrOnLblSw;
	}

	public CandidateNutrient setDclrOnLblSw(String dclrOnLblSw) {
		this.dclrOnLblSw = dclrOnLblSw;
		return this;
	}

	public String getNtrntMeasrTxt() {
		return ntrntMeasrTxt;
	}

	public CandidateNutrient setNtrntMeasrTxt(String ntrntMeasrTxt) {
		this.ntrntMeasrTxt = ntrntMeasrTxt;
		return this;
	}

	public Integer getMasterId() {
		return masterId;
	}

	public CandidateNutrient setMasterId(Integer masterId) {
		this.masterId = masterId;
		return this;
	}

	public Double getNutrientQuantity() {
		return nutrientQuantity;
	}

	public CandidateNutrient setNutrientQuantity(Double nutrientQuantity) {
		this.nutrientQuantity = nutrientQuantity;
		return this;
	}

	public Integer getSourceSystem() {
		return sourceSystem;
	}

	public CandidateNutrient setSourceSystem(Integer sourceSystem) {
		this.sourceSystem = sourceSystem;
		return this;
	}

	public String getUomCode() {
		return uomCode;
	}

	public CandidateNutrient setUomCode(String uomCode) {
		this.uomCode = uomCode;
		return this;
	}

	public Integer getValPreprdTypCd() {
		return valPreprdTypCd;
	}

	public CandidateNutrient setValPreprdTypCd(Integer valPreprdTypCd) {
		this.valPreprdTypCd = valPreprdTypCd;
		return this;
	}
	/**
	 * Compares another object to this one. If that object is a entity, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateNutrient)) return false;

		CandidateNutrient that = (CandidateNutrient) o;
		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = key.hashCode();
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateNutrient{" +
				"key=" + key.toString() +
				", dalyValSrvngPct='" + dalyValSrvngPct + '\'' +
				", dclrOnLblSw='" + dclrOnLblSw + '\'' +
				", ntrntMeasrTxt='" + ntrntMeasrTxt + '\'' +
				", masterId='" + masterId + '\'' +
				'}';
	}

	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateWorkRequestId() {
		if (this.getKey().getCandidateWorkId() == null) {
			this.getKey().setCandidateWorkId(this.candidateWorkRequest.getWorkRequestId());
		}
	}
}
