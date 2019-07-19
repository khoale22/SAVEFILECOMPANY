package com.heb.pm.productSearch.predicateBuilders;

import java.util.UUID;

/**
 * Some common functions all the predicate builders use. At first, I created an abstract class they could all extend,
 * but this caused problems with Spring.
 *
 * @author d116773
 * @since 2.13.0
 */
final class PredicateBuilderUtil {

	// Hide the constructor.
	private PredicateBuilderUtil() {}

	/**
	 * Returns a unique ID.
	 *
	 * @return a unique ID.
	 */
	public static String getUniqueId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
