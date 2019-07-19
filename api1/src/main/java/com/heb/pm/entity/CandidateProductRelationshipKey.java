/*
 * CandidateProductRelationshipKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Entity key for the ps_prod_rlshp table.
 *
 * @author vn87351
 * @since 2.16.0
 */
public class CandidateProductRelationshipKey  implements Serializable {
	/**
	 * The ps prod id.
	 */
	@Column(name = "ps_prod_id")
	private Long candidateProductId;

	@Column(name = "ps_related_prod_id")
	private Long candidateRelatedProductId;
	@Column(name = "prod_rlshp_cd")
	private String productRelationshipCode;

	public Long getCandidateProductId() {
		return candidateProductId;
	}

	public void setCandidateProductId(Long candidateProductId) {
		this.candidateProductId = candidateProductId;
	}

	public Long getCandidateRelatedProductId() {
		return candidateRelatedProductId;
	}

	public void setCandidateRelatedProductId(Long candidateRelatedProductId) {
		this.candidateRelatedProductId = candidateRelatedProductId;
	}

	public String getProductRelationshipCode() {
		return productRelationshipCode;
	}

	public void setProductRelationshipCode(String productRelationshipCode) {
		this.productRelationshipCode = productRelationshipCode;
	}

	@Override
	public String toString() {
		return "CandidateProductRelationshipKey{" +
				"candidateProductId=" + candidateProductId +
				", candidateRelatedProductId=" + candidateRelatedProductId +
				", productRelationshipCode='" + productRelationshipCode + '\'' +
				'}';
	}
}
