package com.heb.pm.massUpdate;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.entity.PDPTemplate;
import com.heb.pm.entity.SalesChannel;
import com.heb.pm.entity.TransactionTracker;
import com.heb.pm.massUpdate.job.JobExecutionException;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
import com.heb.pm.massUpdate.job.MassUpdateParameterMap;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.repository.FulfillmentChannelRepository;
import com.heb.pm.repository.PDPTemplateRepository;
import com.heb.pm.repository.SalesChannelRepository;
import com.heb.pm.repository.TransactionTrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Holds the business logic to start a mass update job.
 *
 * @author d116773
 * @since 2.12.0
 */
@Service
public class MassUpdateService {

	private static final Logger logger = LoggerFactory.getLogger(MassUpdateService.class);

	private static final String JOB_EXECUTION_ERROR_MESSAGE = "Unable to execute job %s: %s";
	private static final String TRANSACTION_ID_PARAMETER = "transactionId";

	@Value("${app.massUpdateJobName}")
	private String massUpdateJobName;

	@Value("${app.sourceSystemId}")
	private int sourceSystemId;

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private JobLocator jobLocator;

	@Autowired
	private MassUpdateParameterMap massUpdateParameterMap;

	@Autowired
	private MassUpdateProductMap massUpdateProductMap;

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;

	@Autowired
	private SalesChannelRepository salesChannelRepository;

	@Autowired
	private PDPTemplateRepository pdpTemplateRepository;

	@Autowired
	private FulfillmentChannelRepository fulfillmentChannelRepository;

	/**
	 * Submits a job to process a mass update request.
	 *
	 * @param request The products to update and the parameters of what to update.
	 * @return The transaction ID of the mass update.
	 */
	public Long submitMassUpdate(MassUpdateRequest request) {

		Job job = this.getMassUpdateJob();

		// Add a record to trx_tracking to group all the updates together.
		TransactionTracker transaction = this.getTransaction(request.getParameters().getUserId(),
				request.getParameters().getAttribute(), request.getParameters().getDescription());
		MassUpdateService.logger.debug(transaction.toString());

		// Add the products and parameters to the map so the job can get them later.
		this.massUpdateParameterMap.add(transaction.getTrackingId(), request.getParameters());
		this.massUpdateProductMap.add(transaction.getTrackingId(),
				request.getProductSearchCriteria().toProductSearchCriteria());

		// The only parameter is the transaction ID.
		JobParametersBuilder parametersBuilder = new JobParametersBuilder();
		parametersBuilder.addLong(MassUpdateService.TRANSACTION_ID_PARAMETER, transaction.getTrackingId());

		try {
			// Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
			this.jobLauncher.run(job, parametersBuilder.toJobParameters());

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				JobParametersInvalidException e) {

			MassUpdateService.logger.error(e.getMessage());
			throw new JobExecutionException(String.format(MassUpdateService.JOB_EXECUTION_ERROR_MESSAGE,
					this.massUpdateJobName, e.getMessage()), e.getCause());
		}

		return transaction.getTrackingId();
	}

	/**
	 * Saves an entry into the tracking table. This will be used to group all of the mass updates together as one unit.
	 *
	 * @param userId The ID of the user creating the mass update.
	 * @param attribute The attribute the user is updating.
	 * @param description The user's description of the update.
	 * @return @return The object saved to the tracking table.
	 */
	@CoreTransactional
	protected TransactionTracker getTransaction(String userId, MassUpdateParameters.Attribute attribute,
											  String description) {

		TransactionTracker t = new TransactionTracker();
		t.setUserId(userId);
		t.setCreateDate(LocalDateTime.now());
		t.setSource(Integer.toString(this.sourceSystemId));
		t.setUserRole(TransactionTracker.USER_ROLE_CODE);
		t.setTrxStatCd(TransactionTracker.STAT_CODE_NOT_COMPLETE);
		t.setFileNm(attribute.getAttributeName());
		t.setFileDes(description);

		return this.transactionTrackingRepository.save(t);
	}

	/**
	 * Returns a reference to the mass update job.
	 *
	 * @return A reference to the mass update job.
	 */
	private Job getMassUpdateJob() {

		try {

			MassUpdateService.logger.debug(String.format("Looking for job %s", this.massUpdateJobName));
			return this.jobLocator.getJob(this.massUpdateJobName);

		} catch (NoSuchJobException e) {
			JobNotDefinedException je = new JobNotDefinedException(this.massUpdateJobName);
			MassUpdateService.logger.error(je.getMessage());
			throw je;
		}
	}

	/**
	 * This method returns all of the possible sale channels
	 * @return
	 */
	public List<SalesChannel> getAllSalesChannels() {
		return this.salesChannelRepository.findAll();
	}

	/**
	 * This method returns all of the possible pdp templates
	 * @return
	 */
	public List<PDPTemplate> getAllPDPTemplates() {
		return this.pdpTemplateRepository.findAll();
	}

	/**
	 * This method returns all of the possible sale channels the descriptions have excessive
	 * white space at the end so it is trimmed
	 * @return
	 */
	public List<FulfillmentChannel> getAllFulfillmentPrograms() {
		List<FulfillmentChannel> originalList=this.fulfillmentChannelRepository.findAll();
		for (FulfillmentChannel fulfillmentChannel: originalList) {
			fulfillmentChannel.setDescription(fulfillmentChannel.getDescription().trim());
		}
		return originalList;
	}

}
