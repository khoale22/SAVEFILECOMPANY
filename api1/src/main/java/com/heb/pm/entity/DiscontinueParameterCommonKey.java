package com.heb.pm.entity;

/**
 * Since JPA is very flaky when it comes to inheritance, I could not make the keys for product discontinue
 * exception rules extend product discontinue default rules. Since some functions need to operate on them
 * as though they are the same, the common functions have been moved to this interface.
 *
 * These two attributes together make a default rule unique. These two attributes combined with an ID common
 * across all the components of an exception make an exception rule unique.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface DiscontinueParameterCommonKey {

	/**
	 * Returns the ID of the default rule.
	 *
	 * @return The ID of the default rule.
	 */
	int getId();

	/**
	 * Returns the sequence number of the default rule.
	 *
	 * @return The sequence number of the default rule.
	 */
	int getSequenceNumber();
}
