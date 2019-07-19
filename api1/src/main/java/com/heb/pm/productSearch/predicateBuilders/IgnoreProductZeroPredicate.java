package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Predicate builder to exclude product ID zero (dummy for MRTs).
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class IgnoreProductZeroPredicate implements PredicateBuilder {

	/**
	 * Returns a constraint on product_master to exclude product ID 0 (dummy for MRTs).
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param productSearchCriteria The user's search criteria.
	 * @return A constraint on product_master to exclude product ID 0 (dummy for MRTs).
	 */
	@Override
	public List<Predicate> buildBasicWhereClause(CriteriaBuilder criteriaBuilder,
												 Root<? extends ProductMaster> pmRoot,
												 ProductSearchCriteria productSearchCriteria) {
		List<Predicate> whereClause = new LinkedList<>();
		whereClause.add(criteriaBuilder.notEqual(pmRoot.get(ProductMaster_.prodId), 0));
		return whereClause;
	}
}
