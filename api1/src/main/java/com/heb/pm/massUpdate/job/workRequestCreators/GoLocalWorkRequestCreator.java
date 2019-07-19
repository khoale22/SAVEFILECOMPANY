package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateProductMarketingClaim;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.MarketingClaim;
import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

/**
 * Creates CandidateWorkRequests that will set a product's Go Local status.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class GoLocalWorkRequestCreator implements WorkRequestCreator {

	/**
	 * Creates a CandidateWorkRequest set set Go Local either on or off. Set the boolean value
	 * of the update parameters to true to turn it on and to false to turn it off.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest set set select ingredients either on or off.
	 */
	@Override
	public CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
												  int sourceSystem) {

		// Make the work request.
		CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(productId,
				parameters.getUserId(), transactionId, sourceSystem);
		// Add the product master.
		WorkRequestCreatorUtils.addProductMaster(candidateWorkRequest, parameters.getUserId());
		// Add the marketing claim.
		String flag = parameters.getBooleanValue().equals(Boolean.TRUE) ?
				CandidateProductMarketingClaim.TURN_CODE_ON : CandidateProductMarketingClaim.TURN_CODE_OFF;
		WorkRequestCreatorUtils.addMarketingClaim(candidateWorkRequest.getCandidateProductMaster().get(0), null,
				flag, MarketingClaim.Codes.GO_LOCAL.getCode(), null, null);


		return candidateWorkRequest;
	}
}
