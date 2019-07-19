package com.heb.pm.massUpdate.job.workRequestCreators;

/**
 * Exception thrown when the factory is asked to create a work request creator it doesn't know how to.
 *
 * @author d116773
 * @since 2.12.0
 */
public class UnknownAttributeException extends RuntimeException{

	/**
	 * Creates a new UnknownAttributeException.
	 * @param message The error message.
	 */
	public UnknownAttributeException(String message){
		super(message);
	}
}
