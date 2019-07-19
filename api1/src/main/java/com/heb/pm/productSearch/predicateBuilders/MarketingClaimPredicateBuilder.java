package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;

import javax.persistence.criteria.*;

/**
 * Base class for predicate builders that query against prod_mkt_clm.
 *
 * @author d116773
 * @since 2.14.0
 */
public abstract class MarketingClaimPredicateBuilder {

	/**
	 * Builds a predicate to query against prod_mkt_clm.
	 *
	 * @param codeToCheck The type of marketing claim to check against.
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @return A subquery to query against prod_mkt_clm
	 */

	ExistsClause<ProductMarketingClaim> buildProductMarketingClaimPredicate(int codeToCheck,
									CriteriaBuilder criteriaBuilder,
									Root<? extends ProductMaster> pmRoot,
									CriteriaQuery<? extends ProductMaster> queryBuilder,
									ProductSearchCriteria productSearchCriteria) {

		// See if there are any entries for marketing claims.
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry entry = null;

		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == codeToCheck) {
				entry = c;
				break;
			}
		}
		if (entry == null) {
			return null;
		}

		Subquery<ProductMarketingClaim> marketingClaimExists = queryBuilder.subquery(ProductMarketingClaim.class);
		Root<ProductMarketingClaim> pmcRoot = marketingClaimExists.from(ProductMarketingClaim.class);
		marketingClaimExists.select(pmcRoot.get("key").get("prodId"));

		Predicate[] criteria = new Predicate[2];
		// Join prod market claim back to product master
		criteria[0] = criteriaBuilder.equal(
				pmRoot.get(ProductMaster_.prodId),
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.prodId));

		criteria[1] = criteriaBuilder.equal(
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.marketingClaimCode),
				this.getMarketingClaimCode(entry));

		marketingClaimExists.where(criteria);

		return new ExistsClause<>(marketingClaimExists);
	}

	/**
	 * Returns the mkt_clm_cd for a specific custom search entry.
	 *
	 * @param customSearchEntry The custom search entry to check.
	 * @return The mkt_clm_cd corresponding to that custom search entry.
	 */
	private String getMarketingClaimCode(CustomSearchEntry customSearchEntry) {

		String toReturn = null;

		switch (customSearchEntry.getType()) {
			case CustomSearchEntry.DISTINCTIVE:
				toReturn = MarketingClaim.Codes.DISTINCTIVE.getCode();
				break;
			case CustomSearchEntry.GO_LOCAL:
				toReturn =MarketingClaim.Codes.GO_LOCAL.getCode();
				break;
			case CustomSearchEntry.SELECT_INGREDIENTS:
				toReturn = MarketingClaim.Codes.SELECT_INGREDIENTS.getCode();
				break;
			case CustomSearchEntry.TOTALLY_TEXAS:
				toReturn = MarketingClaim.Codes.TOTALLY_TEXAS.getCode();
		}

		return toReturn;
	}
}
