/*
 * CandidateProductRelationship
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity for the ps_prod_rlshp table.
 *
 * @author vn87351
 * @since 2.16.0
 */
@Entity
@Table(name = "ps_prod_rlshp")
public class CandidateProductRelationship implements Serializable{
	public static final String PRODUCT_VARIANT_RELATIONSHIP_CODE = "VARNT";
	@EmbeddedId
	private CandidateProductRelationshipKey key;

	@Column(name = "prod_qty")
	private Double quantity;

	@Column(name = "LST_UPDT_TS",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
			nullable = false, length = 0)
	private LocalDateTime lastUpdateTs;

	@Column(name = "LST_UPDT_UID", nullable = false, length = 8)
	private String lstUpdtUsrId;

	@Column(name = "scn_cd_id")
	private Long productScanCode;

	@Column(name = "reld_prod_cand_sw")
	private Boolean relatedProductCandidateSwitch;

	@ManyToOne
	@JoinColumn(name = "PS_PROD_ID", insertable = false, updatable = false)
	private CandidateProductMaster candidateProductMaster;

	@ManyToOne
	@JoinColumn(name = "ps_related_prod_id", insertable = false, updatable = false)
	private CandidateProductMaster candidateRelatedProductId;

	/**
	 * get candidate related product id
	 * @return the candidate product master
	 */
	public CandidateProductMaster getCandidateRelatedProductId() {
		return candidateRelatedProductId;
	}

	/**
	 * set candidate related product id
	 * @param candidateRelatedProductId
	 */
	public void setCandidateRelatedProductId(CandidateProductMaster candidateRelatedProductId) {
		this.candidateRelatedProductId = candidateRelatedProductId;
	}

	/**
	 * get candidate product master
	 * @return the candidate product master
	 */
	public CandidateProductMaster getCandidateProductMaster() {
		return candidateProductMaster;
	}

	/**
	 * set candidate product master
	 * @param candidateProductMaster
	 */
	public void setCandidateProductMaster(CandidateProductMaster candidateProductMaster) {
		this.candidateProductMaster = candidateProductMaster;
	}

	/**
	 * get key for candidate product relationship
	 * @return the CandidateProductRelationshipKey
	 */
	public CandidateProductRelationshipKey getKey() {
		return key;
	}

	/**
	 * set key for candidate product relationship
	 * @param key
	 */
	public void setKey(CandidateProductRelationshipKey key) {
		this.key = key;
	}

	/**
	 * get quantity
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * set quantity
	 * @param quantity
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * get last update time
	 * @returnlastUpdateTs
	 */
	public LocalDateTime getLastUpdateTs() {
		return lastUpdateTs;
	}

	/**
	 * set last update time
	 * @param lastUpdateTs
	 */
	public void setLastUpdateTs(LocalDateTime lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	/**
	 * get last update user id
	 * @return the last update user id
	 */
	public String getLstUpdtUsrId() {
		return lstUpdtUsrId;
	}

	/**
	 * set last update user id
	 * @param lstUpdtUsrId
	 */
	public void setLstUpdtUsrId(String lstUpdtUsrId) {
		this.lstUpdtUsrId = lstUpdtUsrId;
	}

	/**
	 * get product scan code
	 * @return the product scan code
	 */
	public Long getProductScanCode() {
		return productScanCode;
	}

	/**
	 * set product scan code
	 * @param productScanCode
	 */
	public void setProductScanCode(Long productScanCode) {
		this.productScanCode = productScanCode;
	}

	/**
	 * get related product candidate switch
	 * @return the switch
	 */
	public Boolean getRelatedProductCandidateSwitch() {
		return relatedProductCandidateSwitch;
	}

	/**
	 * set related product candidate switch
	 * @param relatedProductCandidateSwitch
	 */
	public void setRelatedProductCandidateSwitch(Boolean relatedProductCandidateSwitch) {
		this.relatedProductCandidateSwitch = relatedProductCandidateSwitch;
	}

	@Override
	public String toString() {
		return "CandidateProductRelationship{" +
				"key=" + key +
				", quantity=" + quantity +
				", lastUpdateTs=" + lastUpdateTs +
				", lstUpdtUsrId='" + lstUpdtUsrId + '\'' +
				", productScanCode=" + productScanCode +
				", relatedProductCandidateSwitch=" + relatedProductCandidateSwitch +
				", candidateProductMaster=" + candidateProductMaster +
				'}';
	}

	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setCandidateProductId() {
		if (this.getKey().getCandidateProductId() == null) {
			this.getKey().setCandidateProductId(this.candidateProductMaster.getCandidateProductId());
		}
		if (this.getKey().getCandidateRelatedProductId() == null) {
			this.getKey().setCandidateRelatedProductId(this.candidateRelatedProductId.getCandidateProductId());
		}
	}
}
