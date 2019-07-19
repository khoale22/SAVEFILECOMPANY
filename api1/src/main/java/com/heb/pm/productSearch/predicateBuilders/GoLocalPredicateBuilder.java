package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching against prod_mkt_clm for all go local products.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class GoLocalPredicateBuilder extends MarketingClaimPredicateBuilder implements PredicateBuilder{


	/**
	 * Builds a predicate to query against prod_mkt_clm for go local products.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery to query against prod_mkt_clm
	 */
	@Override
	public ExistsClause<ProductMarketingClaim> buildPredicate(CriteriaBuilder criteriaBuilder,
									  Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		return this.buildProductMarketingClaimPredicate(CustomSearchEntry.GO_LOCAL,
				criteriaBuilder,
				pmRoot,
				queryBuilder,
				productSearchCriteria);
	}
}
