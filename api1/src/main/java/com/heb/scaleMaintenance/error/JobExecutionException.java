package com.heb.scaleMaintenance.error;

/**
 * Thrown when there is an error starting a job.
 *
 * @author m314029
 * @since 2.17.8
 */
public class JobExecutionException extends RuntimeException {

	private static final long serialVersionUID = -8101816825596538952L;

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
