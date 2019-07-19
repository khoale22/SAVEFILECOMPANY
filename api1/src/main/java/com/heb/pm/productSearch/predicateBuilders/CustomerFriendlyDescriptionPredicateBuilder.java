package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by customer friendly description.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class CustomerFriendlyDescriptionPredicateBuilder implements PredicateBuilder{

	/**
	 * Constructs a subquery that will join product_master to prod_des_txt and constrain on the customer friendly
	 * description types and a string provided by the user.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subquery that will join product_master to prod_des_txt and constrain on the customer friendly
	 * description types and a string provided by the user.
	 */
	@Override
	public ExistsClause<ProductDescription> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (!productSearchCriteria.getSearchByCustomerFriendlyDescription()) {
			return null;
		}

		Subquery<ProductDescription> productExists = queryBuilder.subquery(ProductDescription.class);
		Root<ProductDescription> pdRoot = productExists.from(ProductDescription.class);
		productExists.select(pdRoot.get("key").get("productId"));

		String regex = String.format("%%%s%%", productSearchCriteria.getDescription().toUpperCase());

		Predicate[] constraints = new Predicate[3];

		// The constraint for the description
		constraints[0] = criteriaBuilder.like(criteriaBuilder.upper(
				pdRoot.get(ProductDescription_.description)), regex);

		// The constraints for the tag types
		Predicate[] types = new Predicate[2];
		types[0] = criteriaBuilder.equal(
				pdRoot.get(ProductDescription_.key).get(ProductDescriptionKey_.descriptionType),
				DescriptionType.CUSTOMER_FRIENDLY_DESCRIPTION_LINE_ONE);
		types[1] = criteriaBuilder.equal(
				pdRoot.get(ProductDescription_.key).get(ProductDescriptionKey_.descriptionType),
				DescriptionType.CUSTOMER_FRIENDLY_DESCRIPTION_LINE_TWO);
		constraints[1] = criteriaBuilder.or(types);

		// Join back to product master
		constraints[2] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				pdRoot.get(ProductDescription_.key).get(ProductDescriptionKey_.productId));
		productExists.where(constraints);

		return new ExistsClause<>(productExists);
	}
}
