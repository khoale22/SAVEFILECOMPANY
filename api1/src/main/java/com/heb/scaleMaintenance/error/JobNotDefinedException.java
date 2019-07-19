package com.heb.scaleMaintenance.error;

/**
 * Exception thrown when the mass update load job is not defined.
 *
 * @author m314029
 * @since 2.17.8
 */
public class JobNotDefinedException extends RuntimeException {

	private static final String ERROR_MESSAGE = "Job %s not defined.";
	private static final long serialVersionUID = -186914668152683276L;

	/**
	 * Constructs a new JobNotDefinedException.
	 *
	 * @param jobName The name of the mass update load job.
	 */
	public JobNotDefinedException(String jobName) {
		super(String.format(JobNotDefinedException.ERROR_MESSAGE, jobName));
	}
}
