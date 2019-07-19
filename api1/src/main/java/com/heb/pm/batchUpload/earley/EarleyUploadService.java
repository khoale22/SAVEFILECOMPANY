package com.heb.pm.batchUpload.earley;

import com.heb.pm.CoreTransactional;
import com.heb.pm.batchUpload.BatchUploadParameterMap;
import com.heb.pm.batchUpload.BatchUploadRequest;
import com.heb.pm.batchUpload.JobExecutionException;
import com.heb.pm.batchUpload.earley.attribute.EarleyAttributeParser;
import com.heb.pm.batchUpload.earley.attributeValues.EarleyAttributeValuesParser;
import com.heb.pm.batchUpload.earley.hierarchy.EarleyHierarchyParser;
import com.heb.pm.batchUpload.earley.productAttributeValues.EarleyProdudctAttributeValuesParser;
import com.heb.pm.batchUpload.earley.hierarchy.EarleyProductParser;
import com.heb.pm.entity.TransactionTracker;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Service class that contains methods to kick off jobs to process Earley uploads.
 *
 * @author d116773
 * @since 2.15.0
 */
@Service
public class EarleyUploadService {

	private static final Logger logger = LoggerFactory.getLogger(EarleyUploadService.class);

	@Value("${app.earley.HierarchyUploadJob}")
	private String earleyHierarchyUploadJob;

	@Value("${app.earley.ProductUploadJob}")
	private String earleyProductUploadJob;

	@Value("${app.earley.AttributeUploadJob}")
	private String earleyAttributeUploadJob;

	@Value("${app.earley.AttributeValuesUploadJob}")
	private String earleyAttributeValuesUploadJob;

	@Value("${app.earley.ProductAttributeValuesUploadJob}")
	private String earleyProductAttributeValuesUploadJob;

	@Value("${app.earley.sourceSystemId}")
	private int sourceSystemId;

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private JobLocator jobLocator;

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;

	@Autowired
	private BatchUploadParameterMap parameterMap;

	private EarleyProductParser earleyProductParser = new EarleyProductParser();

	private EarleyAttributeParser earleyAttributeParser = new EarleyAttributeParser();

	private EarleyAttributeValuesParser earleyAttributeValuesParser = new EarleyAttributeValuesParser();

	private EarleyProdudctAttributeValuesParser earleyProdudctAttributeValuesParser =
			new EarleyProdudctAttributeValuesParser();

	private EarleyHierarchyParser earleyHierarchyParser = new EarleyHierarchyParser();

