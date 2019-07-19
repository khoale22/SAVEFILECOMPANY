package com.heb.pm.entity;

import java.util.List;

/**
 * This class holds all the information required to link an image to a customer hierarchy
 * @author s753601
 * @version 2.13.0
 */
public class CopyToHierarchyRequest {

	private List<ProductScanImageURI> imageInfo;
	private List<GenericEntity> genericEntities;
	private List<Long> upcs;

	/**
	 * The list of imageInfo that will be attached to a hierarchy
	 * @return
	 */
	public List<ProductScanImageURI> getImageInfo() {
		return imageInfo;
	}

	/**
	 * Updates the imageInfo list
	 * @param imageInfo
	 */
	public void setImageInfo(List<ProductScanImageURI> imageInfo) {
		this.imageInfo = imageInfo;
	}

	/**
	 * Holds the levels of the hierarchy to be attached
	 * @return
	 */
	public List<GenericEntity> getGenericEntities() {
		return genericEntities;
	}

	/**
	 * Updates the genericEntities
	 * @param genericEntities
	 */
	public void setGenericEntities(List<GenericEntity> genericEntities) {
		this.genericEntities = genericEntities;
	}

	/**
	 * A list of upcs used to get the updated image info after the copy to hierarchy is complete
	 * @return
	 */
	public List<Long> getUpcs() {
		return upcs;
	}

	/**
	 * updates the upc list
	 * @param upcs
	 */
	public void setUpcs(List<Long> upcs) {
		this.upcs = upcs;
	}

	/**
	 * Compares another object to this one. If that object is a copy to hierarchy request, it uses they keys
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
		if (!(o instanceof CopyToHierarchyRequest)) {
			return false;
		}

		CopyToHierarchyRequest that = (CopyToHierarchyRequest) o;
		if(!this.imageInfo.equals(that.imageInfo)) return false;
		if(!this.genericEntities.equals(that.genericEntities)) return false;
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
		int result = this.imageInfo.hashCode();
		result = result * this.genericEntities.hashCode();
		return result;
	}
}
