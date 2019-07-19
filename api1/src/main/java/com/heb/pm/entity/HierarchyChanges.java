package com.heb.pm.entity;

import java.util.List;

/**
 * This object holds generic entity relationships to add and remove
 * @author s753601
 * @version 2.16.0
 */
public class HierarchyChanges {

	private List<GenericEntityRelationship> relationshipsAdded;
	private List<GenericEntityRelationship> relationshipsRemoved;

	/**
	 * updates relationshipsAdded
	 * @return
	 */
	public List<GenericEntityRelationship> getRelationshipsAdded() {
		return relationshipsAdded;
	}

	/**
	 * Returns a list of generic entity relationships to be removed
	 * @param relationshipsAdded
	 */
	public void setRelationshipsAdded(List<GenericEntityRelationship> relationshipsAdded) {
		this.relationshipsAdded = relationshipsAdded;
	}
	/**
	 * Returns a list of generic entity relationships that will be added
	 * @return
	 */
	public List<GenericEntityRelationship> getRelationshipsRemoved() {
		return relationshipsRemoved;
	}

	/**
	 * updates relationshipsRemoved
	 * @param relationshipsRemoved
	 */
	public void setRelationshipsRemoved(List<GenericEntityRelationship> relationshipsRemoved) {
		this.relationshipsRemoved = relationshipsRemoved;
	}

}
