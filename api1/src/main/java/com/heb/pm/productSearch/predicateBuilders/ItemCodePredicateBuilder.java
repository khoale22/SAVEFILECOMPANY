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

/**
 * Handles searching by item code.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class ItemCodePredicateBuilder implements PredicateBuilder {

	@Autowired
	private SearchCriteriaRepository repository;

	/**
	 * Adds item codes to the temporary table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@Override
	@CoreTransactional
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getItemCodes() == null) {
			return;
		}

		List<SearchCriteria> tempTableData = new LinkedList<>();

		productSearchCriteria.getItemCodes().forEach((i) -> {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
			searchCriteria.setSessionId(productSearchCriteria.getSessionId());
			searchCriteria.setItemCode(i);
			tempTableData.add(searchCriteria);
		});

		this.repository.save(tempTableData);
	}

	/**
	 * Returns a sub-query joining product_master to prod_item and the temp table. It joins prod_item
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
	public ExistsClause<ProdItem> buildPredicate(CriteriaBuilder criteriaBuilder,
									  Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getItemCodes() == null) {
			return null;
		}

		// We'l be adding sub-query to prod_item
		Subquery<ProdItem> productExists = queryBuilder.subquery(ProdItem.class);
		Root<ProdItem> piRoot = productExists.from(ProdItem.class);
		productExists.select(piRoot.get("itemCode"));

		// Join prod_item and the temp table
		ListJoin<ProdItem, SearchCriteria> join = piRoot.join(ProdItem_.searchCriteria);

		// Add a join from product_master to prod_item.
		Predicate[] pmToPiJoin = new Predicate[3];
		pmToPiJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), piRoot.get(ProdItem_.productId));
		// Add the bind variable for session ID.
		pmToPiJoin[1] = criteriaBuilder.equal(join.get(SearchCriteria_.sessionId), sessionIdBindVariable);
		pmToPiJoin[2] = criteriaBuilder.equal(piRoot.get(ProdItem_.key).get(ProdItemKey_.itemType), ItemMaster.ITEM_TYPE_CODE);
		productExists.where(pmToPiJoin);

		return new ExistsClause<>(productExists);
	}
}
