package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles searching by product description.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ProductDescriptionPredicateBuilder implements PredicateBuilder  {

	/**
	 * Builds a where clause that constrains product master on prod_eng_des.
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

		if (!productSearchCriteria.getSearchByProductDescription()) {
			return null;
		}

		List<Predicate> whereClause = new LinkedList<>();

		String regex = String.format("%%%s%%", productSearchCriteria.getDescription().toUpperCase());

		whereClause.add(criteriaBuilder.like(pmRoot.get(ProductMaster_.description), regex));
		return  whereClause;
	}
}