	/**
	 * Handles the upload of an Earley hierarchy file.
	 *
	 * @param userId The ID of the user who loaded the file.
	 * @param file The file the user uploaded.
	 * @param description The description of the upload of the file to be stored in the tracking tables.
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitHierarchyUpload(String userId, MultipartFile file, String description)
			throws IOException, FileProcessingException {

		// Validate the file
		this.earleyHierarchyParser.validateHeaderRecord(file);

		TransactionTracker tt = this.getTransaction(userId, file.getName(), description);
		JobParametersBuilder jobParametersBuilder = this.getJobParameters(tt.getTrackingId(), userId, file);

		this.launchUploadJob(this.earleyHierarchyUploadJob, jobParametersBuilder);

		return tt.getTrackingId();
	}

	/**
	 * Handles the upload of an Earley attribute file.
	 *
	 * @param userId The ID of the user who loaded the file.
	 * @param file The file the user uploaded.
	 * @param description The description of the upload of the file to be stored in the tracking tables.
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitAttributeUpload(String userId, MultipartFile file, String description)
			throws IOException, FileProcessingException {

		// Validate the file
		this.earleyAttributeParser.validateHeaderRecord(file);

		TransactionTracker tt = this.getTransaction(userId, file.getName(), description);
		JobParametersBuilder jobParametersBuilder = this.getJobParameters(tt.getTrackingId(), userId, file);

		this.launchUploadJob(this.earleyAttributeUploadJob, jobParametersBuilder);

		return tt.getTrackingId();
	}

	/**
	 * Handles the upload of an Earley attribut values file.
	 *
	 * @param userId The ID of the user who loaded the file.
	 * @param file The file the user uploaded.
	 * @param description The description of the upload of the file to be stored in the tracking tables.
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitAttributeValuesUpload(String userId, MultipartFile file, String description)
			throws IOException, FileProcessingException {

		// Validate the file
		this.earleyAttributeValuesParser.validateHeaderRecord(file);

		TransactionTracker tt = this.getTransaction(userId, file.getName(), description);
		JobParametersBuilder jobParametersBuilder = this.getJobParameters(tt.getTrackingId(), userId, file);

		this.launchUploadJob(this.earleyAttributeValuesUploadJob, jobParametersBuilder);

		return tt.getTrackingId();
	}

	/**
	 * Handles the upload of an Earley product attribute values file.
	 *
	 * @param userId The ID of the user who loaded the file.
	 * @param file The file the user uploaded.
	 * @param description The description of the upload of the file to be stored in the tracking tables.
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitProductAttributeValuesUpload(String userId, MultipartFile file, String description)
			throws IOException, FileProcessingException {

		// Validate the file
		this.earleyProdudctAttributeValuesParser.validateHeaderRecord(file);

		TransactionTracker tt = this.getTransaction(userId, file.getName(), description);
		JobParametersBuilder jobParametersBuilder = this.getJobParameters(tt.getTrackingId(), userId, file);

		this.launchUploadJob(this.earleyProductAttributeValuesUploadJob, jobParametersBuilder);

		return tt.getTrackingId();
	}

	/**
	 * Handles the upload of an Earley product file.
	 *
	 * @param userId The ID of the user who loaded the file.
	 * @param file The file the user uploaded.
	 * @param description The description of the upload of the file to be stored in the tracking tables.
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitProductUpload(String userId, MultipartFile file, String description)
			throws IOException, FileProcessingException {

		// Validate the file before processing.
		this.earleyProductParser.validateHeaderRecord(file);

		TransactionTracker tt = this.getTransaction(userId, file.getName(), description);
		JobParametersBuilder jobParametersBuilder = this.getJobParameters(tt.getTrackingId(), userId, file);

		this.launchUploadJob(this.earleyProductUploadJob, jobParametersBuilder);

		return tt.getTrackingId();
	}

	/**
	 * Kicks off the  job to process the upload.
	 *
	 * @param jobName The name of the job to start.
	 * @param parametersBuilder The builder used to construct parameters to send to the job.
	 * @throws IOException
	 */
	private void launchUploadJob(String jobName, JobParametersBuilder parametersBuilder) {
		try {
			this.jobLauncher.run(this.getJob(jobName), parametersBuilder.toJobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |  JobParametersInvalidException e) {
			EarleyUploadService.logger.info(e.getLocalizedMessage());
			throw new JobExecutionException(e.getMessage(), e.getCause());
		}
	}

	private JobParametersBuilder getJobParameters(long trackingId, String userId, MultipartFile file)
			throws IOException {

		// Save the user's upload into the parameters map.
		BatchUploadRequest batchUploadRequest = new BatchUploadRequest();
		batchUploadRequest.setDataAsStream(file.getInputStream());
		batchUploadRequest.setUserId(userId);
		this.parameterMap.add(trackingId, batchUploadRequest);

		// Need to pass the transaction ID to the job.
		JobParametersBuilder parametersBuilder = new JobParametersBuilder();
		parametersBuilder.addLong("transactionId", trackingId);

		// Add the user ID to the job parameters
		parametersBuilder.addString("userId", userId);

		return parametersBuilder;
	}

	/**
	 * Constructs and saves a batch upload transaction.
	 *
	 * @param userId The ID of the user who uploaded the file.
	 * @param fileName The name of the file the user uploaded.
	 * @param description A description of the job.
	 * @return The TransactionTracker saved to the DB.
	 */
	@CoreTransactional
	protected TransactionTracker getTransaction(String userId, String fileName,
											  String description) {

		TransactionTracker t = new TransactionTracker();
		t.setUserId(userId);
		t.setCreateDate(LocalDateTime.now());
		t.setSource(Integer.toString(this.sourceSystemId));
		t.setUserRole(TransactionTracker.USER_ROLE_CODE);
		t.setTrxStatCd(TransactionTracker.STAT_CODE_NOT_COMPLETE);
		t.setFileNm(fileName);
		t.setFileDes(description);

		return this.transactionTrackingRepository.save(t);
	}

	/**
	 * Returns a reference to the requested batch job.
	 *
	 * @param jobName The name of the job to look for.
	 * @return A reference to the requested batch job.
	 */
	private Job getJob(String jobName) {

		try {
			EarleyUploadService.logger.debug(String.format("Looking for job %s", jobName));
			return this.jobLocator.getJob(jobName);

		} catch (NoSuchJobException e) {
			JobNotDefinedException je = new JobNotDefinedException(jobName);
			EarleyUploadService.logger.error(je.getMessage());
			throw je;
		}
	}
}
