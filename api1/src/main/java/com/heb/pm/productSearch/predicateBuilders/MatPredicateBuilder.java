package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Handles searching by MAT.
 *
 * @author a786878
 * @since 2.18.4
 */
@Service
class MatPredicateBuilder implements PredicateBuilder {


	@Value("${app.mat.HierarchyContext}")
	private String hierarchyContext;

	/**
	 * MatPredicateBuilder is used to find products that are not in the hierarchy
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 *
	 * @return not exists clause for products that are not in the hierarchy
	 */
	@Override
	public ExistsClause<GenericEntityRelationship> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (!productSearchCriteria.isSearchByMat()) {
			return null;
		}

		Subquery<GenericEntityRelationship> productExists = queryBuilder.subquery(GenericEntityRelationship.class);

		Root<GenericEntityRelationship> geRoot = productExists.from(GenericEntityRelationship.class);

		productExists.select(geRoot.get("childEntityId"));

		// Joins generic entity relationship to generic entity
		Join<GenericEntityRelationship, GenericEntity> gerJoinEn = geRoot.join(GenericEntityRelationship_.genericChildEntity);

		Predicate[] pmToGeJoin = new Predicate[4];

		// Add a join from product_master to generic entity.
		pmToGeJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), gerJoinEn.get(GenericEntity_.displayNumber));

		// Compares the child enty id with the enty id
		pmToGeJoin[1] = criteriaBuilder.equal(geRoot.get(GenericEntityRelationship_.key).get(GenericEntityRelationshipKey_.childEntityId), gerJoinEn.get(GenericEntity_.id));

		// compares the hier cntxt cd to the configured hierarchy for MAT
		pmToGeJoin[2] = criteriaBuilder.equal(geRoot.get(GenericEntityRelationship_.key).get(GenericEntityRelationshipKey_.hierarchyContext), hierarchyContext);

		// only look at PROD records in the entity table
		pmToGeJoin[3] = criteriaBuilder.equal(gerJoinEn.get(GenericEntity_.type), GenericEntity.EntyType.PROD.getName());

		productExists.where(pmToGeJoin);

		return new ExistsClause<>(false, productExists);
	}
}
