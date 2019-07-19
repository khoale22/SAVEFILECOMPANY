package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Builds predicates base on primo pick status.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class PrimoPickStatusPredicateBuilder implements PredicateBuilder {

	/**
	 * Returns a predicate that will constrain on primo pick status.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that will constrain on primo pick status.
	 */
	@Override
	public ExistsClause<ProductMarketingClaim> buildPredicate(CriteriaBuilder criteriaBuilder,
									  Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for primo pick.
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry primoPickEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.PRIMO_PICK_STATUS) {
				primoPickEntry = c;
			}
		}
		if (primoPickEntry == null || primoPickEntry.getStringComparator() == null ||
				primoPickEntry.getStringComparator().isEmpty()) {
			return null;
		}

		Subquery<ProductMarketingClaim> marketingClaimExists = queryBuilder.subquery(ProductMarketingClaim.class);
		Root<ProductMarketingClaim> pmcRoot = marketingClaimExists.from(ProductMarketingClaim.class);
		marketingClaimExists.select(pmcRoot.get("key").get("prodId"));

		Predicate[] criteria = new Predicate[3];
		// Join prod market claim back to product master
		criteria[0] = criteriaBuilder.equal(
				pmRoot.get(ProductMaster_.prodId),
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.prodId));
		// Constrain on the primo pick attribute
		criteria[1] = criteriaBuilder.equal(
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.marketingClaimCode),
				MarketingClaim.Codes.PRIMO_PICK.getCode());
		// Match the status
		criteria[2] = criteriaBuilder.equal(
				pmcRoot.get(ProductMarketingClaim_.marketingClaimStatusCode),
				primoPickEntry.getStringComparator());

		marketingClaimExists.where(criteria);

		return new ExistsClause<>(marketingClaimExists);
	}
}
