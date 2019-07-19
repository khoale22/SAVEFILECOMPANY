package com.heb.pm.batchUpload.earley;

/**
 * Exception thrown processing an Earley file.
 *
 * @author d116773
 * @since 2.15.0
 */
public class FileProcessingException extends Exception {

	/**
	 * Constructs a new FileProcessingException.
	 *
	 * @param message The error message.
	 */
	public FileProcessingException(String message) {
		super(message);
	}
}
