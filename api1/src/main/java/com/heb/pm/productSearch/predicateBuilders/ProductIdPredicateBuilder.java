package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductMaster_;
import com.heb.pm.entity.SearchCriteria;
import com.heb.pm.entity.SearchCriteria_;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.repository.SearchCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Handles searching by product ID.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class ProductIdPredicateBuilder implements PredicateBuilder {

	@Autowired
	private SearchCriteriaRepository repository;

	/**
	 * Adds all the product IDs to the temporary table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@Override
	@CoreTransactional
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getProductIds() == null) {
			return;
		}

		List<SearchCriteria> tempTableData = new LinkedList<>();

		// Add product IDs
		productSearchCriteria.getProductIds().forEach((p) -> {
			if(p>0){
                SearchCriteria searchCriteria = new SearchCriteria();
                searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
                searchCriteria.setSessionId(productSearchCriteria.getSessionId());
                searchCriteria.setProductId(p);
                tempTableData.add(searchCriteria);
			}
		});

		this.repository.save(tempTableData);
	}

	/**
	 * Returns a sub-query joining product_master to the temp table on product ID.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A sub-query joining product_master to the temp table on product ID.
	 */
	@Override
	public ExistsClause<SearchCriteria> buildPredicate(CriteriaBuilder criteriaBuilder,
														   Root<? extends ProductMaster> pmRoot,
														   CriteriaQuery<? extends ProductMaster> queryBuilder,
														   ProductSearchCriteria productSearchCriteria,
												    	   ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getProductIds() == null) {
			return null;
		}
		// We'll be adding a sub-query for the temp table.
		Subquery<SearchCriteria> productExists = queryBuilder.subquery(SearchCriteria.class);
		Root<SearchCriteria> scRoot = productExists.from(SearchCriteria.class);
		productExists.select(scRoot);

		// Add a join on product_id from product master to the same  filed in the temp table.
		Predicate[] pmToScJoin = new Predicate[2];
		pmToScJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), scRoot.get(SearchCriteria_.productId));
		pmToScJoin[1] = criteriaBuilder.equal(scRoot.get(SearchCriteria_.sessionId), sessionIdBindVariable);

		// Add the join to the where clause.
		productExists.where(pmToScJoin);

		// Return the sub-query.
		return new ExistsClause<>(productExists);
	}
}
