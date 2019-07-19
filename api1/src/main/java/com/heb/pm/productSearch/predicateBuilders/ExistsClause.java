package com.heb.pm.productSearch.predicateBuilders;

import javax.persistence.criteria.Subquery;

/**
 * Provides a wrapper around a sub-query for the predicate builders to returns. Allows the predicate builders to
 * say whether or not the subquery should be treated as an exists or a not exists when building the product search query.
 *
 * @author d116773
 * @since 2.14.0
 */
public class ExistsClause<T> {

	private Subquery<T> subQuery;
	private boolean exists;

	// Hide the default constructor.
	private ExistsClause() {}

	/**
	 * Helper constructor for when the query should be treated as exists.
	 *
	 * @param subQuery The subquery for the query builder to add.
	 */
	public ExistsClause(Subquery<T> subQuery) {
		this(true, subQuery);
	}

	/**
	 * Constructs a new ExistsClause.
	 *
	 * @param isExists True if the query builder should treat the subquery as an exists and false if it should treat
	 *                 the query as a not exists.
	 * @param subQuery The subquery for the query builder to add.
	 */
	public ExistsClause(boolean isExists, Subquery<T> subQuery) {
		this.exists = isExists;
		this.subQuery = subQuery;
	}

	/**
	 * Returns the subquery to add to the product search query.
	 *
	 * @return The subquery to add to the product search query.
	 */
	public Subquery<T> getSubQuery() {
		return subQuery;
	}

	/**
	 * Whether or not the query builder should treat this as an exists or not exists clause.
	 *
	 * @return True if the query builder should treat the subquery as an exists and false if it should treat the query
	 * as a not exists.
	 */
	public boolean isExists() {
		return exists;
	}
}
