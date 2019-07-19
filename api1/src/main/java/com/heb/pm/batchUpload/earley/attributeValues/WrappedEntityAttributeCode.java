package com.heb.pm.batchUpload.earley.attributeValues;

import com.heb.pm.batchUpload.earley.WrappedEarlyEntity;
import com.heb.pm.entity.EntityAttributeCode;

/**
 * Wraps an EntityAttributeCode to be saved as part of the Earley attribute/value load.
 *
 * @author d116773
 * @since 2.16.0
 */
public class WrappedEntityAttributeCode extends WrappedEarlyEntity<EntityAttributeCode> {

	/**
	 * Constructs a new WrappedEntityAttributeCode.
	 *
	 * @param entity The EntityAttributeCode to wrap.
	 */
	public WrappedEntityAttributeCode(EntityAttributeCode entity) {
		super(entity);
	}

	/**
	 * Constructs a new WrappedEntityAttributeCode.
	 *
	 * @param message The message tied to this record.
	 */
	public WrappedEntityAttributeCode(String message) {
		super(message);
	}
}
