package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

/**
 * Handles mass updates of tag type.
 *
 * @author d116773
 * @since 2.17.0
 */
@Service
public class TagTypeWorkRequestCreator implements WorkRequestCreator {

	/**
	 * Creates the work request to update tag type.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateUpdateRequest with the necessary attributes to update tag type.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters, int sourceSystem) {

		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());
		candidateWorkRequest.getCandidateProductMaster().get(0).setTagType(parameters.getStringValue());

		return candidateWorkRequest;
	}
}
