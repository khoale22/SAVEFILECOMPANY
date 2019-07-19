package com.heb.pm.productionSupport;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.index.IndexJobLauncher;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * REST endpoint for production support functions.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.INDEX_REFRESH_JOBS)
public class ProductionSupportController {

	private static final Logger logger = LoggerFactory.getLogger(ProductionSupportController.class);

	private static final String PRODUCTION_SUPPORT_URL = "support";
	private static final String JOBS_URL = PRODUCTION_SUPPORT_URL + "/jobs";
	private static final String EXECUTE_JOB_URL = JOBS_URL + "/{jobName}";

	private static final String JOB_LIST_LOG_MESSAGE = "User %s from IP %s has requested a list of jobs.";

	private static final String EMPTY_JOB_NAME_ERROR_MESSAGE = "Job Name required.";
	private static final String JOB_RUN_LOG_MESSAGE = "User %s from IP %s has requested job %s be started.";
	private static final String USER_ID_PARAMETER = "userId";
	private static final String START_TIME_PARAMETER = "startTime";
	private static final String JOB_RUN_ERROR_MESSAGE = "Job %s failed, check log";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private IndexJobLauncher indexJobLauncher;

	@Value("#{${app.productionSupport.jobs}}")
	private Map<String, String> availableJobs;

	/**
	 * Wraps a batch status in an object to be sent back to the front-end.
	 *
	 * @author d116773
	 * @since 2.2.2
	 */
	public class BatchStatusWrapper {

		private String status;

		/**
		 * Constructs a new BatchStatusWrapper.
		 *
		 * @param batchStatus The status to wrap.
		 */
		public BatchStatusWrapper(BatchStatus batchStatus) {
			this.status = batchStatus.toString();
		}

		/**
		 * Returns the status of a batch job as a String.
		 *
		 * @return The status of a batch job.
		 */
		public String getStatus() {
			return status;
		}
	}

	/**
	 * Get available jobs map.
	 *
	 * @param request the request
	 * @return the map
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductionSupportController.JOBS_URL)
	public Map<String, String> getAvailableJobs (HttpServletRequest request){

		this.logRequestJobList(request.getRemoteAddr());

		return this.availableJobs;
	}


	/**
	 * Endpoint where production support can request a job to be run..
	 *
	 * @param jobName The name of the job to run.
	 * @param request The HTTP Request initiating this call.
	 * @return A string that represents the status of the job.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductionSupportController.EXECUTE_JOB_URL)
	public @ResponseBody BatchStatusWrapper runJob(@PathVariable String jobName, HttpServletRequest request) {

		// Job name is required.
		if (jobName == null || jobName.isEmpty()) {
			throw new IllegalArgumentException(ProductionSupportController.EMPTY_JOB_NAME_ERROR_MESSAGE);
		}

		this.logJobRun(request.getRemoteAddr(), jobName);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString(ProductionSupportController.USER_ID_PARAMETER, this.userInfo.getUserId());
		jobParametersBuilder.addDate(ProductionSupportController.START_TIME_PARAMETER, new Date());

		BatchStatus batchStatus = this.indexJobLauncher.runJob(jobName, jobParametersBuilder);

		if (batchStatus == null) {
			throw new BatchException(String.format(ProductionSupportController.JOB_RUN_ERROR_MESSAGE, jobName));
		}
		return new BatchStatusWrapper(batchStatus);
	}

	/**
	 * Sets the UserInfo for this object to use. This is used for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the IndexJobLauncher for this object to use.
	 *
	 * @param indexJobLauncher The IndexJobLauncher for this class to use.
	 */
	public void setIndexJobLauncher(IndexJobLauncher indexJobLauncher) {
		this.indexJobLauncher = indexJobLauncher;
	}

	/**
	 * Logs a user's request for a list of jobs to run.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logRequestJobList(String ip) {
		ProductionSupportController.logger.info(
				String.format(ProductionSupportController.JOB_LIST_LOG_MESSAGE, this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Sets the list of available jobs. This method is mainly for testing as Spring will set the value as part of
	 * application configuration.
	 *
	 * @param availableJobs The list of available jobs.
	 */
	public void setAvailableJobs(Map<String, String> availableJobs) {
		this.availableJobs = availableJobs;
	}

	/**
	 * Logs the user request to run a job.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logJobRun(String ip, String jobName) {
		ProductionSupportController.logger.info(
				String.format(ProductionSupportController.JOB_RUN_LOG_MESSAGE, this.userInfo.getUserId(), ip, jobName)
		);
	}
}
