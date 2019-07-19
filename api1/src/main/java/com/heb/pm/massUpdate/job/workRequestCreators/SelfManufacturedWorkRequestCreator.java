package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateProductMaster;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Creates CandidateWorkRequests to handle updates to the self manufactured flag.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class SelfManufacturedWorkRequestCreator implements WorkRequestCreator {

	/**
	 * Creates the work request to update self-man flag.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateUpdateRequst with the necessary attributes to update the self-man flag.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters, int sourceSystem) {

		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());
		candidateWorkRequest.getCandidateProductMaster().get(0).setSelfManufactured(
				parameters.getBooleanValue().equals(Boolean.TRUE) ? "Y" : "N");

		return candidateWorkRequest;
	}
}
