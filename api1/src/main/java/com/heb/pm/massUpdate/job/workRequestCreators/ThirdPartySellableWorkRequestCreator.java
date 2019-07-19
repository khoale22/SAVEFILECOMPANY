package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateDistributionFilter;
import com.heb.pm.entity.CandidateDistributionFilterKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.SourceSystem;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Creates work reqeusts that set the third party sellable attribute.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class ThirdPartySellableWorkRequestCreator implements WorkRequestCreator {

	/**
	 * Creates work requests that set third party sellable.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
												  int sourceSystem) {

		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);
		candidateWorkRequest.setCandidateDistributionFilters(new ArrayList<>());

		CandidateDistributionFilterKey distributionFilterKey = new CandidateDistributionFilterKey();
		distributionFilterKey.setAttributeId(productId.toString());
		distributionFilterKey.setTargetSystemId(Long.valueOf(SourceSystem.SOURCE_SYSTEM_INSTACART));
		distributionFilterKey.setKeyType(CandidateDistributionFilterKey.PRODUCT_ATTRIBUTE_TYPE);

		CandidateDistributionFilter filter = new CandidateDistributionFilter();
		filter.setKey(distributionFilterKey);
		filter.setAction(
				parameters.getBooleanValue() ?	CandidateDistributionFilter.ADD : CandidateDistributionFilter.DELETE);
		filter.setWorkRequest(candidateWorkRequest);

		candidateWorkRequest.getCandidateDistributionFilters().add(filter);

		return candidateWorkRequest;
	}
}
