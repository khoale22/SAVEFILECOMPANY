package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.GoodsProduct;
import com.heb.pm.entity.GoodsProduct_;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles searching by qualifying condition.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class QualifyingConditionPredicateBuilder implements PredicateBuilder {

	/**
	 * Returns a constraint on product_master on Vertex qualifying condition.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param productSearchCriteria The user's search criteria.
	 * @return A constraint on product_master on Vertex qualifying condition.
	 */
	@Override
	public List<Predicate> buildBasicWhereClause(CriteriaBuilder criteriaBuilder,
												  Root<? extends ProductMaster> pmRoot,
												  ProductSearchCriteria productSearchCriteria) {

		// See if there is an entry in the custom searches for qualifying condition
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry qualifyingConditionEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.TAX_QUALIFYING_CONDITION) {
				qualifyingConditionEntry = c;
			}
		}
		if (qualifyingConditionEntry == null || qualifyingConditionEntry.getTaxCategory() == null) {
			return null;
		}

		List<Predicate> whereClause = new LinkedList<>();
		whereClause.add(criteriaBuilder.equal(pmRoot.get(ProductMaster_.taxQualifyingCode),
				qualifyingConditionEntry.getTaxCategory().getDvrCode()));

		return whereClause;
	}
}
