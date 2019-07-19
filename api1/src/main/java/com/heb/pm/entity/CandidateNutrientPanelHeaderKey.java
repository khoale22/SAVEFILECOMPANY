package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The primary key class for PS_NTRNT_PAN_HDR table.
 *
 * @author vn47792
 * @since 2.15.0
 */
@Embeddable
public class CandidateNutrientPanelHeaderKey implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "PS_WORK_ID")
	private Long workRequestId;

	@Column(name="SCN_CD_ID")
	private Long upc;

	@Column(name="NTRNT_PAN_NBR")
	private Integer ntrntPanNbr;

	@Column(name="SRC_SYSTEM_ID")
	private Integer sourceSystemId;

	/**
	 * Get the workRequestId.
	 *
	 * @return the workRequestId
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Set the workRequestId.
	 *
	 * @param workRequestId the workRequestId to set
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Get the upc.
	 *
	 * @return the upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Set the upc.
	 *
	 * @param upc the upc to set
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Get the ntrntPanNbr.
	 *
	 * @return the ntrntPanNbr
	 */
	public Integer getNtrntPanNbr() {
		return ntrntPanNbr;
	}

	/**
	 * Set the ntrntPanNbr.
	 *
	 * @param ntrntPanNbr the ntrntPanNbr to set
	 */
	public void setNtrntPanNbr(Integer ntrntPanNbr) {
		this.ntrntPanNbr = ntrntPanNbr;
	}

	/**
	 * Get the sourceSystemId.
	 *
	 * @return the sourceSystemId
	 */
	public Integer getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Set the sourceSystemId.
	 *
	 * @param sourceSystemId the sourceSystemId to set
	 */
	public void setSourceSystemId(Integer sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * Compares another object to this one for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateNutrientPanelHeaderKey)) return false;

		CandidateNutrientPanelHeaderKey that = (CandidateNutrientPanelHeaderKey) o;

		if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
			return false;
		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		if (ntrntPanNbr != null ? !ntrntPanNbr.equals(that.ntrntPanNbr) : that.ntrntPanNbr != null) return false;
		return sourceSystemId != null ? sourceSystemId.equals(that.sourceSystemId) : that.sourceSystemId == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = workRequestId != null ? workRequestId.hashCode() : 0;
		result = 31 * result + (upc != null ? upc.hashCode() : 0);
		result = 31 * result + (ntrntPanNbr != null ? ntrntPanNbr.hashCode() : 0);
		result = 31 * result + (sourceSystemId != null ? sourceSystemId.hashCode() : 0);
		return result;
	}
}
