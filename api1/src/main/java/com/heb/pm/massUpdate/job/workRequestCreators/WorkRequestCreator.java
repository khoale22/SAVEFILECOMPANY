package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.massUpdate.MassUpdateParameters;

/**
 * Interface for a collection of classes that will construct CandidateWorkRequests. Each type
 * will handle a specific type of Mass Update function.
 *
 * @author d116773
 * @since 2.12.0
 */
public interface WorkRequestCreator {

	/**
	 * Performs any initialization the class needs. There is a default implementation that does nothing.
	 */
	default void init(){}

	/**
	 * Constructs CandidateWorkRequests. Each type of creator constructs work requests that have whatever
	 * is needed to be populated for their particular type.
	 *
	 * @param productId The product ID the request is for.
	 * @param transactionId The transaction ID being used to group all the requests together.
	 * @param parameters The parameters the user wants to set the different values to.
	 * @param sourceSystem The ID of this system.
	 * @return A CandidateWorkRequest that can be written to the PS_ tables.
	 */
	CandidateWorkRequest createWorkRequest(Long productId, Long transactionId, MassUpdateParameters parameters,
										   int sourceSystem);
}
