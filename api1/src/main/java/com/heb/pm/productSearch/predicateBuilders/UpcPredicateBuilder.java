package com.heb.pm.productSearch.predicateBuilders;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.repository.SearchCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Handles searching by UPC.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class UpcPredicateBuilder implements PredicateBuilder {

	@Autowired
	private SearchCriteriaRepository repository;

	/**
	 * Adds UPCs to the temporary table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@CoreTransactional
	@Override
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getUpcs() == null) {
			return;

		}
		List<SearchCriteria> tempTableData = new LinkedList<>();

		// Add UPCs
		productSearchCriteria.getUpcs().forEach((u) -> {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
			searchCriteria.setSessionId(productSearchCriteria.getSessionId());
			searchCriteria.setUpc(u);
			tempTableData.add(searchCriteria);
		});

		this.repository.save(tempTableData);
	}

	/**
	 * Returns a sub-query joining product_master to prod_scn_codes and the temp table. It joins prod_scn_codes
	 * back to product_master.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @return A sub-query joining product_master to prod_scn_codes and the temp table.
	 */
	@Override
	public ExistsClause<SellingUnit> buildPredicate(CriteriaBuilder criteriaBuilder,
															 Root<? extends ProductMaster> pmRoot,
															 CriteriaQuery<? extends ProductMaster> queryBuilder,
															 ProductSearchCriteria productSearchCriteria,
															 ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getUpcs() == null) {
			return null;
		}

		// We'll be adding a sub-query to prod_scn_codes
		Subquery<SellingUnit> productExists = queryBuilder.subquery(SellingUnit.class);
		Root<SellingUnit> suRoot = productExists.from(SellingUnit.class);
		productExists.select(suRoot);

		// This joins prod_scn_codes and the temp table together.
		ListJoin<SellingUnit, SearchCriteria> join = suRoot.join(SellingUnit_.searchCriteria);

		// Add a join from product_master to prod_scn_codes
		Predicate[] pmToScJoin = new Predicate[2];
		pmToScJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), suRoot.get(SellingUnit_.prodId));
		// Add the bind variable for session ID.
		pmToScJoin[1] = criteriaBuilder.equal(join.get(SearchCriteria_.sessionId), sessionIdBindVariable);

		// Add the join to the where clause.
		productExists.where(pmToScJoin);

		// Return the sub-query.
		return new ExistsClause<>(productExists);
	}
}
