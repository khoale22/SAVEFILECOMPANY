/*
 * JobLauncher
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

/**
 * Bean used to kick off index refresh jobs.
 *
 * This class is based on an example from Mk Yong that can be found here:
 * http://www.mkyong.com/spring-batch/spring-batch-and-quartz-scheduler-example/
 *
 * @author d116773
 * @since 2.0.2
 */
public class WicJobLauncher extends QuartzJobBean {

	private static final Logger LOGGER = LogManager.getLogger(WicJobLauncher.class);

	// The key in the job data map that will hold the name of the job to start.
	private static final String JOB_NAME = "jobName";

	private static final String LAUNCHING_JOB_LOG_MESSAGE = "Launching job %s";

	// The
	private static String hostName;
	static {

		try {
			WicJobLauncher.hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			WicJobLauncher.hostName = "Unknown";
		}
	}

	// Used to find jobs to run.
	private JobLocator jobLocator;

	// Used to kick off jobs.
	private org.springframework.batch.core.launch.JobLauncher jobLauncher;

	/**
	 * Sets the JobLocator. This will be called by the Spring framework when the job is started by Quartz.
	 *
	 * @param jobLocator The JobLocator.
	 */
	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}

	/**
	 * Sets the JobCrawlerLauncher. This will be called by the Spring framework when the job is started by Quartz.
	 *
	 * @param jobLauncher The JobCrawlerLauncher.
	 */
	public void setJobLauncher(org.springframework.batch.core.launch.JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}


	/**
	 * Called by the Spring Quartz framework to start the job.
	 *
	 * @param context The JobExecutionContext for the job it will kick off.
	 */
	@Override
	public void executeInternal(JobExecutionContext context) {

		JobDataMap jobDataMap = context.getMergedJobDataMap();

		String jobName = (String) jobDataMap.get(JOB_NAME);

		JobParametersBuilder jobParametersBuilder = getJobParametersFromJobMap(jobDataMap);

		this.runJob(jobName, jobParametersBuilder);
	}


	/**
	 * Runs the job.
	 *
	 * @param jobName The name of the job to run.
	 * @param jobParametersBuilder The parameters builder that contains parameters to pass to the job.
	 * @return The status of the job after completion. This may be null, but I cannot say under what circumstances.
	 */
	public BatchStatus runJob(String jobName, JobParametersBuilder jobParametersBuilder) {

		WicJobLauncher.LOGGER.info(String.format(WicJobLauncher.LAUNCHING_JOB_LOG_MESSAGE, jobName));

		BatchStatus batchStatus = null;

		try {
			this.addStandardParameters(jobParametersBuilder);
			JobExecution jobExecution = jobLauncher.run(this.jobLocator.getJob(jobName),
					jobParametersBuilder.toJobParameters());
			batchStatus = jobExecution.getStatus();
		} catch ( NoSuchJobException | JobParametersInvalidException | JobRestartException |
				JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException e) {
			WicJobLauncher.LOGGER.error(e.getMessage());
		}

		return batchStatus;
	}

	/**
	 * Adds parameters that are standard across all runs of all jobs. Currently:
	 *
	 * 1. Run date.
	 * 2. The hostname of the server the job is running in.
	 *
	 * @param jobParametersBuilder The JobParametersBuilder to add the parameters to.
	 */
	private void addStandardParameters(JobParametersBuilder jobParametersBuilder) {

		// Add a parameter with today's date. This will add a unique parameter so the jobs can run more
		// than once.
		jobParametersBuilder.addDate("runDate", new Date());

		// Since this is an environment with multiple servers, add the host name to make each execution from each
		// server unique.
		jobParametersBuilder.addString("hostName", WicJobLauncher.hostName);
	}

	/**
	 * Extracts job parameters from the map passed in by the Spring Quartz framework.
	 *
	 * @param jobDataMap The map of job parameters passed in by the Spring Quartz framework.
	 * @return The parameters builder that will create the parameters to pass to the job.
	 */
	private JobParametersBuilder getJobParametersFromJobMap(JobDataMap jobDataMap) {

		JobParametersBuilder builder = new JobParametersBuilder();

		for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			// Loop through all the entries in jobDataMap and add
			// anything that is a string, date, or number. There's other stuff there
			// like the JobLocator and JobCrawlerLauncher, but they are not needed by
			// the jobs this class will trigger. There is also an entry for the job name
			// which is also not needed.
			if (value instanceof String && !key.equals(WicJobLauncher.JOB_NAME)) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			}
		}

		return builder;
	}
}
