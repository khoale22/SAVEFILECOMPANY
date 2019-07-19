package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.SearchCriteria;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.ProductSearchService;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Interface for classes that handle a specific type of search (by product ID, by item code, etc.). Each type of search
 * must implement this interface.
 *
 * Any class you build that extends this interface should be added to PredicateBuilderList so it is available to
 * the main search classes.
 *
 * @author d116773
 * @since 2.13.0
 */
public interface PredicateBuilder {

	/**
	 * This method will populate the temp table with the types of value the PredicateBuilder is designed to handle.
	 * For example, if this PredicateBuilder is to handle searching by product ID, then this function will insert
	 * the product IDs from the ProductSearchCriteria and only those types of values. There is a default implementation
	 * that does nothing.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	default void populateTempTable(ProductSearchCriteria productSearchCriteria) {}

	/**
	 * This method will generate a list of predicates that constrain directly on product master without
	 * joining to something else. There is a default implementation that does nothing as this is not a common case.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param productSearchCriteria The user's search criteria.
	 * @return A list of predicates that will constrain product master.
	 */
	default List<Predicate> buildBasicWhereClause(CriteriaBuilder criteriaBuilder,
											Root<? extends ProductMaster> pmRoot,
											ProductSearchCriteria productSearchCriteria) {return null;}

	/**
	 * This method will generate the sub-query that will be added to the overall query. The intention is that it builds
	 * a query that can be added as an EXISTS clause in the main WHERE clause. There is a default implementation
	 * that does nothing.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A sub-query that can be added as an EXISTS clause to the main WHERE clause.
	 */
	default ExistsClause<?> buildPredicate(CriteriaBuilder criteriaBuilder,
													  Root<? extends ProductMaster> pmRoot,
													  CriteriaQuery<? extends ProductMaster> queryBuilder,
													  ProductSearchCriteria productSearchCriteria,
							   						  ParameterExpression<String> sessionIdBindVariable) {return null;}
}
