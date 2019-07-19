package com.heb.pm.entity;

import java.util.List;

/**
 * This class holds all of the changes needed to the destinations of an image
 */
public class DestinationChanges {

	private static final int FOUR_BYTES = 32;

	private long upc;

	private long sequenceNumber;

	private Boolean activeSwitch;

	private List<String> destinationsAdded;

	private List<String> destinationsRemoved;

	/**
	 * Gets the UPC ID for the image that is getting the destination update
	 * @return
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Updates the upc
	 * @param upc the new upc
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * The sequence number for the image that is getting the destination update
	 * @return the sequence number
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates the sequence number
	 * @param sequenceNumber the new sequence number
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * The list of destinations(sals_chnl_cd) that need to be added to an image
	 * @return destination
	 */
	public List<String> getDestinationsAdded() {
		return destinationsAdded;
	}

	/**
	 * Updates the destinations added
	 * @param destinationsAdded the new destinations to be added
	 */
	public void setDestinationsAdded(List<String> destinationsAdded) {
		this.destinationsAdded = destinationsAdded;
	}

	/**
	 * The list of destinations(sals_chnl_cd) that need to be removed to an image
	 * @return destination
	 */
	public List<String> getDestinationsRemoved() {
		return destinationsRemoved;
	}

	/**
	 * Updates the destinations removed
	 * @param getDestinationsRemoved the removed destinations
	 */
	public void setDestinationsRemoved(List<String> getDestinationsRemoved) {
		this.destinationsRemoved = getDestinationsRemoved;
	}

	/**
	 * The active switch which is determined from the image uri
	 * @return
	 */
	public Boolean getActiveSwitch() {
		return activeSwitch;
	}

	/**
	 * Updates the active switch
	 * @param activeSwitch
	 */
	public void setActiveSwitch(Boolean activeSwitch) {
		this.activeSwitch = activeSwitch;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "DestinationChanges{" +
				"upc=" + upc +
				", sequenceNumber=" + sequenceNumber +
				", destinationsAdded=" + destinationsAdded +
				", getDestinationsRemoved=" + destinationsRemoved +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DestinationChanges)) {
			return false;
		}

		DestinationChanges that = (DestinationChanges) o;
		if (this.upc != that.upc) return false;
		if (this.sequenceNumber != that.sequenceNumber) return false;
		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = (int) (upc ^ (upc >>> FOUR_BYTES));
		result= result + (int) (sequenceNumber ^ (sequenceNumber >>> FOUR_BYTES));
		return result;
	}
}
