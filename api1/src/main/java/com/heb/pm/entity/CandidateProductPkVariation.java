/*
 *  CandidateProductPkVariation
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
 * The persistent class for the PS_PROD_PK_VAR database table.
 * @author vn87351
 * @since 2.12
 */
@Entity
@Table(name="PS_PROD_PK_VAR")
public class CandidateProductPkVariation implements Serializable{
	public static String PREPRD_PRODUCT_NO= "N";
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CandidateProductPkVariationKey key;

	@Column(name="CLRS_QTY")
	private Double caloriesQuantity;

	@Column(name="HSHLD_SRVNG_SZ_TXT")
	private String houseHoldMeasurement;

	@Column(name="MAX_SPCR_QTY")
	private Double maxServingsPerContainer;

	@Column(name="MIN_SPCR_QTY")
	private Double minServingsPerContainer;

	@Column(name="NTRNT_PAN_NBR")
	private Double ntrntPanNbr;

	@Column(name="PREPRD_PROD_SW")
	private String preprdProdSw;

	@Column(name="PROD_PAN_TYP_CD")
	private String prodPanTypCd;

	@Column(name="PROD_VAL_DES")
	private String prodValDes;

	@Column(name="SPCR_TXT")
	private String servingsPerContainerText;

	@Column(name="SRC_SYSTEM_ID")
	private int sourceSystem;

	@Column(name="SRVNG_SZ_QTY")
	private Double servingSizeQuantity;

	@Column(name="SRVNG_SZ_UOM_CD")
	private String srvngSzUomCd;

	//bi-directional many-to-one association to PsWorkRqst
	@JsonIgnoreProperties({"candidateMasterDataExtensionAttributes", "candidateProductPkVariations", "candidateNutrients", "candidateNutrientPanelHeaders", "candidateGenericEntityRelationship", "candidateFulfillmentChannels"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PS_WORK_ID",insertable = false, updatable = false, nullable = false)
	private CandidateWorkRequest candidateWorkRequest;

	public CandidateProductPkVariationKey getKey() {
		return key;
	}

	public CandidateProductPkVariation setKey(CandidateProductPkVariationKey key) {
		this.key = key;
		return this;
	}

	public Double getCaloriesQuantity() {
		return caloriesQuantity;
	}

	public CandidateProductPkVariation setCaloriesQuantity(Double caloriesQuantity) {
		this.caloriesQuantity = caloriesQuantity;
		return this;
	}

	public String getHouseHoldMeasurement() {
		return houseHoldMeasurement;
	}

	public CandidateProductPkVariation setHouseHoldMeasurement(String houseHoldMeasurement) {
		this.houseHoldMeasurement = houseHoldMeasurement;
		return this;
	}

	public Double getNtrntPanNbr() {
		return ntrntPanNbr;
	}

	public CandidateProductPkVariation setNtrntPanNbr(Double ntrntPanNbr) {
		this.ntrntPanNbr = ntrntPanNbr;
		return this;
	}

	public String getPreprdProdSw() {
		return preprdProdSw;
	}

	public CandidateProductPkVariation setPreprdProdSw(String preprdProdSw) {
		this.preprdProdSw = preprdProdSw;
		return this;
	}

	public String getProdPanTypCd() {
		return prodPanTypCd;
	}

	public CandidateProductPkVariation setProdPanTypCd(String prodPanTypCd) {
		this.prodPanTypCd = prodPanTypCd;
		return this;
	}

	public String getProdValDes() {
		return prodValDes;
	}

	public CandidateProductPkVariation setProdValDes(String prodValDes) {
		this.prodValDes = prodValDes;
		return this;
	}

	public Double getMaxServingsPerContainer() {
		return maxServingsPerContainer;
	}

	public CandidateProductPkVariation setMaxServingsPerContainer(Double maxServingsPerContainer) {
		this.maxServingsPerContainer = maxServingsPerContainer;
		return this;
	}

	public Double getMinServingsPerContainer() {
		return minServingsPerContainer;
	}

	public CandidateProductPkVariation setMinServingsPerContainer(Double minServingsPerContainer) {
		this.minServingsPerContainer = minServingsPerContainer;
		return this;
	}

	public String getServingsPerContainerText() {
		return servingsPerContainerText;
	}

	public CandidateProductPkVariation setServingsPerContainerText(String servingsPerContainerText) {
		this.servingsPerContainerText = servingsPerContainerText;
		return this;
	}

	public int getSourceSystem() {
		return sourceSystem;
	}

	public CandidateProductPkVariation setSourceSystem(int sourceSystem) {
		this.sourceSystem = sourceSystem;
		return this;
	}

	public Double getServingSizeQuantity() {
		return servingSizeQuantity;
	}

	public CandidateProductPkVariation setServingSizeQuantity(Double servingSizeQuantity) {
		this.servingSizeQuantity = servingSizeQuantity;
		return this;
	}

	public String getSrvngSzUomCd() {
		return srvngSzUomCd;
	}

	public CandidateProductPkVariation setSrvngSzUomCd(String srvngSzUomCd) {
		this.srvngSzUomCd = srvngSzUomCd;
		return this;
	}

	public CandidateWorkRequest getCandidateWorkRequest() {
		return candidateWorkRequest;
	}

	public CandidateProductPkVariation setCandidateWorkRequest(CandidateWorkRequest candidateWorkRequest) {
		this.candidateWorkRequest = candidateWorkRequest;
		return this;
	}

	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateWorkRequestId() {
		if (this.getKey().getCandidateWorkRequestId() == null) {
			this.getKey().setCandidateWorkRequestId(this.candidateWorkRequest.getWorkRequestId());
		}
	}
}
