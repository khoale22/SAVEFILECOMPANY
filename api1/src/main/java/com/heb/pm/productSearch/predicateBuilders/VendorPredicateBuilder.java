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
 * Handles searching by vendor.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
class VendorPredicateBuilder implements PredicateBuilder {

	@Autowired
	private SearchCriteriaRepository repository;

	/**
	 * Adds vendors to the temp table.
	 *
	 * @param productSearchCriteria The user's product search criteria.
	 */
	@CoreTransactional
	@Override
	public void populateTempTable(ProductSearchCriteria productSearchCriteria) {

		if (productSearchCriteria.getVendorNumbers() == null && productSearchCriteria.getVendor() == null) {
			return;
		}

		List<SearchCriteria> tempTableData = new LinkedList<>();

		if (productSearchCriteria.getVendorNumbers() != null) {
			// Searching by vendor, we may get a list of vendors.
			productSearchCriteria.getVendorNumbers().forEach((v) -> {
				SearchCriteria searchCriteria = new SearchCriteria();
				searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
				searchCriteria.setSessionId(productSearchCriteria.getSessionId());
				searchCriteria.setVendorLocationNumber(v);
				tempTableData.add(searchCriteria);
			});
		}

		// Or we may get a singular Vendor.
		if (productSearchCriteria.getVendor() != null) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setUniqueId(PredicateBuilderUtil.getUniqueId());
			searchCriteria.setSessionId(productSearchCriteria.getSessionId());
			searchCriteria.setVendorLocationNumber(productSearchCriteria.getVendor().getVendorNumber());
			tempTableData.add(searchCriteria);
		}

		this.repository.save(tempTableData);
	}

	/**
	 * Returns a sub-query joining product_master, prod_item, item_master, vend_loc_itm, location, and the temp table.
	 * The ultimate constraint is between location and the temp table on ap_number to vend_loc_nbr.
	 *
	 * @param criteriaBuilder Used to construct the various parts of the SQL statement.
	 * @param pmRoot The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param queryBuilder JPA query builder used to construct the sub-query.
	 * @param productSearchCriteria The user's search criteria.
	 * @param sessionIdBindVariable The bind variable to add that will constrain on the user's session in the temp table.
	 * @return A sub-query joining product_master, prod_item, item_master, vend_loc_itm, location, and the temp table.
	 */
	@Override
	public ExistsClause<ProdItem> buildPredicate(CriteriaBuilder criteriaBuilder, Root<? extends ProductMaster> pmRoot,
									  CriteriaQuery<? extends ProductMaster> queryBuilder,
									  ProductSearchCriteria productSearchCriteria,
									  ParameterExpression<String> sessionIdBindVariable) {

		if (productSearchCriteria.getVendor() == null && productSearchCriteria.getVendorNumbers() == null) {
			return null;
		}

		// We'l be adding sub-query to prod_item
		Subquery<ProdItem> productExists = queryBuilder.subquery(ProdItem.class);

		// Add all the tables we need to do this query.
		Root<ProdItem> piRoot = productExists.from(ProdItem.class);

		productExists.select(piRoot.get("itemCode"));

		// Join prod_item to item_master
		Join<ProdItem, ItemMaster> piJoinIm = piRoot.join(ProdItem_.itemMaster);

		// Join item_master to vend_loc_itm
		ListJoin<ItemMaster, VendorLocationItem> imJoinVli = piJoinIm.join(ItemMaster_.vendorLocationItems);

		// Join vend_loc_itm to location
		Join<VendorLocationItem, Location> vliJoinL = imJoinVli.join(VendorLocationItem_.location);

		// Join location to the temp table
		Join<Location, SearchCriteria> lJoinSc = vliJoinL.join(Location_.searchCriteria);


		// Add a join from product_master to prod_item.
		Predicate[] pmToPiJoin = new Predicate[2];
		pmToPiJoin[0] = criteriaBuilder.equal(pmRoot.get(ProductMaster_.prodId), piRoot.get(ProdItem_.productId));
		// Add the bind variable for the session.
		pmToPiJoin[1] = criteriaBuilder.equal(lJoinSc.get(SearchCriteria_.sessionId), sessionIdBindVariable);

		productExists.where(pmToPiJoin);

		return new ExistsClause<>(productExists);
	}
}
