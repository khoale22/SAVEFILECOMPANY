package com.heb.scaleMaintenance.service;

import com.heb.scaleMaintenance.EPlumTransactional;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceUpcKey;
import com.heb.scaleMaintenance.error.JobExecutionException;
import com.heb.scaleMaintenance.error.JobNotDefinedException;
import com.heb.scaleMaintenance.model.ScaleMaintenance;
import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import com.heb.scaleMaintenance.repository.ScaleMaintenanceUpcRepository;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds the business logic for scale maintenance loads.
 *
 * @author m314029
 * @since 2.17.8
 */
@Service
public class ScaleMaintenanceService {

	private static final Logger logger = LoggerFactory.getLogger(ScaleMaintenanceService.class);

	// load job name
	private static final String LOAD_JOB_NAME = "I18X005";
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	// load job name
	private static final String LOAD_BY_DEPTNBR_JOB_NAME = "J89WSLD";

	// load job parameters
	private static final String TRANSACTION_ID_PARAMETER = "transactionId";
	private static final String USER_ID_PARAMETER = "userId";
	private static final String START_TIME_PARAMETER = "startTime";
	private static final String UPCS_PARAMETER = "upcs";
	private static final String STORES_PARAMETER = "sourceStoreNo";
	private static final String USER_IP_PARAMETER = "userIp";
	private static final String DEPTNBR_PARAMETER = "departmentNbr";
	private static final String TARGETSTORE_PARAMETER = "targetStoreNbr";
	private static final String EFFECTIVEDATE_PARAMETER = "effectiveDate";
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	// log messages
	private static final String JOB_LOCATOR_LOG_MESSAGE = "Looking for job %s.";
	private static final String JOB_PARAMETERS_LOG_MESSAGE = "Starting load job with parameters: %s.";

	// error messages
	private static final String JOB_EXECUTION_ERROR_MESSAGE = "Unable to execute job %s: %s";

	@Autowired
	private ScaleMaintenanceTrackingService scaleMaintenanceTrackingService;

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private JobLocator jobLocator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceTransmitService scaleMaintenanceTransmitService;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	@Autowired
	private ScaleMaintenanceUpcRepository scaleMaintenanceUpcRepository;

	/**
	 * Builds a scale maintenance load job.
	 *
	 * @param upcs          The upcs to set as 'upcs' parameter.
	 * @param stores        The stores to set as 'stores' parameter.
	 * @param transactionId The transaction id to set as the 'transactionId' parameter.
	 * @param userIp        IP of the user creating this request
	 */
	private void buildScaleMaintenanceLoadJob(String upcs, String stores, Long transactionId, String userIp) {

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString(ScaleMaintenanceService.USER_ID_PARAMETER, this.userInfo.getUserId());
		jobParametersBuilder.addDate(ScaleMaintenanceService.START_TIME_PARAMETER, new Date());
		jobParametersBuilder.addLong(ScaleMaintenanceService.TRANSACTION_ID_PARAMETER, transactionId);
		jobParametersBuilder.addString(ScaleMaintenanceService.USER_IP_PARAMETER, userIp);
		//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		jobParametersBuilder.addLong(ScaleMaintenanceService.TARGETSTORE_PARAMETER, Long.valueOf(0));
		jobParametersBuilder.addDate(ScaleMaintenanceService.EFFECTIVEDATE_PARAMETER, new Date());
		//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

		this.logJobParameters(jobParametersBuilder.toJobParameters());

		this.runScaleMaintenanceLoadJob(jobParametersBuilder);
	}

	/**
	 * Logs the parameters for this load job.
	 *
	 * @param jobParameters Job parameters for this scale maintenance load job.
	 */
	private void logJobParameters(JobParameters jobParameters) {
		ScaleMaintenanceService.logger.info(
				String.format(ScaleMaintenanceService.JOB_PARAMETERS_LOG_MESSAGE, jobParameters));
	}

	/**
	 * Runs the scale maintenance job asynchronously (so the user gets the transaction id as soon as possible).
	 *
	 * @param jobParametersBuilder Job parameters for this scale maintenance load.
	 */
	private void runScaleMaintenanceLoadJob(JobParametersBuilder jobParametersBuilder) {

		Job job = this.getScaleMaintenanceLoadJob();
		try {
			// Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
			this.jobLauncher.run(job, jobParametersBuilder.toJobParameters());

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				JobParametersInvalidException e) {

			ScaleMaintenanceService.logger.error(e.getMessage());
			throw new JobExecutionException(String.format(ScaleMaintenanceService.JOB_EXECUTION_ERROR_MESSAGE,
					LOAD_JOB_NAME, e.getMessage()), e.getCause());
		}
	}

