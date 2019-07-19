/*
 *  CustomHierarchyPredicateBuilder
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.entity.GenericEntityRelationshipKey_;
import com.heb.pm.entity.GenericEntityRelationship_;
import com.heb.pm.entity.GenericEntity_;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * Handles searching by Customer Hierarchy.
 *
 * @author l730832
 * @since 2.15.0
 */
@Service
class CustomHierarchyPredicateBuilder implements PredicateBuilder {

	private static final String ENTY_TYP_CD = "PROD";

	/**
	 * Returns a sub-query joining product_master to a generic entity. It then compares the child entity with the
	 * entity id. and the parent enty id with the child id. it then compares the hier cntxt cd to the chosen hierarchy
	 * the user has chosen on the front end.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @return A sub-query joining product_master to enty table.
	 */
	@Override
	public ExistsClause<GenericEntityRelationship> buildPredicate(CriteriaBuilder criteriaBuilder,
													Root<? extends ProductMaster> pmRoot,
													CriteriaQuery<? extends ProductMaster> queryBuilder,
													ProductSearchCriteria productSearchCriteria,
													ParameterExpression<String> sessionIdBindVariable) {

		if(productSearchCriteria.getLowestCustomerHierarchyNode() == null) {
			return null;
		}

		Subquery<GenericEntityRelationship> productExists = queryBuilder.subquery(GenericEntityRelationship.class);

		Root<GenericEntityRelationship> gerRoot = productExists.from(GenericEntityRelationship.class);
		productExists.select(gerRoot.get("parentEntityId"));

		// Joins generic entity relationship to generic entity
		Join<GenericEntityRelationship, GenericEntity> gerJoinEn = gerRoot.join(GenericEntityRelationship_.genericChildEntity);

		// Add a join from product master to enty
		Predicate[] pmToEntyJoin = new Predicate[5];
		pmToEntyJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), gerJoinEn.get(GenericEntity_.displayNumber));
		// Compares the child enty id with the enty id
		pmToEntyJoin[1] = criteriaBuilder.equal(gerRoot.get(GenericEntityRelationship_.key).get(GenericEntityRelationshipKey_.childEntityId), gerJoinEn.get(GenericEntity_.id));
		// compares the parent enty id with the child id
		pmToEntyJoin[2] = criteriaBuilder.equal(gerRoot.get(GenericEntityRelationship_.key).get(GenericEntityRelationshipKey_.parentEntityId), productSearchCriteria.getLowestCustomerHierarchyNode().getChildEntityId());
		// compares the hier cntxt cd to the chosen hierarchy in the front end
		pmToEntyJoin[3] = criteriaBuilder.equal(gerRoot.get(GenericEntityRelationship_.key).get(GenericEntityRelationshipKey_.hierarchyContext), productSearchCriteria.getLowestCustomerHierarchyNode().getHierarchyContext());
		// compares the enty typ cd to prod
		pmToEntyJoin[4] = criteriaBuilder.equal(gerJoinEn.get(GenericEntity_.type), ENTY_TYP_CD);

		productExists.where(pmToEntyJoin);

		return new ExistsClause<>(productExists);
	}
}
