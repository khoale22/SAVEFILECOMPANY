package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by eBM.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class EbmPredicateBuilder implements PredicateBuilder {

	/**
	 * Constructs a subquery that joins product_master to pd_class_commodity and constrains on eBM.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that joins product_master to pd_class_commodity and constrains on eBM.
	 */
	@Override
	public ExistsClause<ClassCommodity> buildPredicate(CriteriaBuilder criteriaBuilder,
										  Root<? extends ProductMaster> pmRoot,
										  CriteriaQuery<? extends ProductMaster> queryBuilder,
										  ProductSearchCriteria productSearchCriteria,
										  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for EBM
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry ebmEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.EBM) {
				ebmEntry = c;
			}
		}
		if (ebmEntry == null || ebmEntry.getStringComparator() == null) {
			return null;
		}

		// Constrains on commodity
		Subquery<ClassCommodity> productExists = queryBuilder.subquery(ClassCommodity.class);
		Root<ClassCommodity> ccRoot = productExists.from(ClassCommodity.class);
		productExists.select(ccRoot.get("key").get("classCode"));

		Predicate[] criteria = new Predicate[3];

		// Join product master to class commodity
		criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.classCode),
				ccRoot.get(ClassCommodity_.key).get(ClassCommodityKey_.classCode));
		criteria[1] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.commodityCode),
				ccRoot.get(ClassCommodity_.key).get(ClassCommodityKey_.commodityCode));

		// Constrain on ebm
		criteria[2] = criteriaBuilder.equal(criteriaBuilder.trim(ccRoot.get(ClassCommodity_.eBMid)),
				ebmEntry.getStringComparator());

		productExists.where(criteria);

		return new ExistsClause<>(productExists);
	}
}
