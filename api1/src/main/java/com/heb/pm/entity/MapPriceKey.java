package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a key for map price.
 *
 * @author m314029
 * @since 2.13.0
 */
@Embeddable
public class MapPriceKey  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "scn_cd_id")
	private Long upc;

	@Column(name = "eff_ts")
	private LocalDateTime effectiveTime;

	/**
	 * Gets the upc tied to this map price.
	 *
	 * @return The upc.
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the upc for this map price.
	 *
	 * @param upc The upc to set for this map price.
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * Gets the effective time for this map price.
	 *
	 * @return The effective time for this map price.
	 */
	public LocalDateTime getEffectiveTime() {
		return effectiveTime;
	}

	/**
	 * Sets the effective time for this map price.
	 *
	 * @param effectiveTime The effective time for this map price.
	 */
	public void setEffectiveTime(LocalDateTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MapPriceKey that = (MapPriceKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		return effectiveTime != null ? effectiveTime.equals(that.effectiveTime) : that.effectiveTime == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = MapPriceKey.PRIME_NUMBER * result + (effectiveTime != null ? effectiveTime.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "MapPriceKey{" +
				"upc=" + upc +
				", effectiveTime=" + effectiveTime +
				'}';
	}
}
