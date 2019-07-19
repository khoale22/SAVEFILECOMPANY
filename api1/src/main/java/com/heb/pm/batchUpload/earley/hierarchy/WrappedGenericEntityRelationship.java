package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.batchUpload.earley.WrappedEarlyEntity;
import com.heb.pm.entity.GenericEntityRelationship;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapps a GenericEntityRelationship for processing by the batch upload.
 *
 * @author d116773
 * @since 2.16.0
 */
public class WrappedGenericEntityRelationship extends WrappedEarlyEntity<List<GenericEntityRelationship>> {

	/**
	 * Creates a new WrappedGenericEntityRelationship.
	 *
	 * @param entity The entity to wrap.
	 */
	public WrappedGenericEntityRelationship(List<GenericEntityRelationship> entity) {
		super(entity);
	}

	/**
	 * Creates a new WrappedGenericEntityRelationship.
	 *
	 * @param message An error message generated while processing.
	 */
	public WrappedGenericEntityRelationship(String message) {
		super(message);
	}

	/**
	 * Convenience method for when this class will wrap only one GenericEntityRelationship.
	 * @param entity The GenericEntityRelationship to hold in the list.
	 */
	public WrappedGenericEntityRelationship(GenericEntityRelationship entity) {
		super(new LinkedList<>());
		this.getEntity().add(entity);
	}
}
