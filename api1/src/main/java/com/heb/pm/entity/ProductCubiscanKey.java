package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Product Cubiscan Key.
 *
 * @author m594201
 * @since 2.7.0
 */
@Embeddable
public class ProductCubiscanKey implements Serializable{

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;


	@Column(name = "prod_id")
	private Long prodId;

	@Column(name = "dim_typ_cd")
	private String dimTypCd;

	@Column(name = "seq_nbr")
	private Long seqNumber;

	/**
	 * Gets prod id tied to the product that is associated with the cubiscan measuring data.
	 *
	 * @return the prod id tied to the product that is associated with the cubiscan measuring data.
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets prod id tied to the product that is associated with the cubiscan measuring data.
	 *
	 * @param prodId the prod id tied to the product tied to the product that is associated with the cubiscan measuring data.
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * Gets dim typ cd. that is tied the the cubiscan measuring data.
	 *
	 * @return the dimension typ cd that is tied the the cubiscan measuring data.
	 */
	public String getDimTypCd() {
		return dimTypCd;
	}

	/**
	 * Sets dim typ cd that is tied the the cubiscan measuring data.
	 *
	 * @param dimTypCd the dim typ cd that is tied the the cubiscan measuring data.
	 */
	public void setDimTypCd(String dimTypCd) {
		this.dimTypCd = dimTypCd;
	}

	/**
	 * Gets seq number used to manage the order to display data.
	 *
	 * @return the seq number used to manage the order to display data.
	 */
	public Long getSeqNumber() {
		return seqNumber;
	}

	/**
	 * Sets seq number used to manage the order to display data.
	 *
	 * @param seqNumber the seq number used to manage the order to display data.
	 */
	public void setSeqNumber(Long seqNumber) {
		this.seqNumber = seqNumber;
	}

	/**
	 * Compares another object to this one. If that object is an ProductCubiscanKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductCubiscanKey that = (ProductCubiscanKey) o;

		if (prodId != null ? !prodId.equals(that.prodId) : that.prodId != null) return false;
		if (dimTypCd != null ? !dimTypCd.equals(that.dimTypCd) : that.dimTypCd != null) return false;
		return seqNumber != null ? seqNumber.equals(that.seqNumber) : that.seqNumber == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = prodId != null ? prodId.hashCode() : 0;
		result = PRIME_NUMBER * result + (dimTypCd != null ? dimTypCd.hashCode() : 0);
		result = PRIME_NUMBER * result + (seqNumber != null ? seqNumber.hashCode() : 0);
		return result;
	}
}
