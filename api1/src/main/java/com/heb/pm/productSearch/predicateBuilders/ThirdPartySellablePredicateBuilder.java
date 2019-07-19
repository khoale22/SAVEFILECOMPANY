package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching for third-party sellable product.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ThirdPartySellablePredicateBuilder implements PredicateBuilder {

	/**
	 * Constructs a subquery that joins product_master to dstrb_fltr. It returns either an exists or not exists based on
	 * what status the user is searching for.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that joins product_master to dstrb_fltr. It returns either an exists or not exists based on
	 * what status the user is searching for.
	 */
	@Override
	public ExistsClause<?> buildPredicate(CriteriaBuilder criteriaBuilder,
									  Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for third party sellable.
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry thridPartySellableEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.THIRD_PARTY_SELLABLE) {
				thridPartySellableEntry = c;
			}
		}
		if (thridPartySellableEntry == null || thridPartySellableEntry.getBooleanComparator() == null) {
			return null;
		}

		Subquery<DistributionFilter> productExist = queryBuilder.subquery(DistributionFilter.class);
		Root<DistributionFilter> dfRoot = productExist.from(DistributionFilter.class);
		productExist.select(dfRoot.get("key").get("keyId"));

		Predicate[] criteria = new Predicate[3];

		// Join product master to Distribution Filter
		criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				dfRoot.get(DistributionFilter_.key).get(DistributionFilterKey_.keyId));
		// Constrain on type product
		criteria[1] = criteriaBuilder.equal(dfRoot.get(DistributionFilter_.key).get(DistributionFilterKey_.keyType),
				DistributionFilterKey.PRODUCT_KEY_TYPE);
		// Constrain on instacart
		criteria[2] = criteriaBuilder.equal(dfRoot.get(DistributionFilter_.key).get(DistributionFilterKey_.targetSystemId),
				SourceSystem.SOURCE_SYSTEM_INSTACART);

		productExist.where(criteria);

		return new ExistsClause<>(thridPartySellableEntry.getBooleanComparator(), productExist);
	}
}