	/**
	 * Returns a reference to the scale maintenance load job.
	 *
	 * @return A reference to the scale maintenance load job.
	 */
	private Job getScaleMaintenanceLoadJob() {

		try {
			ScaleMaintenanceService.logger.debug(String.format(JOB_LOCATOR_LOG_MESSAGE, LOAD_JOB_NAME));
			return this.jobLocator.getJob(LOAD_JOB_NAME);

		} catch (NoSuchJobException e) {
			JobNotDefinedException je = new JobNotDefinedException(LOAD_JOB_NAME);
			ScaleMaintenanceService.logger.error(je.getMessage());
			throw je;
		}
	}

	/**
	 * Creates a scale maintenance tracking, uses its tracking id, upcs, and stores to initiate a new (asynchronous)
	 * load batch job, then returns the scale maintenance tracking.
	 *
	 * @param parameters Information surrounding the stores and PLU to send to ePlum.
	 * @param userIp     IP of the user creating this request
	 * @return New scale maintenance tracking.
	 */
	public ScaleMaintenanceTracking submitLoadToStoresForPlu(ScaleMaintenanceLoadParameters parameters, String userIp) {
		ScaleMaintenanceTracking newTracking = this.createTrackingAndTransmitsForLoad(parameters);
		//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		this.buildScaleMaintenanceLoadJob(
				parameters.getUpcs(),
				parameters.getStores(),
				newTracking.getTransactionId(),
				userIp);
        //End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		return newTracking;
	}

	/**
	 * This calls scale maintenance tracking service to create a tracking for a load, then calls scale maintenance
	 * transmit service to create transmits linked the the new tracking, then returns the new tracking. This method
	 * had to be broken into its own method to make it transactional without including the batch job.
	 *
	 * @param parameters Parameters for scale maintenance load.
	 * @return New scale maintenance tracking.
	 */
	@EPlumTransactional
	ScaleMaintenanceTracking createTrackingAndTransmitsForLoad(ScaleMaintenanceLoadParameters parameters) {
		ScaleMaintenanceTracking newTracking = this.scaleMaintenanceTrackingService.createLoad(parameters);
        
		this.scaleMaintenanceTransmitService.createForLoad(parameters.getStores(), newTracking.getTransactionId());
		return newTracking;
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	
	/**
	 * This calls scale maintenance tracking service to create a tracking for a load, then calls scale maintenance
	 * transmit service to create transmits linked the the new tracking, then returns the new tracking. This method
	 * had to be broken into its own method to make it transactional without including the batch job.
	 *
	 * @param parameters Parameters for scale maintenance load.
	 * @return New scale maintenance tracking.
	 */
	@EPlumTransactional
	ScaleMaintenanceTracking createTrackingAndTransmitsForLoadByDept(ScaleMaintenanceLoadParameters parameters) {
		ScaleMaintenanceTracking newTracking = this.scaleMaintenanceTrackingService.createLoadByDept(parameters);
		this.scaleMaintenanceTransmitService.createForLoad(parameters.getStores(), newTracking.getTransactionId());
		return newTracking;
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	/**
	 * This method retrieves the available stores to send a scale maintenance load to.
	 *
	 * @return List of store numbers that are able to send scale maintenance loads to.
	 */
	public List<Integer> getAvailableLoadStores() {
		return EPlumApiUtils.LAB_STORES;
	}

	/**
	 * Returns scale maintenance objects by store and transaction id.
	 *
	 * @param page page object.
	 * @param pageSize size field of page object.
	 * @param includeCount whether or not to include the count.
	 * @param store store number.
	 * @param transactionId id of the transaction in question.
	 * @return scale maintenance objects by store and transaction id.
	 */
	public PageableResult<ScaleMaintenance> findAllScaleMaintenanceByStoreAndTransactionId(int page, int pageSize, boolean includeCount, int store, long transactionId) {
		List<ScaleMaintenance> scaleMaintenanceList = new ArrayList<>();
		PageableResult<ScaleMaintenanceAuthorizeRetail> retails = this.scaleMaintenanceAuthorizeRetailService
				.findAllByTransactionIdAndStore(page, pageSize, includeCount, transactionId, store);
		ScaleMaintenance scaleMaintenance;
		for (ScaleMaintenanceAuthorizeRetail retail : retails.getData()) {
			scaleMaintenance = new ScaleMaintenance();
			scaleMaintenanceList.add(scaleMaintenance);
			scaleMaintenance.setAuthorizeRetail(retail);
			if (retail.getAuthorized()) {
				ScaleMaintenanceUpcKey scaleMaintenanceUpcKey = new ScaleMaintenanceUpcKey().setUpc(
						retail.getKey().getUpc()).setTransactionId(retail.getKey().getTransactionId());
						scaleMaintenance.setScaleMaintenanceUpc(this.scaleMaintenanceUpcRepository.findOne(scaleMaintenanceUpcKey));
			}

		}
		return 	new PageableResult<>(page, scaleMaintenanceList);
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	
	/**
	 * Creates a scale maintenance tracking, uses its tracking id, upcs, and stores to initiate a new (asynchronous)
	 * load batch job, then returns the scale maintenance tracking.
	 *
	 * @param parameters Information surrounding the stores and PLU to send to ePlum.
	 * @param userIp     IP of the user creating this request
	 * @return New scale maintenance tracking.
	 */
	public ScaleMaintenanceTracking submitLoadToStoresByDepartmentNbr(ScaleMaintenanceLoadParameters parameters, String userIp) {
		ScaleMaintenanceTracking newTracking = this.createTrackingAndTransmitsForLoadByDept(parameters);
		this.buildScaleMaintenanceLoadByDepartmentJob(
				parameters,
				newTracking.getTransactionId(),
				userIp);

		return newTracking;
	}
	
	/**
	 * Builds a scale maintenance load job.
	 *
	 * @param upcs          The upcs to set as 'upcs' parameter.
	 * @param stores        The stores to set as 'stores' parameter.
	 * @param transactionId The transaction id to set as the 'transactionId' parameter.
	 * @param userIp        IP of the user creating this request
	 */
	private void buildScaleMaintenanceLoadByDepartmentJob(ScaleMaintenanceLoadParameters parameters, Long transactionId, String userIp) {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString(ScaleMaintenanceService.USER_ID_PARAMETER, this.userInfo.getUserId());
		jobParametersBuilder.addDate(ScaleMaintenanceService.START_TIME_PARAMETER, new Date());
		jobParametersBuilder.addLong(ScaleMaintenanceService.TRANSACTION_ID_PARAMETER, transactionId);
		jobParametersBuilder.addLong(ScaleMaintenanceService.STORES_PARAMETER, Long.valueOf(parameters.getStores()));
		jobParametersBuilder.addLong(ScaleMaintenanceService.TARGETSTORE_PARAMETER, Long.valueOf(parameters.getTargetStore()));
		jobParametersBuilder.addString(ScaleMaintenanceService.DEPTNBR_PARAMETER, parameters.getDepartmentNbr());
		jobParametersBuilder.addDate(ScaleMaintenanceService.EFFECTIVEDATE_PARAMETER, parameters.getEffectiveDate());
		jobParametersBuilder.addString(ScaleMaintenanceService.USER_IP_PARAMETER, userIp);
		this.logJobParameters(jobParametersBuilder.toJobParameters());

		this.runScaleMaintenanceLoadByDepartmentJob(jobParametersBuilder);
	}
	
	/**
	 * Runs the scale maintenance job asynchronously (so the user gets the transaction id as soon as possible).
	 *
	 * @param jobParametersBuilder Job parameters for this scale maintenance load.
	 */
	private void runScaleMaintenanceLoadByDepartmentJob(JobParametersBuilder jobParametersBuilder) {
		try {
			 ScaleMaintenanceService.logger.debug(String.format(JOB_LOCATOR_LOG_MESSAGE, LOAD_JOB_NAME));
			 Job job = this.jobLocator.getJob(LOAD_BY_DEPTNBR_JOB_NAME);
			 // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
			 this.jobLauncher.run(job, jobParametersBuilder.toJobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				JobParametersInvalidException  | NoSuchJobException e) {
			ScaleMaintenanceService.logger.error(e.getMessage());
			throw new JobExecutionException(String.format(ScaleMaintenanceService.JOB_EXECUTION_ERROR_MESSAGE,
					LOAD_JOB_NAME, e.getMessage()), e.getCause());
		}
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
}
