package com.heb.pm.massUpdate;

import com.heb.pm.productSearch.RawSearchCriteria;

/**
 * The request the front end will send to initiate a mass update.
 *
 * @author d116773
 * @since 2.12.0
 */
public class MassUpdateRequest {

	private RawSearchCriteria productSearchCriteria;
	private MassUpdateParameters parameters;

	/**
	 * Returns the criteria the user used to search for products.
	 *
	 * @return The criteria the user used to search for products.
	 */
	public RawSearchCriteria getProductSearchCriteria() {
		return productSearchCriteria;
	}

	/**
	 * Sets the criteria the user used to search for products.
	 *
	 * @param productSearchCriteria The criteria the user used to search for products.
	 */
	public void setProductSearchCriteria(RawSearchCriteria productSearchCriteria) {
		this.productSearchCriteria = productSearchCriteria;
	}

	/**
	 * Returns the parameters to update all of the products to.
	 *
	 * @return The parameters to update all of the products to.
	 */
	public MassUpdateParameters getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters to update all of the products to.
	 *
	 * @param parameters The parameters to update all of the products to.
	 */
	public void setParameters(MassUpdateParameters parameters) {
		this.parameters = parameters;
	}
}
