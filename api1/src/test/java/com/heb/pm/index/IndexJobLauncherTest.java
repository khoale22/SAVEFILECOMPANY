/*
 * IndexJobLauncherTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Tests IndexJobLauncher.
 *
 * @author d116773
 * @since 2.0.2
 */
public class IndexJobLauncherTest {

	private static final String TEST_JOB_NAME = "test-job";
	private static final String TEST_JOB_PARM_KEY = "testJobParmKey";
	private static final String TEST_JOB_PARM_VALUE = "testJobParmValue";

	private static final String TEST_JOB_KEY = "jobName";
	private static final String STRING_KEY = "stringKey";
	private static final String STRING_VAL = "TEST-STRING";
	private static final String FLOAT_KEY = "floatKey";
	private static final Float FLOAT_VALUE = 3345.5F;
	private static final String DOUBLE_KEY = "doubleKey";
	private static final Double DOUBLE_VALUE = 232323.32;
	private static final String INTEGER_KEY = "integerKey";
	private static final Integer INTEGER_VALUE = 2332;
	private static final String LONG_KEY = "longKey";
	private static final Long LONG_VALUE = 2332432L;
	private static final String DATE_KEY = "dateKey";
	private static final Date DATE_VALUE = new Date();


	/**
	 * Tests runJob.
	 */
	@Test
	public void runJob() {
		IndexJobLauncher indexJobLauncher = new IndexJobLauncher();
		indexJobLauncher.setJobLauncher(this.getJobLauncher(this.getManualLaunchAnswer()));
		indexJobLauncher.setJobLocator(this.getJobLocator());
		indexJobLauncher.runJob(IndexJobLauncherTest.TEST_JOB_NAME,this.getTestJobParametersManual());
	}

	/**
	 * Tests executeInternal.
	 */
	@Test
	public void executeInternal() {
		IndexJobLauncher indexJobLauncher = new IndexJobLauncher();
		indexJobLauncher.setJobLauncher(this.getJobLauncher(this.getQuartzLaunchAnswer()));
		indexJobLauncher.setJobLocator(this.getJobLocator());
		indexJobLauncher.executeInternal(this.getJobExecutionContext());
	}


	/*
	 * Support functions
	 */

	/**
	 * Returns a set of JobParameters that can be used for the manual job kickoff.
	 *
	 * @return A set of JobParameters that can be used for the manual job kickoff.
	 */
	private JobParametersBuilder getTestJobParametersManual() {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString(IndexJobLauncherTest.TEST_JOB_PARM_KEY,
				IndexJobLauncherTest.TEST_JOB_PARM_VALUE);
		return jobParametersBuilder;
	}

	/**
	 * Returns a job map that simulates being run from Quartz.
	 *
	 * @return A job map that simulates being run from Quartz.
	 */
	private JobDataMap getJobMap() {
		JobDataMap jobMap = new JobDataMap();

		jobMap.put(IndexJobLauncherTest.TEST_JOB_KEY, IndexJobLauncherTest.TEST_JOB_NAME);
		jobMap.put(IndexJobLauncherTest.STRING_KEY, IndexJobLauncherTest.STRING_VAL);
		jobMap.put(IndexJobLauncherTest.FLOAT_KEY, IndexJobLauncherTest.FLOAT_VALUE);
		jobMap.put(IndexJobLauncherTest.INTEGER_KEY, IndexJobLauncherTest.INTEGER_VALUE);
		jobMap.put(IndexJobLauncherTest.DOUBLE_KEY, IndexJobLauncherTest.DOUBLE_VALUE);
		jobMap.put(IndexJobLauncherTest.LONG_KEY, IndexJobLauncherTest.LONG_VALUE);
		jobMap.put(IndexJobLauncherTest.DATE_KEY, IndexJobLauncherTest.DATE_VALUE);

		return jobMap;
	}

	/**
	 * Returns a JobExecutionContext to simulate being called by Quartz.
	 *
	 * @return A JobExecutionContext to simulate being called by Quartz.
	 */
	private JobExecutionContext getJobExecutionContext() {

		JobExecutionContext ctx = Mockito.mock(JobExecutionContext.class);
		Mockito.when(ctx.getMergedJobDataMap()).thenReturn(this.getJobMap());
		return ctx;
	}

