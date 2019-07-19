package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.CustomSearchEntry;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles building predicates when searching by service case status.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class ServiceCaseStatusPredicateBuilder implements PredicateBuilder {

	/**
	 * Builds a predicate to search for products by service case status.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A subqery that will limit products based on service case status.
	 */
	@Override
	public ExistsClause<?> buildPredicate(CriteriaBuilder criteriaBuilder,
									  Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		// See if there is an entry in the custom searches for service case status.
		if (productSearchCriteria.getCustomSearchEntries() == null) {
			return null;
		}

		CustomSearchEntry serviceCaseStatusEntry = null;
		for (CustomSearchEntry c : productSearchCriteria.getCustomSearchEntries()) {
			if (c.getType() == CustomSearchEntry.SERVICE_CASE_STATUS) {
				serviceCaseStatusEntry = c;
			}
		}
		if (serviceCaseStatusEntry == null) {
			return null;
		}

		// This is not strictly related to product marketing claims, but the front end will send back the same
		// codes as defined in ProductMarketingClaim for the workflow status.
		return new ExistsClause<>(this.getSubquery(criteriaBuilder, pmRoot, queryBuilder, serviceCaseStatusEntry));
	}


	/**
	 * Builds a predicate to search for approved or pending or rejected service case descriptions.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder PA query builder used to construct the sub-query.
	 * @param serviceCaseStatusEntry The custom search entry for service case status.
	 * @return A subquery that will return products with approved or pending or rejected status.
	 */
	private Subquery<CandidateWorkRequest> getSubquery(CriteriaBuilder criteriaBuilder,
															Root<? extends ProductMaster> pmRoot,
															CriteriaQuery<? extends ProductMaster> queryBuilder,
															CustomSearchEntry serviceCaseStatusEntry) {

		Subquery<CandidateWorkRequest> workRequestExists = queryBuilder.subquery(CandidateWorkRequest.class);
		Root<CandidateWorkRequest> wrRoot = workRequestExists.from(CandidateWorkRequest.class);
		workRequestExists.select(wrRoot.get("productId"));

		Predicate[] criteria = new Predicate[3];

		// Join ps_work_request claim back to product master
		criteria[0] = criteriaBuilder.equal(
				pmRoot.get(ProductMaster_.prodId),
				wrRoot.get(CandidateWorkRequest_.productId));

		String statusCode = CandidateWorkRequest.StatusCode.IN_PROGRESS.getName();
		if (serviceCaseStatusEntry.getStringComparator().equals(ProductMarketingClaim.APPROVED)) {
			statusCode = CandidateWorkRequest.StatusCode.SUCCESS.getName();
		}else if (serviceCaseStatusEntry.getStringComparator().equals(ProductMarketingClaim.REJECTED)) {
			statusCode = CandidateWorkRequest.StatusCode.REJECTED.getName();
		}
		criteria[1] = criteriaBuilder.equal(wrRoot.get(CandidateWorkRequest_.status), statusCode);

		// Constraint on the intent ID
		criteria[2] = criteriaBuilder.equal(wrRoot.get(CandidateWorkRequest_.intent),
				CandidateWorkRequest.INTNT_ID_SERVICE_CASE_DESCRIPTION);

		workRequestExists.where(criteria);

		return workRequestExists;
	}

	/**
	 * Builds a predicate to search for approved service case descriptions.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder PA query builder used to construct the sub-query.
	 * @return A subquery that will return products with approved status.
	 */
	private Subquery<ProductDescription> getApprovedSubquery(CriteriaBuilder criteriaBuilder,
															 Root<? extends ProductMaster> pmRoot,
															 CriteriaQuery<? extends ProductMaster> queryBuilder){

		Subquery<ProductDescription> productExists = queryBuilder.subquery(ProductDescription.class);
		Root<ProductDescription> pdRoot = productExists.from(ProductDescription.class);
		productExists.select(pdRoot.get("key").get("productId"));

		Predicate[] constraints = new Predicate[2];

		// Join back to product master
		constraints[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId),
				pdRoot.get(ProductDescription_.key).get(ProductDescriptionKey_.productId));

		// The constraints for the service case description type
		constraints[1] = criteriaBuilder.equal(
				pdRoot.get(ProductDescription_.key).get(ProductDescriptionKey_.descriptionType),
				DescriptionType.SERVICE_CASE_DESCRIPTION);

		productExists.where(constraints);

		return productExists;
	}
}
