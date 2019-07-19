package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by publish status.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class PublishStatusPredicateBuilder implements PredicateBuilder {

	/**
	 * Constructs a subquery joining product_master to pblctn_aud. It returns either an exists or not exists based on
	 * what status the user is searching for.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery joining product_master to pblctn_aud.
	 */
	@Override
	public ExistsClause<PublicationAudit> buildPredicate(CriteriaBuilder criteriaBuilder,
										  Root<? extends ProductMaster> pmRoot,
										  CriteriaQuery<? extends ProductMaster> queryBuilder,
										  ProductSearchCriteria productSearchCriteria,
										  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for publication status
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry publishStatusEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.PUBLISH_STATUS) {
				publishStatusEntry = c;
			}
		}
		if (publishStatusEntry == null || publishStatusEntry.getBooleanComparator() == null) {
			return null;
		}

		Subquery<PublicationAudit> productExist = queryBuilder.subquery(PublicationAudit.class);
		Root<PublicationAudit> paRoot = productExist.from(PublicationAudit.class);
		productExist.select(paRoot.get("key").get("itemProductId"));

		Predicate[] criteria = new Predicate[2];

		// Join product master to publication audit
		criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.itemProductId));

		// Constrain on type product
		criteria[1] = criteriaBuilder.equal(
				paRoot.get(PublicationAudit_.key).get(PublicationAuditKey_.itemProductKeyCode),
				PublicationAuditKey.PRODUCT_KEY_TYPE);


		productExist.where(criteria);

		return new ExistsClause<>(publishStatusEntry.getBooleanComparator(), productExist);
	}
}
