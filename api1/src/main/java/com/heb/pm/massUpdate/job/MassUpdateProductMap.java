package com.heb.pm.massUpdate.job;

import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the list of products the user asked to update.
 *
 * @author d116773
 * @since 2.12.0
 */
@Service
public class MassUpdateProductMap {

	private Map<Long, ProductSearchCriteria> productSearchMap;

	/**
	 * Constructs a new MassUpdateProductMap.
	 */
	public MassUpdateProductMap() {
		this.productSearchMap = new HashMap<>();
	}

	/**
	 * Adds a list of products to the map.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @param productSearchCriteria The user's search criteria.
	 */
	public void add(Long transactionId, ProductSearchCriteria productSearchCriteria) {
		this.productSearchMap.put(transactionId, productSearchCriteria);
	}

	/**
	 * Returns a list of products for the job. This is a destructive read and the list will no longer be available
	 * after this call.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @return The list of products for the job to update.
	 */
	public ProductSearchCriteria get(Long transactionId) {
		return this.productSearchMap.remove(transactionId);
	}
}
