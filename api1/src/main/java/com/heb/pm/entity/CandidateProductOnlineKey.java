/*
 *  CandidateProductOnlineKey
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a ps product online data key for table ps product online.
 *
 * @author vn70529
 * @since 2.26.0
 */
@Embeddable
@TypeDefs({
	@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
	@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class CandidateProductOnlineKey implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * The ps prod id.
     */
    @Column(name = "ps_prod_id")
    private Long candidateProductId;

	@Column(name = "sals_chnl_cd")
	@Type(type = "fixedLengthCharPK")
	private String saleChannelCode;

    @Column(name="ps_work_id")
    private Long workRequestId;

    /**
     * Returns the candidate product id of this record.
     *
     * @return the candidate product id of this record.
     */
    public Long getCandidateProductId() {
        return this.candidateProductId;
    }

    /**
     * Sets the candidate product id of this record.
     *
     * @param candidateProductId the candidate product id of this record.
     */
    public void setCandidateProductId(Long candidateProductId) {
        this.candidateProductId = candidateProductId;
    }

	/**
	 * @return Gets the value of saleChannelCode and returns saleChannelCode
	 */
	public void setSaleChannelCode(String saleChannelCode) {
		this.saleChannelCode = saleChannelCode;
	}

	/**
	 * Sets the saleChannelCode
	 */
	public String getSaleChannelCode() {
		return saleChannelCode;
	}
    /**
     * Returns the work ID of this candidate request.
     *
     * @return The work ID of this candidate request.
     */
    public Long getWorkRequestId() {
        return workRequestId;
    }

    /**
     * Sets the work ID of this candidate request.
     *
     * @param workRequestId The work ID of this candidate request.
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateProductOnlineKey)) return false;

		CandidateProductOnlineKey that = (CandidateProductOnlineKey) o;

		if (getCandidateProductId() != null ? !getCandidateProductId().equals(that.getCandidateProductId()) : that.getCandidateProductId() != null)
			return false;
		if (getSaleChannelCode() != null ? !getSaleChannelCode().equals(that.getSaleChannelCode()) : that.getSaleChannelCode() != null)
			return false;
		return getWorkRequestId() != null ? getWorkRequestId().equals(that.getWorkRequestId()) : that.getWorkRequestId() == null;
	}

	@Override
	public int hashCode() {
		int result = getCandidateProductId() != null ? getCandidateProductId().hashCode() : 0;
		result = 31 * result + (getSaleChannelCode() != null ? getSaleChannelCode().hashCode() : 0);
		result = 31 * result + (getWorkRequestId() != null ? getWorkRequestId().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CandidateProductOnlineKey{" +
				"candidateProductId=" + candidateProductId +
				", saleChannelCode='" + saleChannelCode + '\'' +
				", workRequestId='" + workRequestId + '\'' +
				'}';
	}
}