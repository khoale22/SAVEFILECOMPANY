package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles searching by retail link.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class RetailLinkPredicateBuilder implements PredicateBuilder  {

	/**
	 * Builds a where clause that constrains product master on pd_link_cd.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param productSearchCriteria The user's search criteria.
	 * @return A list of predicates that will constrain product master.
	 */
	@Override
	public List<Predicate> buildBasicWhereClause(CriteriaBuilder criteriaBuilder,
											 Root<? extends ProductMaster> pmRoot,
											 ProductSearchCriteria productSearchCriteria) {

		// See if there is an entry in the custom searches for item add date
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry productAddEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.RETAIL_LINK) {
				productAddEntry = c;
				break;
			}
		}
		if (productAddEntry == null) {
			return null;
		}

		List<Predicate> whereClause = new LinkedList<>();

		whereClause.add(criteriaBuilder.equal(pmRoot.get(ProductMaster_.retailLink),
				productAddEntry.getIntegerComparator()));
		return  whereClause;
	}
}
