package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateProductMaster;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

/**
 * Handles mass update requests for tax category.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class TaxCategoryWorkRequestCreator implements WorkRequestCreator{
	/**
	 * Creates the work request to update tax category.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateUpdateRequest with the necessary attributes to update tax category.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters, int sourceSystem) {

		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);

		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());
		candidateWorkRequest.getCandidateProductMaster().get(0).setVertexTaxCategoryCode(parameters.getStringValue());

		return candidateWorkRequest;
	}
}
