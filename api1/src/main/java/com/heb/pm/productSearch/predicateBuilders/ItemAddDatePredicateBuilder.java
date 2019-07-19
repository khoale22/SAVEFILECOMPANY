package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Builds predicates to search based on item add date.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ItemAddDatePredicateBuilder implements PredicateBuilder {

	/**
	 * Builds a predicate to search for products based on item add date.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that will return products based on item add date.
	 */
	@Override
	public ExistsClause<ProdItem> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for item add date
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry itemAddEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.ITEM_ADD_DATE) {
				itemAddEntry = c;
				break;
			}
		}
		if (itemAddEntry == null || itemAddEntry.getDateComparator() == null) {
			return null;
		}
		if (itemAddEntry.getOperator() == CustomSearchEntry.BETWEEN) {
			if (itemAddEntry.getEndDateComparator() == null) {
				return null;
			}
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

		// Add the constraint for add date
		switch (itemAddEntry.getOperator()) {
			case CustomSearchEntry.EQUAL:
				criteria[1] = criteriaBuilder.equal(piJoinIm.get(ItemMaster_.addedDate),
						itemAddEntry.getDateComparator());
				break;
			case CustomSearchEntry.GREATER_THAN:
				criteria[1] = criteriaBuilder.greaterThanOrEqualTo(piJoinIm.get(ItemMaster_.addedDate),
						itemAddEntry.getDateComparator());
				break;
			case CustomSearchEntry.LESS_THAN:
				criteria[1] = criteriaBuilder.lessThanOrEqualTo(piJoinIm.get(ItemMaster_.addedDate),
						itemAddEntry.getDateComparator());
				break;
			case CustomSearchEntry.ONE_WEEK:
				LocalDate today = LocalDate.now();
				LocalDate oneWeekAgo = today.minusDays(7);
				criteria[1] = criteriaBuilder.greaterThanOrEqualTo(piJoinIm.get(ItemMaster_.addedDate),
						oneWeekAgo);
				break;
			case CustomSearchEntry.BETWEEN:
				criteria[1] = criteriaBuilder.between(piJoinIm.get(ItemMaster_.addedDate),
						itemAddEntry.getDateComparator(), itemAddEntry.getEndDateComparator());
		}

		productExists.where(criteria);

		return new ExistsClause<>(productExists);
	}
}
