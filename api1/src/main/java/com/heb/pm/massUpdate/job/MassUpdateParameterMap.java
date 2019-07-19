package com.heb.pm.massUpdate.job;

import com.heb.pm.massUpdate.MassUpdateParameters;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores parameters for the mass update job that cannot be passed through the standard mechanisms.
 *
 * @author d116773
 * @since 2.12.0
 */
@Service
public class MassUpdateParameterMap {

	private Map<Long, MassUpdateParameters> parametersMap;

	/**
	 * Constructs a new MassUpdateParameterMap.
	 */
	public MassUpdateParameterMap() {
		this.parametersMap = new HashMap<>();
	}

	/**
	 * Adds a new set of parameters to the map
	 *
	 * @param transactionId The transaction id for the parameters. This will be used as the key.
	 * @param parameters The parameters to add.
	 */
	public void add(Long transactionId, MassUpdateParameters parameters) {
		this.parametersMap.put(transactionId, parameters);
	}

	/**
	 * Returns the parameters for a given ID. This is a destructive read and they will no longer be available after
	 * being read.
	 *
	 * @param transactionId The ID of the parameters to look for.
	 * @return The parameters tied to transactionId. If none are found, it returns null.
	 */
	public MassUpdateParameters get(Long transactionId) {
		return this.parametersMap.remove(transactionId);
	}

}
