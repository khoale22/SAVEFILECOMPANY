/*
 *  CandidateStatusKey
 *  Copyright (c) 2017 HEB
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
import java.time.LocalDateTime;

/**
 * Represents a ps candidate stat key. A ps candidate stat key contains the id of the ps candidate stat.
 *
 * @author vn00602
 * @since 2.12.0
 */
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
@Embeddable
public class CandidateStatusKey implements Serializable {

    private static final long serialVersionUID = 1L;
	//Status code for candidate work request
	public enum StatusCode {
		FAILURE("109"),
		BATCH_UPLOAD("111");

		private String name;
		StatusCode(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}
    @Column(name = "PS_WORK_ID", nullable = false)
    private Long workRequestId;

    @Column(name = "PD_SETUP_STAT_CD", nullable = false, length = 5)
    @Type(type = "fixedLengthCharPK")
    private String status;

    @Column(name = "LST_UPDT_TS", nullable = false, length = 0)
    private LocalDateTime lastUpdateDate;

    /**
     * Returns the work ID of this candidate request.
     *
     * @return The work ID of this candidate request.
     */
    public Long getWorkRequestId() {
        return this.workRequestId;
    }

    /**
     * Sets the work ID of this candidate request.
     *
     * @param workRequestId The work ID of this candidate request.
     */
    public void setWorkRequestId(Long workRequestId) {
        this.workRequestId = workRequestId;
    }

    /**
     * Returns the status of this candidate request.
     *
     * @return The status of this candidate request.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the status of this candidate request.
     *
     * @param status the status of this candidate request.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the date and time this record was last updated.
     *
     * @return The date and time this record was last updated.
     */
    public LocalDateTime getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    /**
     * Sets the date and time this record was last updated.
     *
     * @param lastUpdateDate The date and time this record was last updated.
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Compares another object to this one. This is a deep compare.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateStatusKey)) return false;

        CandidateStatusKey that = (CandidateStatusKey) o;

        if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null)
            return false;
        return lastUpdateDate != null ? lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate == null;
    }

    /**
     * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
     * they will (probably) have different hashes.
     *
     * @return The hash code for this object.
     */
    @Override
    public int hashCode() {
        int result = workRequestId != null ? workRequestId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        return result;
    }

    /**
     * Returns a String representation of this object.
     *
     * @return A String representation of this object.
     */
    @Override
    public String toString() {
        return "CandidateStatusKey{" +
                "workRequestId=" + workRequestId +
                ", status='" + status + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
