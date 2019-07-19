package com.heb.pm.batchUpload.earley;

/**
 * Exception thrown when an entity is not found.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EntityNotFoundException extends Exception {

	/**
	 * Constructs a new EntityNotFoundException.
	 * @param message The error message.
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}
}
