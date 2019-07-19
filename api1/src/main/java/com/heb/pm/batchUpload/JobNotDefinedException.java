package com.heb.pm.batchUpload;

/**
 * Exception thrown when the mass update load job is not defined.
 *
 * @author d116773
 * @since 2.12.0
 */
public class JobNotDefinedException extends RuntimeException {

	private static final String ERROR_MESSAGE = "Job %s not defined.";

	/**
	 * Constructs a new JobNotDefinedException.
	 *
	 * @param jobName The name of the mass update load job.
	 */
	public JobNotDefinedException(String jobName) {
		super(String.format(JobNotDefinedException.ERROR_MESSAGE, jobName));
	}
}
