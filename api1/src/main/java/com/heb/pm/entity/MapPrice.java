package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a map price.
 *
 * @author m314029
 * @since 2.13.0
 */
@Entity
@Table(name = "pd_map_price")
public class MapPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MapPriceKey key;

	@Column(name = "map_amt")
	private Double mapAmount;

	@Column(name = "exprn_ts")
	private LocalDateTime expirationTime;

	@Transient
	private String actionCode;

	/**
	 * Gets the key for this map price.
	 *
	 * @return The key.
	 */
	public MapPriceKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this map price.
	 * 
	 * @param key The key to set.
	 */
	public void setKey(MapPriceKey key) {
		this.key = key;
	}

	/**
	 * Gets the map amount for this map price.
	 *
	 * @return The map amount.
	 */
	public Double getMapAmount() {
		return mapAmount;
	}

	/**
	 * Sets the map amount for this map price.
	 *
	 * @param mapAmount The map amount to set.
	 */
	public void setMapAmount(Double mapAmount) {
		this.mapAmount = mapAmount;
	}

	/**
	 * Gets the expiration time for this map price. This is when the map price expires.
	 *
	 * @return The expiration time.
	 */
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Sets the expiration time for this map price.
	 *
	 * @param expirationTime The expiration time.
	 */
	public void setExpirationTime(LocalDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * This is the action code for this map price.
	 *
	 * @return The action code.
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Set the action code for this map price.
	 *
	 * @param actionCode The action code.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
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

		MapPrice mapPrice = (MapPrice) o;

		return key != null ? key.equals(mapPrice.key) : mapPrice.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "MapPrice{" +
				"key=" + key +
				", mapAmount=" + mapAmount +
				", expirationTime=" + expirationTime +
				'}';
	}
}
