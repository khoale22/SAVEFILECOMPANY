/*
 * ProductionSupportControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productionSupport;

import com.heb.pm.index.IndexJobLauncher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParameters;
import testSupport.CommonMocks;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests ProductionSupportController.
 *
 * @author d116773
 * @since 2.0.2
 */
public class ProductionSupportControllerTest {

	/**
	 * Tests getAvailableJobs.
	 */
	@Test
	public void getAvailableJobs() {

		ProductionSupportController controller = new ProductionSupportController();
		controller.setUserInfo(CommonMocks.getUserInfo());

		Map<String, String> availableJobs = new HashMap<>();
		availableJobs.put("key", "value");
		controller.setAvailableJobs(availableJobs);

		Map<String, String> returnedJobs = controller.getAvailableJobs(CommonMocks.getServletRequest());
		Assert.assertEquals("value", returnedJobs.get("key"));
	}

	/**
	 * Tests runJob.
	 */
	@Test
	public void runJob() {

		ProductionSupportController controller = new ProductionSupportController();
		controller.setUserInfo(CommonMocks.getUserInfo());
		controller.setIndexJobLauncher(this.getJobLauncher());

		ProductionSupportController.BatchStatusWrapper status =
				controller.runJob("test", CommonMocks.getServletRequest());
		Assert.assertEquals("ABANDONED", status.getStatus());
		// Other tests happen in the answer from below.
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns an IndexJobLauncher to test with.
	 *
	 * @return An IndexJobLauncher to test with.
	 */
	private IndexJobLauncher getJobLauncher() {

		Answer<BatchStatus> runAnswer = invocation -> {

			// Make sure the user ID and start time are added as parameters to the job.
			JobParameters jobParameters = (JobParameters)invocation.getArguments()[1];
			Assert.assertNotNull(jobParameters.getString("userId"));
			Assert.assertNotNull(jobParameters.getDate("startTime"));
			return BatchStatus.ABANDONED;
		};

		IndexJobLauncher jobLauncher = Mockito.mock(IndexJobLauncher.class);
		Mockito.doAnswer(runAnswer).when(jobLauncher).runJob(Mockito.anyString(), Mockito.anyObject());

		return jobLauncher;
	}
}
