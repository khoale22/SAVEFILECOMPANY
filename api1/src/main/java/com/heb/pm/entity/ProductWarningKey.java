package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for a Product Warning.
 *
 * @author m594201
 * @since 2.14.0
 */
@Embeddable
public class ProductWarningKey implements Serializable {

	@Column(name = "prod_id")
	private Long prodId;

	@Column(name = "st_prod_warn_cd")
	private Long stateProductWarningCode;

	@Column(name = "st_cd")
	private String StateCode;

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	public Long getStateProductWarningCode() {
		return stateProductWarningCode;
	}

	public void setStateProductWarningCode(Long stateProductWarningCode) {
		this.stateProductWarningCode = stateProductWarningCode;
	}

	public String getStateCode() {
		return StateCode;
	}

	public void setStateCode(String stateCode) {
		StateCode = stateCode;
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
		if (!(o instanceof ProductWarningKey)) return false;

		ProductWarningKey that = (ProductWarningKey) o;

		if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) return false;
		if (stateProductWarningCode != null ? !stateProductWarningCode.equals(that.stateProductWarningCode) : that.stateProductWarningCode != null)
			return false;
		return StateCode != null ? StateCode.equals(that.StateCode) : that.StateCode == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = prodId != null ? prodId.hashCode() : 0;
		result = 31 * result + (stateProductWarningCode != null ? stateProductWarningCode.hashCode() : 0);
		result = 31 * result + (StateCode != null ? StateCode.hashCode() : 0);
		return result;
	}
}
