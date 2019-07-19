package com.heb.pm.batchUpload.earley.attribute;

import com.heb.pm.batchUpload.earley.WrappedEarlyEntity;
import com.heb.pm.entity.EntityAttribute;

/**
 * Wraps an EntityAttribute for the Earley attribute load job.
 *
 * @author d116773
 * @since 2.16.0
 */
public class WrappedEntityAttribute extends WrappedEarlyEntity<EntityAttribute> {

	/**
	 * Constructs a new WrappedEntityAttribute.
	 *
	 * @param message The message for the object.
	 */
	public WrappedEntityAttribute(String message) {
		super(message);
	}

	/**
	 * Constructs a new WrappedEntityAttribute.
	 *
	 * @param entity The entity to wrap.
	 */
	public WrappedEntityAttribute(EntityAttribute entity) {
		super(entity);
	}
}
