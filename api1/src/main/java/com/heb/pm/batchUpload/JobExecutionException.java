package com.heb.pm.batchUpload;

/**
 * Thrown when there is an error starting the mass update load job.
 *
 * @author d116773
 * @since 2.12.0
 */
public class JobExecutionException extends RuntimeException {

	/**
	 * Constructs a new JobExecutionException.
	 *
	 * @param message The error message.
	 * @param cause The root cause of the error.
	 */
	public JobExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}
