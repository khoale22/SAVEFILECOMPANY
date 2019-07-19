package com.heb.pm.massUpdate.job;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import com.heb.pm.massUpdate.job.workRequestCreators.WorkRequestCreator;
import com.heb.pm.massUpdate.job.workRequestCreators.WorkRequestCreatorFactory;
import com.heb.xmlns.ei.product.PRODUCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * The processor part of the mass update job. It will convert a product ID to a candidate work request.
 *
 * @author d116773
 * @since 2.12.0
 */
public class ProductToCandidateWorkRequest implements ItemProcessor<Long, CandidateWorkRequest>, StepExecutionListener{

	private static final Logger logger = LoggerFactory.getLogger(ProductToCandidateWorkRequest.class);

	private static final String UPDATE_PARAMETERS_NOT_SET_ERROR = "Mass update parameters not set for transaction ID %d";
	private static final String PRODUCT_LOG_MESSAGE = "Processing Product ID %d";

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("${app.sourceSystemId}")
	private int sourceSystemId;

	private MassUpdateParameters parameters;

	@Autowired
	private MassUpdateParameterMap parameterMap;

	@Autowired
	private WorkRequestCreatorFactory factory;

	private WorkRequestCreator creator;

	/**
	 * Called by the SpringBatch framework. It takes a product ID and creates a CandidateWorkRequest.
	 *
	 * @param item The product ID to create a work request for.
	 * @return A candidate work request for the product.
	 * @throws Exception
	 */
	@Override
	public CandidateWorkRequest process(Long item) throws Exception {

		ProductToCandidateWorkRequest.logger.info (
				String.format(ProductToCandidateWorkRequest.PRODUCT_LOG_MESSAGE, item));

		return this.creator.createWorkRequest(item, this.transactionId, this.parameters, this.sourceSystemId);
	}

	/**
	 * Called by Spring Batch before the step starts.
	 *
	 * @param stepExecution The context the step is executing in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {

		// Pull the parameters out of the map.
		this.parameters = this.parameterMap.get(this.transactionId);
		if (this.parameters == null) {
			String errorMessage = String.format(ProductToCandidateWorkRequest.UPDATE_PARAMETERS_NOT_SET_ERROR,
					this.transactionId);
			ProductToCandidateWorkRequest.logger.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		// Get whatever object creates the CandidateWorkRequests for the type of update the user requested.
		this.creator = this.factory.getCreator(this.parameters.getAttribute());
		this.creator.init();
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
