package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;

/**
 * Builds predicates to look for products that are currently designated as a primo pick.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ActivePrimoPickPredicateBuilder implements PredicateBuilder {

	/**
	 * Builds a predicate to search for products that are currently designated as a primo pick.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that will return products that are currently designated as a primo pick.
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
			if (c.getType() == CustomSearchEntry.PRIMO_PICK) {
				primoPickEntry = c;
			}
		}
		if (primoPickEntry == null) {
			return null;
		}

		Subquery<ProductMarketingClaim> marketingClaimExists = queryBuilder.subquery(ProductMarketingClaim.class);
		Root<ProductMarketingClaim> pmcRoot = marketingClaimExists.from(ProductMarketingClaim.class);
		marketingClaimExists.select(pmcRoot.get("key").get("prodId"));

		Predicate[] criteria = new Predicate[5];
		// Join prod market claim back to product master
		criteria[0] = criteriaBuilder.equal(
				pmRoot.get(ProductMaster_.prodId),
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.prodId));
		// Constrain on the primo pick attribute
		criteria[1] = criteriaBuilder.equal(
				pmcRoot.get(ProductMarketingClaim_.key).get(ProductMarketingClaimKey_.marketingClaimCode),
				MarketingClaim.Codes.PRIMO_PICK.getCode());
		// Make sure it is active
		criteria[2] = criteriaBuilder.equal(
				pmcRoot.get(ProductMarketingClaim_.marketingClaimStatusCode),
				ProductMarketingClaim.APPROVED);
		// Make sure it is not expired
		criteria[3] = criteriaBuilder.lessThan(
				pmcRoot.get(ProductMarketingClaim_.promoPickEffectiveDate),
				LocalDate.now());
		criteria[4] = criteriaBuilder.greaterThan(
				pmcRoot.get(ProductMarketingClaim_.promoPickExpirationDate),
				LocalDate.now());

		marketingClaimExists.where(criteria);

		return new ExistsClause<>(marketingClaimExists);
	}
}