	/**
	 * Returns an instance of a JobExecution that can be checked for completion.
	 *
	 * @return An instance of a JobExecution that can be checked for completion.
	 */
	private JobExecution getJobExecution() {

		JobExecution jobExecution = Mockito.mock(JobExecution.class);
		Mockito.when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
		return jobExecution;
	}


	/**
	 * Returns a Job which will have a name of the supplied parameter.
	 *
	 * @param jobName The name of the job.
	 * @return A Job.
	 */
	private Job getJob(String jobName) {

		Job job = Mockito.mock(Job.class);
		Mockito.when(job.getName()).thenReturn(jobName);

		return job;
	}

	/**
	 * Returns a JobLocator.
	 *
	 * @return A JobLocator.
	 */
	private JobLocator getJobLocator() {

		Job job = this.getJob(IndexJobLauncherTest.TEST_JOB_NAME);

		JobLocator jobLocator = Mockito.mock(JobLocator.class);
		try {
			Mockito.when(jobLocator.getJob(Mockito.anyString()))
					.thenReturn(job);
		} catch (NoSuchJobException e) {
			Assert.fail(e.getMessage());
		}
		return jobLocator;
	}

	/**
	 * Returns an Answer that will check against a manual launch of the job.
	 *
	 * @return An Answer that will check against a manual launch of the job.
	 */
	Answer<JobExecution> getManualLaunchAnswer() {
		return invocation -> {
			Assert.assertEquals(IndexJobLauncherTest.TEST_JOB_NAME, ((Job)invocation.getArguments()[0]).getName());
			Assert.assertEquals(IndexJobLauncherTest.TEST_JOB_PARM_VALUE,
					((JobParameters)invocation.getArguments()[1]).getString(IndexJobLauncherTest.TEST_JOB_PARM_KEY));
			return this.getJobExecution();
		};
	}

	/**
	 * Returns an Answer that will check against a Quartz launch of the job.
	 *
	 * @return An Answer that will check against a Quartz launch of the job.
	 */
	Answer<JobExecution> getQuartzLaunchAnswer() {
		return invocation -> {
			Assert.assertEquals(IndexJobLauncherTest.TEST_JOB_NAME, ((Job) invocation.getArguments()[0]).getName());
			JobParameters jobParameters = (JobParameters) invocation.getArguments()[1];
			Assert.assertEquals(IndexJobLauncherTest.DATE_VALUE, jobParameters.getDate(IndexJobLauncherTest.DATE_KEY));
			Assert.assertEquals(IndexJobLauncherTest.DOUBLE_VALUE, jobParameters.getDouble(IndexJobLauncherTest.DOUBLE_KEY), 1.0);
			Assert.assertEquals(IndexJobLauncherTest.FLOAT_VALUE, jobParameters.getDouble(IndexJobLauncherTest.FLOAT_KEY), 1.0);
			Assert.assertEquals(Long.valueOf(IndexJobLauncherTest.INTEGER_VALUE.longValue()), jobParameters.getLong(IndexJobLauncherTest.INTEGER_KEY));
			Assert.assertEquals(IndexJobLauncherTest.LONG_VALUE, jobParameters.getLong(IndexJobLauncherTest.LONG_KEY));
			Assert.assertEquals(IndexJobLauncherTest.STRING_VAL, jobParameters.getString(IndexJobLauncherTest.STRING_KEY));
			return this.getJobExecution();
		};
	}

	/**
	 * Returns a JobLauncher that will check the call to runJob the supplied Answer.
	 *
	 * @param answer the Answer to use to check hte call to runJob.
	 * @return A JobLauncher.
	 */
	private JobLauncher getJobLauncher(Answer<JobExecution> answer) {
		JobLauncher jobLauncher = Mockito.mock(JobLauncher.class);

		try {
			Mockito.doAnswer(answer).when(jobLauncher).run(Mockito.anyObject(), Mockito.anyObject());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException |
				JobInstanceAlreadyCompleteException e) {
			Assert.fail(e.getMessage());
		}
		return jobLauncher;
	}
}
