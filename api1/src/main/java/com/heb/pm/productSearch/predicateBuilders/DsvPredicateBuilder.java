package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Adds predicates for DSV.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class DsvPredicateBuilder implements PredicateBuilder{

	/**
	 * Returns a subquery to go against prod_item and item_master constrain on DSV products.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that will constrain on the DSV flag.
	 */
	@Override
	public ExistsClause<ProdItem> buildPredicate(CriteriaBuilder criteriaBuilder,
										  Root<? extends ProductMaster> pmRoot,
										  CriteriaQuery<? extends ProductMaster> queryBuilder,
										  ProductSearchCriteria productSearchCriteria,
										  ParameterExpression<String> sessionIdBindVariable) {
		// See if there is an entry in the custom searches for item add date
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry dsvEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.DSV) {
				dsvEntry = c;
				break;
			}
		}
		if (dsvEntry == null) {
			return null;
		}

		// We'l be adding sub-query to prod_item
		Subquery<ProdItem> productExists = queryBuilder.subquery(ProdItem.class);
		Root<ProdItem> piRoot = productExists.from(ProdItem.class);
		productExists.select(piRoot.get("itemCode"));

		// Join prod_item to item_master
		Join<ProdItem, ItemMaster> piJoinIm = piRoot.join(ProdItem_.itemMaster);

		Predicate[] criteria = new Predicate[2];

		// Add a join from product_master to prod_item.
		criteria[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), piRoot.get(ProdItem_.productId));

		// Add the constraint for DSV
		if (dsvEntry.getBooleanComparator()) {
			criteria[1] = criteriaBuilder.equal(piJoinIm.get(ItemMaster_.dsv), "Y    ");
		} else {
			// since there's a bunch with blank spaces that mean false, do not equal to yes
			criteria[1] = criteriaBuilder.notEqual(piJoinIm.get(ItemMaster_.dsv), "Y    ");
		}

		productExists.where(criteria);

		return new ExistsClause<>(productExists);
	}
}
