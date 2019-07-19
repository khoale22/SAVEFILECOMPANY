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
 * Handles searching by Case UPC.
 *
 * @author d116773
 * @since 2.14.0
 */
@Service
public class CaseUpcPredicateBuilder implements PredicateBuilder{

	@Autowired
	private SearchCriteriaRepository repository;

	// TODO: add a new, separate field for case UPC.
	/**
	 * Adds UPCs to the temporary table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@CoreTransactional
	@Override
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getCaseUpcs() == null) {
			return;

		}
		List<SearchCriteria> tempTableData = new LinkedList<>();

		// Add UPCs
		productSearchCriteria.getCaseUpcs().forEach((u) -> {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
			searchCriteria.setSessionId(productSearchCriteria.getSessionId());
			searchCriteria.setUpc(u);
			tempTableData.add(searchCriteria);
		});

		this.repository.save(tempTableData);
	}

	/**
	 * Returns a sub-query joining product_master to prod_item, item_master, and the temp table.
	 * It joins item_master to prod_item and back to product_master.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @return A sub-query joining product_master to prod_scn_codes and the temp table.
	 */
	@Override
	public ExistsClause<ProdItem> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getCaseUpcs() == null) {
			return null;
		}

		// We'l be adding sub-query to prod_item
		Subquery<ProdItem> productExists = queryBuilder.subquery(ProdItem.class);

		// Add all the tables we need to do this query.
		Root<ProdItem> piRoot = productExists.from(ProdItem.class);

		productExists.select(piRoot.get("itemCode"));

		// Join prod_item to item_master
		Join<ProdItem, ItemMaster> piJoinIm = piRoot.join(ProdItem_.itemMaster);

		// Join location to the temp table
		Join<ItemMaster, SearchCriteria> imJoin = piJoinIm.join(ItemMaster_.searchCriteria);


		// Add a join from product_master to prod_item.
		Predicate[] pmToPiJoin = new Predicate[2];
		pmToPiJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), piRoot.get(ProdItem_.productId));
		// Add the bind variable for the session.
		pmToPiJoin[1] = criteriaBuilder.equal(imJoin.get(SearchCriteria_.sessionId), sessionIdBindVariable);

		productExists.where(pmToPiJoin);

		return new ExistsClause<>(productExists);
	}
}
