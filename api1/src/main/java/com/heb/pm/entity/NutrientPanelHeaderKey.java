package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The primary key class for NTRNT_PAN_HDR table.
 *
 * @author vn73545
 * @since 2.15.0
 */
@Embeddable
public class NutrientPanelHeaderKey implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="SCN_CD_ID")
	private Long upc;

	@Column(name="NTRNT_PAN_NBR")
	private Long ntrntPanNbr;

	@Column(name="SRC_SYSTEM_ID")
	private Long sourceSystemId;

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
	public Long getNtrntPanNbr() {
		return ntrntPanNbr;
	}

	/**
	 * Set the ntrntPanNbr.
	 *
	 * @param ntrntPanNbr the ntrntPanNbr to set
	 */
	public void setNtrntPanNbr(Long ntrntPanNbr) {
		this.ntrntPanNbr = ntrntPanNbr;
	}

	/**
	 * Get the sourceSystemId.
	 *
	 * @return the sourceSystemId
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Set the sourceSystemId.
	 *
	 * @param sourceSystemId the sourceSystemId to set
	 */
	public void setSourceSystemId(Long sourceSystemId) {
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
		if (!(o instanceof NutrientPanelHeaderKey)) return false;

		NutrientPanelHeaderKey that = (NutrientPanelHeaderKey) o;

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
		int result = upc != null ? upc.hashCode() : 0;
		result = 31 * result + (ntrntPanNbr != null ? ntrntPanNbr.hashCode() : 0);
		result = 31 * result + (sourceSystemId != null ? sourceSystemId.hashCode() : 0);
		return result;
	}
}
